package com.mod.rest.service;

import com.mod.rest.model.*;
import com.mod.rest.repository.RiskRepository;
import com.mod.rest.repository.TaskRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim.omaya on 12/27/2019.
 */
@Service
public class ReportService {

    @Autowired
    RiskService riskService;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserHelperService userHelperService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    SessionService sessionService;
    @Autowired
    ProjectService projectService;
    @Autowired
    RiskRepository riskRepository;


    public ReportObject buildReportObject(ReportObject reportObject){
        if (reportObject.getReportType()<= 6){
            if (reportObject.getUserIds().equals(";")){
                NodeList nodeList = userHelperService.getSubUsers(reportObject.getSAMLart());
                String s = ";";
                long[] uIds= new long[nodeList.getLength()];
                for (int i=0; i< nodeList.getLength(); i++){

                    Node node = nodeList.item(i);
                    s += node.getTextContent()+";";
                    uIds[i] = Long. parseLong(node.getTextContent());
                }
                reportObject.setUsers(uIds);
                reportObject.setUserIds(s);
            }
        }

        if (reportObject.getReportType() == 6) {
            if (reportObject.getRiskIds().equals("")) {
                reportObject.setRiskIds(riskService.getRisksByUser(reportObject.getSAMLart()));
            }
        }else if(reportObject.getReportType() == 4){
            if (reportObject.getProjectIds().equals("")) {
                reportObject.setProjectIds(projectService.getProjectUnderHeadUnit(reportObject.getSAMLart()));
            }
        }
        return reportObject;
    }


    public <T> T execute( ReportObject reportObject){

        if (reportObject.getReportType() == 10){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskReportHelper(reportObject, "delayedRiskReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) riskRepository.getDelayedRisks(reportObject.getPageNumber(), reportObject.getPageSize(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) riskRepository.getDelayedRisksCount(ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file

            }
        }

        if (reportObject.getReportType() == 6){
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  reportHelper(reportObject, "riskReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskService.addUserToTask(taskRepository.getIntegrationTaskReport(reportObject.getUserIds(), reportObject.getRiskIds(), "RiskManagement", reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.cGetIntegrationTaskReport(reportObject.getUserIds(), reportObject.getRiskIds(), "RiskManagement");
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<Task> tasks=  taskRepository.getIntegrationTaskReport(reportObject.getUserIds(), reportObject.getRiskIds(), "RiskManagement", 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }else if (reportObject.getReportType() == 1) {
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  reportHelper(reportObject, "delayedTaskReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskService.addUserToTask(taskRepository.getDelayedTasks(reportObject.getUserIds(), reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.cDelayedTask(reportObject.getUserIds());
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<Task> tasks = taskRepository.getDelayedTasks(reportObject.getUserIds(), 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }else if (reportObject.getReportType() == 2) {
            if (reportObject.getDetectedReportType() == 0) { // graph
                return (T) reportDateHelper(reportObject, "UserProductivityReport");
            } else if (reportObject.getDetectedReportType() == 1) { // table
                return (T) taskService.addUserToTask(taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds(), reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.cUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds());
            } else if (reportObject.getDetectedReportType() == 3) { // file
                List<Task> tasks = taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds(), 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }  else if (reportObject.getReportType() == 3) {
            if (reportObject.getDetectedReportType() == 0) { // graph
                return (T) reportDateHelper(reportObject, "CompletedTaskReport");
            } else if (reportObject.getDetectedReportType() == 1) { // table
                Long userId = sessionService.getSession(reportObject.getSAMLart()).getId();
                return (T) taskService.addUserToTask(taskRepository.getCompletedTaskReport(reportObject.getUserIds(), userId, reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                Long userId = sessionService.getSession(reportObject.getSAMLart()).getId();
                return (T) taskRepository.cCompletedTaskReport(reportObject.getUserIds(), userId, reportObject.getStartDate(), reportObject.getEndDate());
            } else if (reportObject.getDetectedReportType() == 3) { // file
                Long userId = sessionService.getSession(reportObject.getSAMLart()).getId();
                List<Task> tasks = taskRepository.getCompletedTaskReport(reportObject.getUserIds(), userId, reportObject.getStartDate(), reportObject.getEndDate(), 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }else if(reportObject.getReportType() == 4){
            if (reportObject.getDetectedReportType() == 0) { // graph
                return (T) reportHelper(reportObject, getProjectReportType(reportObject.getStatus()));
            } else if (reportObject.getDetectedReportType() == 1) { // table
                return (T) taskService.addUserToTask(taskService.getTaskReportProjectBasedOnStatus(reportObject.getStatus(), reportObject.getUserIds(), reportObject.getProjectIds(), reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getSAMLart()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                if (reportObject.getStatus() == 1) { // delayed
                    return (T) taskRepository.cDelayedTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getProjectIds());
                }else if (reportObject.getStatus() == 2) { // finished
                    return (T) taskRepository.cFinishedTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getProjectIds());
                }else { // all
                    return (T) taskRepository.cFinishedAndDelayedTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getProjectIds());
                }
            } else if (reportObject.getDetectedReportType() == 3) { // file
                List<Task> tasks =  taskService.getTaskReportProjectBasedOnStatus(reportObject.getStatus(), reportObject.getUserIds(), reportObject.getProjectIds(), reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getSAMLart());
                return (T) excelWriterService.generate(tasks);
            }

        }

        return null;
    }

    private List<GraphDataHelper> riskReportHelper(ReportObject reportObject, String type){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long[] users = reportObject.getUsers();


        for(int i=0; i< users.length; i++){
            List<Risk> riskList = null;
            if (type.equals("delayedRiskReport")){
                riskList = riskRepository.getDelayedRisks(1, Integer.MAX_VALUE,  users[i] +"");
            }

            int[] riskDate = new int[12];
            for (Risk risk : riskList) {
                int num = Utils.getMonthFromDate(risk.getRiskSolutionDate());
                riskDate[num] += 1;
            }
            GraphDataHelper graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
            graphDataHelper.setData(riskDate);
            graphDataHelpers.add(graphDataHelper);
        }
        return graphDataHelpers;

    }

    public List<GraphDataHelper> reportHelper(ReportObject reportObject, String type ){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long[] users = reportObject.getUsers();

        for(int i=0; i< users.length; i++){
            List<Task> taskList = null;

            String user = ";"+  users[i] +";";
            if (type == "riskReport"){
                taskList = taskRepository.getIntegrationTaskReport(user, reportObject.getRiskIds(), "RiskManagement", 1, Integer.MAX_VALUE );
            } else if (type == "delayedTaskReport"){
                taskList = taskRepository.getDelayedTasks(user, 1, Integer.MAX_VALUE);
            }else if(type == "DelayedTaskReportProject"){
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getDelayedTaskReportProject(";"+users[i]+";", userHelper.getId(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            }else if(type == "FinishedTaskReportProject"){
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getFinishedTaskReportProject(";"+users[i]+";", userHelper.getId(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            } else if(type == "AllTaskReportProject") {
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getFinishedAndDelayedTaskReportProject(";"+users[i]+";", userHelper.getId(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            }
            if (taskList.size() == 0) continue;
            int[] taskDate = new int[12];
            for (Task task : taskList) {
                int num = Utils.getMonthFromDate(task.getDueDate());
                taskDate[num] += 1;
            }
            GraphDataHelper graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
            graphDataHelper.setData(taskDate);
            graphDataHelpers.add(graphDataHelper);
        }
        return graphDataHelpers;
    }

    private String getProjectReportType(int projectStatus){
        if (projectStatus == 1) { // delayed
            return  "DelayedTaskReportProject";
        }else if (projectStatus == 2) { // finished
            return "FinishedTaskReportProject";
        }else { // all
            return "AllTaskReportProject";
        }
    }

    public List<GraphDataHelper> reportDateHelper( ReportObject reportObject,  String type ){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long days = Utils.differenceBetweenTwoDates(reportObject.getStartDate(), reportObject.getEndDate());
        long[] users = reportObject.getUsers();

        int arraySize = (days >= 0 && days <= 8)? 7 : (days > 8 && days <= 31)? 31 : 12;

        for(int i=0; i< users.length; i++){
            List<Task> taskList = null;
            String user = ";"+  users[i] +";";
            if(type == "UserProductivityReport"){
                taskList = taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(),user, 1, Integer.MAX_VALUE);
            }else if(type == "CompletedTaskReport"){
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getCompletedTaskReport(user, userHelper.getId(), reportObject.getStartDate(), reportObject.getEndDate(), 1, Integer.MAX_VALUE);
            }

            if (taskList.size() == 0) continue;

            int[] taskDate = new int[arraySize];
            for (Task task : taskList) {
                int num = 0;
                if (arraySize == 7){
                    num = Utils.getDayNameFromDate(task.getDueDate());
                }else if (arraySize == 31){
                    num = Utils.getDayFromDate(task.getDueDate());
                }else if (arraySize == 12){
                    num = Utils.getMonthFromDate(task.getDueDate());
                }
                taskDate[num] += 1;
            }
            GraphDataHelper graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
            graphDataHelper.setData(taskDate);
            graphDataHelpers.add(graphDataHelper);
        }
        return graphDataHelpers;
    }
}
