package com.mod.rest.service;

import com.mod.rest.model.*;
import com.mod.rest.repository.*;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    @Autowired
    EntityRepository entityRepository;
    @Autowired
    IndividualRepository individualRepository;
	@Autowired
    StatisticsRepository statisticsRepository;
	@Autowired
    TaskReportHelperRepository taskReportHelperRepository;
	@Autowired
    TechnicalSupportRepository technicalSupportRepository;

    public ReportObject buildReportObject(ReportObject reportObject){
        if (reportObject.getReportType()<= 19){
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

        if (reportObject.getReportType() == 6 || reportObject.getReportType() == 14 || reportObject.getReportType() == 17) {
            if (reportObject.getRiskIds().equals("")) {
                reportObject.setRiskIds(riskService.getRisksByUser(reportObject.getSAMLart()));
            }
        }else if(reportObject.getReportType() == 4 || reportObject.getReportType() == 15){
            if (reportObject.getProjectIds().equals("")) {
                reportObject.setProjectIds(projectService.getProjectUnderHeadUnit(reportObject.getSAMLart()));
            }
        }
        return reportObject;
    }


    public <T> T execute( ReportObject reportObject){

        //Case طلب دعم فني
        if (reportObject.getReportType() == 30 ) {
            List<TechnicalSupportReport> technicalSupportReports = technicalSupportRepository.getTechnicalSupportStatistics(reportObject.getStartDate(),reportObject.getEndDate());
            return (T) excelWriterService.generate(technicalSupportReports);
        }else if (reportObject.getReportType() == 31 ) {
            List<TechnicalSupportReport> technicalSupportReports = technicalSupportRepository.getTechnicalSupportStatistics(reportObject.getStartDate(),reportObject.getEndDate());
            return (T) excelWriterService.generate(technicalSupportReports);
        }

        // Case Contact Tracker
        if (reportObject.getReportType() == 20 ) {
            List<EntityReport> entityReports = entityRepository.getEntitiesByType(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityType(),reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getTags());
            return (T) excelWriterService.generate(entityReports);
        }else if (reportObject.getReportType() == 21 ) {
            List<EntityReport> entityReports = entityRepository.getPrivateEntities(1, Integer.MAX_VALUE, "", "" ,reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getIsRegistered(),reportObject.getLicenseNumber(),reportObject.getTags());
            return (T) excelWriterService.generate(entityReports);
        }else if (reportObject.getReportType() == 22 ) {
            List<IndividualReport> individualReports = individualRepository.getIndividuals(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition(),reportObject.getTags());
            return (T) excelWriterService.generate(individualReports);
        }


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
//                List<Risk> riskExcel = (T) riskRepository.getDelayedRisks(reportObject.getPageNumber(), reportObject.getPageSize(), ids);
//                return (T) excelWriterService.generate(riskExcel);
            }
        }
        if (reportObject.getReportType() == 14){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  reportHelper(reportObject, "delayedTaskRiskReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskRepository.getDelayedTaskRiskReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.getDelayedTaskRiskReportCount( reportObject.getRiskIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file

            }
        }

        if (reportObject.getReportType() == 15){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskReportHelper(reportObject, "activityProjectRiskRelated");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) riskRepository.getRiskRelatedToProjectReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getProjectIds(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) riskRepository.getRiskRelatedToProjectReportCount(reportObject.getProjectIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file

            }
        }
        if (reportObject.getReportType() == 16){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskReportHelper(reportObject, "userProductivityRiskReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) riskRepository.getClosedRisksReport(reportObject.getPageNumber(), reportObject.getPageSize(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) riskRepository.getClosedRisksReportCount(ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file

            }
        }
        if (reportObject.getReportType() == 17){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  reportHelper(reportObject, "inProgressDelayedClosedTaskRisksReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskRepository.getInProgressDelayedClosedTaskRisksReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.getInProgressDelayedClosedTaskRisksReportCount( reportObject.getRiskIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file

            }
        }
        if (reportObject.getReportType() == 18){

            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskStatisticsReportHelper(reportObject);
            }else  if (reportObject.getDetectedReportType() == 1){ // table
//                return (T) riskRepository.getUserRiskStatisticsReport( reportObject.getStartDate(), reportObject.getEndDate(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count

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
                List<TaskReportHelper> tasks=  taskReportHelperRepository.getIntegrationTaskReport(reportObject.getUserIds(), reportObject.getRiskIds(), "RiskManagement", 1, Integer.MAX_VALUE);
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
                List<TaskReportHelper> tasks = taskReportHelperRepository.getDelayedTasks(reportObject.getUserIds(), 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }else if (reportObject.getReportType() == 2) { // if user productivity report
            if (reportObject.getDetectedReportType() == 0) { // graph
                return (T) userProductivityReportDateHelper(reportObject);
            } else if (reportObject.getDetectedReportType() == 1) { // table
                return (T) taskService.addUserToTask(taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds(), reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.cUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds());
            } else if (reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks = taskReportHelperRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds(), 1, Integer.MAX_VALUE);
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
                List<TaskReportHelper> tasks = taskReportHelperRepository.getCompletedTaskReport(reportObject.getUserIds(), userId, reportObject.getStartDate(), reportObject.getEndDate(), 1, Integer.MAX_VALUE);
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
                List<TaskReportHelper> tasks =  taskService.getTaskReportHelperProjectBasedOnStatus(reportObject.getStatus(), reportObject.getUserIds(), reportObject.getProjectIds(), reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getSAMLart());
                return (T) excelWriterService.generate(tasks);
            }

        }

        return null;
    }

    public HashMap<List, List> riskStatisticsReportHelper(ReportObject reportObject){

        HashMap<List, List> result = new HashMap<>();
        ArrayList arrayList = new ArrayList();

        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        UserHelper user = sessionService.getSession(reportObject.getSAMLart());
        user.getId();
        long[] users = reportObject.getUsers();

        int poistion = 0;

        int total = users.length;
        int[] riskDataOpened = new int[total];
        int[] riskDataClosed = new int[total];

        GraphDataHelper graphDataHelperOpened = new GraphDataHelper();
        GraphDataHelper graphDataHelperClosed = new GraphDataHelper();
        String usedNameOpened = "";
        String usedNameClosed = "";

        for(int i=0; i< users.length; i++){

            List<Statistics> statisticss = statisticsRepository.getUserRiskStatisticsReport(users[i],reportObject.getStartDate(), reportObject.getEndDate());

            int inProgress = statisticss.get(0).getInProgress();
            int closed = statisticss.get(0).getEnded();

            if (inProgress == 0 && closed == 0){
                total--;
                for (GraphDataHelper g : graphDataHelpers) g.setData((int[]) resizeArray(g.getData(), total));
                poistion++;
                continue;
            }

            riskDataOpened[i-poistion] = inProgress;
            riskDataClosed[i-poistion] = closed;
            String name = userRepository.findById(users[i]).get().getDisplayName();
            arrayList.add(name);

            usedNameOpened =  " مفتوحه";
            usedNameClosed =  " مغلقة";

//            riskData = new int[total];
//            riskData[i-poistion] = closed;
//            usedName = name + " مغلقة";
//            graphDataHelper = new GraphDataHelper();
//            graphDataHelper.setName(usedName);
//            graphDataHelper.setData(riskData);
//            graphDataHelpers.add(graphDataHelper);


        }
        graphDataHelperOpened.setName(usedNameOpened);
        graphDataHelperClosed.setName(usedNameClosed);
        graphDataHelperClosed.setData(riskDataClosed);
        graphDataHelperOpened.setData(riskDataOpened);
        graphDataHelpers.add(graphDataHelperOpened);
        graphDataHelpers.add(graphDataHelperClosed);

        result.put(graphDataHelpers, arrayList);

        return result;

    }

    private Object resizeArray (Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0)
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        return newArray;
    }


    private List<GraphDataHelper> riskReportHelper(ReportObject reportObject, String type){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long[] users = reportObject.getUsers();

        for(int i=0; i< users.length; i++){
            List<Risk> riskList = null;
            if (type.equals("delayedRiskReport")){
                riskList = riskRepository.getDelayedRisks(1, Integer.MAX_VALUE,  users[i] +"");
                if (riskList.size() == 0) continue;

                int[] riskDate = new int[12];
                for (Risk risk : riskList) {
                    int num = Utils.getMonthFromDate(risk.getRiskSolutionDate());
                    riskDate[num] += 1;
                }
                GraphDataHelper graphDataHelper = new GraphDataHelper();
                graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                graphDataHelper.setData(riskDate);
                graphDataHelpers.add(graphDataHelper);

            }else if (type.equals("activityProjectRiskRelated")){
                riskList = riskRepository.getRiskRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), users[i] +"" );
                if (riskList.size() == 0) continue;

                int[] riskDate = new int[12];
                for (Risk risk : riskList) {
                    int num = Utils.getMonthFromDate(risk.getRiskSolutionDate());
                    riskDate[num] += 1;
                }
                GraphDataHelper graphDataHelper = new GraphDataHelper();
                graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                graphDataHelper.setData(riskDate);
                graphDataHelpers.add(graphDataHelper);

            } else if (type.equals("userProductivityRiskReport")){

                riskList = riskRepository.getClosedRisksReport(1, Integer.MAX_VALUE, users[i] +"" );
                if (riskList.size() == 0) continue;

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
            }else if (type.equals("delayedTaskRiskReport")){
                taskList = taskRepository.getDelayedTaskRiskReport(1, Integer.MAX_VALUE, reportObject.getRiskIds(), users[i] +"" );
            }else if (type.equals("inProgressDelayedClosedTaskRisksReport")){
                taskList = taskRepository.getInProgressDelayedClosedTaskRisksReport(1, Integer.MAX_VALUE, reportObject.getRiskIds(), users[i] +"" );
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

    public List<GraphDataHelper> userProductivityReportDateHelper( ReportObject reportObject){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long days = Utils.differenceBetweenTwoDates(reportObject.getStartDate(), reportObject.getEndDate());
        long[] users = reportObject.getUsers();

        int arraySize = users.length;

        int[] completedTasks = new int[arraySize];
        int[] inProgressTasks = new int[arraySize];
        int[] delayedTasks = new int[arraySize];

        for(int i=0; i< users.length; i++){
            List<Task> taskList = null;
            String user = ";"+  users[i] +";";

            taskList = taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(),user, 1, Integer.MAX_VALUE);


            if (taskList.size() == 0) continue;

            int[] taskDate = new int[arraySize];
            for (Task task : taskList) {
//                if(task.getProgress() == 100){
//                    completedTasks[i] += 1;
//                }else {
//                    if (task.getDueDate().before(new Date())) {
//                        delayedTasks[i] += 1;
//                    } else {
//                        inProgressTasks[i] += 1;
//                    }
//                }
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

    public List<GraphDataHelper> reportDateHelper( ReportObject reportObject,  String type ){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long days = Utils.differenceBetweenTwoDates(reportObject.getStartDate(), reportObject.getEndDate());
        long[] users = reportObject.getUsers();

        int arraySize = (days >= 0 && days <= 8)? 7 : (days > 8 && days <= 31)? 31 : 12;

        for(int i=0; i< users.length; i++){
            List<Task> taskList = null;
            String user = ";"+  users[i] +";";
//            if(type == "UserProductivityReport"){
//                taskList = taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(),user, 1, Integer.MAX_VALUE);
//            }else
            if(type == "CompletedTaskReport"){
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
