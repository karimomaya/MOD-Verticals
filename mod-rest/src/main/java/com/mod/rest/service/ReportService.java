package com.mod.rest.service;

import com.mod.rest.model.*;
import com.mod.rest.repository.*;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.*;


/**
 * Created by karim.omaya on 12/27/2019.
 */
@Service
public class ReportService {

    @Autowired
    RiskService riskService;
    @Autowired
    IssueService issueService;
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
    ProjectRepository projectRepository;
    @Autowired
    ProjectService projectService;
    @Autowired
    RiskRepository riskRepository;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    EntityRepository entityRepository;
    @Autowired
    IndividualRepository individualRepository;
	@Autowired
    StatisticsRepository statisticsRepository;
	@Autowired
    TaskReportHelperRepository taskReportHelperRepository;
    @Autowired
	RiskReportHelperRepository riskReportHelperRepository;
    @Autowired
    IssueReportHelperRepository issueReportHelperRespository;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    public ReportObject buildReportObject(ReportObject reportObject){
        if (reportObject.getReportType()<= 19 || reportObject.getReportType() == 30 ||
                reportObject.getReportType() == 34 || reportObject.getReportType() == 35
                || reportObject.getReportType() == 36 || reportObject.getReportType() == 37
                || reportObject.getReportType() == 38 || reportObject.getReportType() == 19){

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

        if (reportObject.getReportType() == 6 || reportObject.getReportType() == 14 || reportObject.getReportType() == 17 ) {
            if (reportObject.getRiskIds().equals("")) {
                reportObject.setRiskIds(riskService.getRisksByUser(reportObject.getSAMLart()));
            }
        }else if(reportObject.getReportType() == 4 || reportObject.getReportType() == 7 || reportObject.getReportType() == 15 || reportObject.getReportType() == 35 ){
            if (reportObject.getProjectIds().equals("")) {
                reportObject.setProjectIds(projectService.getProjectUnderHeadUnit(reportObject.getSAMLart()));
            }
        }else if( reportObject.getReportType() == 34 || reportObject.getReportType() == 37) {
            if (reportObject.getIssueIds().equals("")) {
                reportObject.setIssueIds(issueService.getIssuesByUser(reportObject.getSAMLart()));
            }
        }
        return reportObject;
    }

    public <T> T execute(ReportObject reportObject){

        //Case طلبات شراء
        if (reportObject.getReportType() == 50 ) {
            List<PurchaseOrderReport> purchaseOrderReports = purchaseOrderRepository.getPurchaseOrderReport(reportObject.getStartDateString(),reportObject.getEndDateString(), reportObject.getEntityName(), 0, Integer.MAX_VALUE, reportObject.getInput());
            return (T) excelWriterService.generate(purchaseOrderReports);
        }
        // Case Contact Tracker
        if (reportObject.getReportType() == 20 ) {
            List<EntityReport> entityReports = entityRepository.getEntitiesByType(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityType(),reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getTags());
            return (T) excelWriterService.generate(entityReports);
        } else if (reportObject.getReportType() == 21 ) {
            List<EntityReport> entityReports = entityRepository.getPrivateEntities(1, Integer.MAX_VALUE, "", "" ,reportObject.getNameArabic(),reportObject.getNameEnglish(),reportObject.getPhone(),reportObject.getIsRegistered(),reportObject.getLicenseNumber(),reportObject.getSupplierStatus(),reportObject.getTags());
            return (T) excelWriterService.generate(entityReports);
        }else if (reportObject.getReportType() == 22 ) {
            List<IndividualReport> individualReports = individualRepository.getIndividuals(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition(),reportObject.getTags());
            return (T) excelWriterService.generate(individualReports);
        }else if (reportObject.getReportType() == 23) {
            List<User> ministryUsersReports = userRepository.getMinistryUsers(1, Integer.MAX_VALUE, "", "" ,reportObject.getEntityName(),reportObject.getName(),reportObject.getPosition());
            return (T) excelWriterService.generate(ministryUsersReports);
        }


        //•	تقرير التحديات المتأخرة
        if (reportObject.getReportType() == 10){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskReportHelper(reportObject, "delayedRiskReport");
            } else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) riskRepository.getDelayedRisks(reportObject.getPageNumber(), reportObject.getPageSize(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) riskRepository.getDelayedRisksCount(ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<RiskReportHelper> risks = riskReportHelperRepository.getDelayedRisks( 1, Integer.MAX_VALUE, ids);
                return (T) excelWriterService.generate(risks);
            }
        }
        //•	تقرير المهام المتأخرةالمتعلقة بالتحديات
        if (reportObject.getReportType() == 14){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
               // reportHelper(reportObject, "delayedTaskRiskReport");
                return (T) taskReportHelper(reportObject, "delayedTaskRiskReport");

              //  return (T) taskReportUserHelper(reportObject, "delayedTaskRiskReport");
            } else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskService.addUserToTask(taskRepository.getDelayedTaskRiskReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds(), ids));
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.getDelayedTaskRiskReportCount( reportObject.getRiskIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks =  taskReportHelperRepository.getDelayedTaskRiskReport(1,  Integer.MAX_VALUE,reportObject.getRiskIds(),ids);
                return (T) excelWriterService.generate(tasks);
            }
        }
        //•	تقرير التحديات المرتبطة بمشروع/نشاط معين
        if (reportObject.getReportType() == 15){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskReportHelper(reportObject, "activityProjectRiskRelated");
            } else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) riskRepository.getRiskRelatedToProjectReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getProjectIds(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) riskRepository.getRiskRelatedToProjectReportCount(reportObject.getProjectIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<RiskReportHelper> risks = riskReportHelperRepository.getRiskRelatedToProjectReport( 1, Integer.MAX_VALUE, reportObject.getProjectIds(),ids);
                return (T) excelWriterService.generate(risks);
            }
        }
        //•	تقارير إنتاجية مستخدم/مستخدمين في فترة محددة في مواجهة التحديات.
        if (reportObject.getReportType() == 16){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskReportHelper(reportObject, "userProductivityRiskReport");
            } else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) riskRepository.getClosedRisksReport(reportObject.getPageNumber(), reportObject.getPageSize(), ids, reportObject.getStartDate(), reportObject.getEndDate());
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) riskRepository.getClosedRisksReportCount(ids, reportObject.getStartDate(), reportObject.getEndDate());
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<RiskReportHelper> risks = riskReportHelperRepository.getClosedRisksReport( 1, Integer.MAX_VALUE,ids);
                return (T) excelWriterService.generate(risks);

            }
        }
        //•	تقرير المهام المنجزة/ المتأخرة في حل تحدي
        if (reportObject.getReportType() == 17){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T) taskReportHelper(reportObject, "AllTaskReportRisk");
              //  return (T)  reportHelper(reportObject, "inProgressDelayedClosedTaskRisksReport");
            } else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskService.addUserToTask(taskRepository.getInProgressDelayedClosedTaskRisksReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds(), ids));
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.getInProgressDelayedClosedTaskRisksReportCount( reportObject.getRiskIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks =  taskReportHelperRepository.getInProgressDelayedClosedTaskRisksReport(1,  Integer.MAX_VALUE,reportObject.getRiskIds(),ids);
                return (T) excelWriterService.generate(tasks);
            }
        }
        //18 -> •	تقارير إحصائية بالتحديات وحالتها في فترة محددة
        //38 -> •	تقارير إحصائية بالمخاطر وحالتها في فترة محددة
        if (reportObject.getReportType() == 18 || reportObject.getReportType() == 38){

            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  riskStatisticsReportHelper(reportObject);
            } else  if (reportObject.getDetectedReportType() == 1){ // table

            } else if(reportObject.getDetectedReportType() == 2) { // count

            } else if(reportObject.getDetectedReportType() == 3) { // file

            }
        }
        ////•	تقرير المخاطر المتأخرة
        if (reportObject.getReportType() == 30){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  issueReportHelper(reportObject, "delayedIssueReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) issueRepository.getDelayedIssues(reportObject.getPageNumber(), reportObject.getPageSize(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) issueRepository.getDelayedIssuesCount(ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<IssueReportHelper> issues =  issueReportHelperRespository.getDelayedIssues(1, Integer.MAX_VALUE,ids);
                return (T) excelWriterService.generate(issues);
            }
        }
        //•	تقرير المهام المتأخرةالمتعلقة بالمخاطر
        if (reportObject.getReportType() == 34){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
               // return (T)  reportHelper(reportObject, "delayedTaskIssueReport");
                return (T) taskReportHelper(reportObject, "delayedTaskIssueReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskRepository.getDelayedTaskIssueReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getIssueIds(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.getDelayedTaskIssueReportCount( reportObject.getIssueIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks =  taskReportHelperRepository.getDelayedTaskIssueReport(1,  Integer.MAX_VALUE,reportObject.getIssueIds(),ids);
                return (T) excelWriterService.generate(tasks);
            }
        }
        //•	تقرير المخاطر المرتبطة بمشروع/نشاط معين
        if (reportObject.getReportType() == 35){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  issueReportHelper(reportObject, "activityProjectIssueRelated");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) issueRepository.getIssueRelatedToProjectReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getProjectIds(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) issueRepository.getIssueRelatedToProjectReportCount(reportObject.getProjectIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<IssueReportHelper> issues =  issueReportHelperRespository.getIssueRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), ids);
                return (T) excelWriterService.generate(issues);
            }
        }
        //•	تقارير إنتاجية مستخدم/مستخدمين في فترة محددة في مواجهة المخاطر.
        if (reportObject.getReportType() == 36){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T)  issueReportHelper(reportObject, "userProductivityRiskReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) issueRepository.getClosedIssuesReport(reportObject.getPageNumber(), reportObject.getPageSize(), ids);
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) issueRepository.getClosedIssuesReportCount(ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<IssueReportHelper> issues =  issueReportHelperRespository.getClosedIssuesReport(1, Integer.MAX_VALUE, ids);
                return (T) excelWriterService.generate(issues);
            }
        }
        //•	تقرير المهام المنجزة/ المتأخرة في حل خطر
        if (reportObject.getReportType() == 37){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                return (T) taskReportHelper(reportObject, "AllTaskReportIssue");
                // return (T)  reportHelper(reportObject, "inProgressDelayedClosedTaskIssuesReport");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) taskService.addUserToTask(taskRepository.getInProgressDelayedClosedTaskIssuesReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getIssueIds(), ids));
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.getInProgressDelayedClosedTaskIssuesReportCount( reportObject.getIssueIds(), ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks =  taskReportHelperRepository.getInProgressDelayedClosedTaskIssuesReport(1, Integer.MAX_VALUE, reportObject.getIssueIds(), ids);
                return (T) excelWriterService.generate(tasks);
            }
        }
        if (reportObject.getReportType() == 19){
            String ids = "";
            for (int i=0; i<reportObject.getUsers().length; i++){
                if (i != 0) ids += ",";
                ids += reportObject.getUsers()[i]+"";
            }
            if (reportObject.getDetectedReportType() == 0){ // graph
                // return (T)  reportHelper(reportObject, "delayedTaskIssueReport");
                return (T)  issueReportHelper(reportObject, "issuesByPrioirtyThenPrecedence");
            }else  if (reportObject.getDetectedReportType() == 1){ // table
                return (T) issueRepository.getIssuesByPriorityThenPrecedence(ids, reportObject.getPageNumber(), reportObject.getPageSize());
            } else if(reportObject.getDetectedReportType() == 2) { // count
                return (T) issueRepository.getIssuesByPriorityThenPrecedenceCount(ids);
            } else if(reportObject.getDetectedReportType() == 3) { // file
                List<IssueReportHelper> issues =  issueReportHelperRespository.getIssuesByPriorityThenPrecedence(ids, 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(issues);
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
                List<TaskReportHelper> tasks =  taskReportHelperRepository.getIntegrationTaskReport(reportObject.getUserIds(), reportObject.getRiskIds(), "RiskManagement", 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }else if (reportObject.getReportType() == 1) {
            if (reportObject.getDetectedReportType() == 0){ // graph
//                return (T)  reportHelper(reportObject, "delayedTaskReport");
                return (T)  taskReportUserHelper(reportObject, "DelayedTaskReport");
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
                return (T) taskReportUserHelper(reportObject, "UserProductivityReport");
            } else if (reportObject.getDetectedReportType() == 1) { // table
                return (T) taskService.addUserToTask(taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds(), reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.cUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds());
            } else if (reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks = taskReportHelperRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getUserIds(), 1, Integer.MAX_VALUE);
                return (T) excelWriterService.generate(tasks);
            }
        }else if (reportObject.getReportType() == 3) { // if completed tasks report
            if (reportObject.getDetectedReportType() == 0) { // graph
                return (T) taskReportUserHelper(reportObject, "CompletedTaskReport");
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
        }else if(reportObject.getReportType() == 4 || reportObject.getReportType() == 7){
            if (reportObject.getDetectedReportType() == 0) { // graph
                if(reportObject.getReportType() == 4){
                    return (T) taskReportHelper(reportObject, getProjectReportType(reportObject.getStatus()));
                }else if(reportObject.getReportType() == 7){
                    return (T) taskReportHelper(reportObject, "TaskReportFromSource");
                }
            } else if (reportObject.getDetectedReportType() == 1) { // table
                return (T) taskService.addUserToTask(taskService.getTaskReportProjectBasedOnStatus(reportObject.getStatus(), reportObject.getUserIds(), reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getProjectIds(), reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getSAMLart()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                if (reportObject.getStatus() == 1) { // delayed
                    return (T) taskRepository.cDelayedTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getProjectIds());
                }else if (reportObject.getStatus() == 2) { // finished
                    return (T) taskRepository.cFinishedTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getProjectIds());
                }else if (reportObject.getStatus() == 3) { // inProgress
                    return (T) taskRepository.cInProgressTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getProjectIds());
                }else { // all
                    return (T) taskRepository.cFinishedAndDelayedTaskReportProject(reportObject.getUserIds(), sessionService.getSession(reportObject.getSAMLart()).getId(), reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getProjectIds());
                }
            } else if (reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks =  taskService.getTaskReportHelperProjectBasedOnStatus(reportObject.getStatus(), reportObject.getUserIds(), reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getProjectIds(), 1, Integer.MAX_VALUE, reportObject.getSAMLart());
                return (T) excelWriterService.generate(tasks);
            }
        }else if (reportObject.getReportType() == 8) { // if completed tasks report
            if (reportObject.getDetectedReportType() == 0) { // graph
                return (T) taskReportHelper(reportObject, "TaskAssignmentReport");
            } else if (reportObject.getDetectedReportType() == 1) { // table
                return (T) taskService.addUserToTask(taskRepository.getTaskAssignmentReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getType(), reportObject.getPageNumber(), reportObject.getPageSize()));
            } else if (reportObject.getDetectedReportType() == 2) { // count
                return (T) taskRepository.cTaskAssignmentReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getType());
            } else if (reportObject.getDetectedReportType() == 3) { // file
                List<TaskReportHelper> tasks = taskService.getTaskAssignmentReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getType(), 1, Integer.MAX_VALUE);
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
            List<Statistics> statisticss = null;
            if(reportObject.getReportType() == 18){
                statisticss = statisticsRepository.getUserRiskStatisticsReport(users[i],reportObject.getStartDate(), reportObject.getEndDate());
            }else if(reportObject.getReportType() == 38) {
                statisticss = statisticsRepository.getUserIssueStatisticsReport(users[i], reportObject.getStartDate(), reportObject.getEndDate());
            }

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

    private HashMap<List, List> riskReportHelper(ReportObject reportObject, String type){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        ArrayList<String> xaxis = new ArrayList<>();
        List<Risk> riskList = null;
        long[] users = reportObject.getUsers();
        String ids = "";
        for (int i=0; i< users.length; i++){
            if (i != 0) ids += ",";
            ids += users[i]+"";
        }
        List<Risk> riskList1 = null;
            if (type.equals("delayedRiskReport")){
                    riskList = riskRepository.getDelayedRisks(1, Integer.MAX_VALUE, ids);
                    if(riskList.size() != 0){
                        xaxis = Utils.getYearlyMonthBetweenDates(riskList.get(0).getRiskDate(),riskList.get(riskList.size()-1).getRiskDate());
                        LocalDate startDate = LocalDate.parse(riskList.get(0).getRiskDate().toString().split(" ")[0]);
                        LocalDate endDate = LocalDate.parse(riskList.get(riskList.size()-1).getRiskDate().toString().split(" ")[0]);

                        int months = (int) ChronoUnit.MONTHS.between(
                                startDate.withDayOfMonth(1),
                                endDate.withDayOfMonth(endDate.lengthOfMonth()))+1;

                        int[] riskDate = new int[months];

                    for(int i=0; i< users.length; i++) {
                        riskList1 = riskRepository.getDelayedRisks(1, Integer.MAX_VALUE, users[i] + "");
                        if(riskList1.size() != 0){
                            for (Risk risk : riskList1) {
                                LocalDate date = LocalDate.parse(risk.getRiskDate().toString().split(" ")[0]);
                                int indexOfRisk = (int) ChronoUnit.MONTHS.between(
                                        startDate.withDayOfMonth(1),
                                        date.withDayOfMonth(date.lengthOfMonth()));
                                riskDate[indexOfRisk] += 1;
                            }
                            GraphDataHelper graphDataHelper = new GraphDataHelper();
                            graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                            graphDataHelper.setData(riskDate);
                            graphDataHelpers.add(graphDataHelper);
                            riskDate = new int[months];
                        }
                    }
                }
            } else if (type.equals("activityProjectRiskRelated")){

                riskList = riskRepository.getRiskRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), ids );
                if(riskList.size() != 0) {
                    xaxis = Utils.getYearlyMonthBetweenDates(riskList.get(0).getRiskDate(), riskList.get(riskList.size() - 1).getRiskDate());
                    LocalDate startDate = LocalDate.parse(riskList.get(0).getRiskDate().toString().split(" ")[0]);
                    LocalDate endDate = LocalDate.parse(riskList.get(riskList.size() - 1).getRiskDate().toString().split(" ")[0]);

                    int months = (int) ChronoUnit.MONTHS.between(
                            startDate.withDayOfMonth(1),
                            endDate.withDayOfMonth(endDate.lengthOfMonth())) + 1;

                    int[] riskDate = new int[months];
                    for (int i = 0; i < users.length; i++) {
                        riskList1 = riskRepository.getRiskRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), users[i] + "");
                        for (Risk risk : riskList1) {
                            LocalDate date = LocalDate.parse(risk.getRiskDate().toString().split(" ")[0]);
                            int indexOfRisk = (int) ChronoUnit.MONTHS.between(
                                    startDate.withDayOfMonth(1),
                                    date.withDayOfMonth(date.lengthOfMonth()));
                            riskDate[indexOfRisk] += 1;
                        }
                        GraphDataHelper graphDataHelper = new GraphDataHelper();
                        graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                        graphDataHelper.setData(riskDate);
                        graphDataHelpers.add(graphDataHelper);
                        riskDate = new int[months];
                    }
                }
            } else if (type.equals("userProductivityRiskReport")){

                riskList = riskRepository.getClosedRisksReport(1, Integer.MAX_VALUE, ids ,reportObject.getStartDate(), reportObject.getEndDate());
                if(riskList.size() != 0) {
                    xaxis = Utils.getYearlyMonthBetweenDates(riskList.get(0).getRiskDate(), riskList.get(riskList.size() - 1).getRiskDate());
                    LocalDate startDate = LocalDate.parse(riskList.get(0).getRiskDate().toString().split(" ")[0]);
                    LocalDate endDate = LocalDate.parse(riskList.get(riskList.size() - 1).getRiskDate().toString().split(" ")[0]);

                    int months = (int) ChronoUnit.MONTHS.between(
                            startDate.withDayOfMonth(1),
                            endDate.withDayOfMonth(endDate.lengthOfMonth())) + 1;

                    int[] riskDate = new int[months];
                    for (int i = 0; i < users.length; i++) {
                        riskList1 = riskRepository.getClosedRisksReport(1, Integer.MAX_VALUE, users[i] +"" ,reportObject.getStartDate(), reportObject.getEndDate());
                        for (Risk risk : riskList1) {
                            LocalDate date = LocalDate.parse(risk.getRiskDate().toString().split(" ")[0]);
                            int indexOfRisk = (int) ChronoUnit.MONTHS.between(
                                    startDate.withDayOfMonth(1),
                                    date.withDayOfMonth(date.lengthOfMonth()));
                            riskDate[indexOfRisk] += 1;
                        }
                        GraphDataHelper graphDataHelper = new GraphDataHelper();
                        graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                        graphDataHelper.setData(riskDate);
                        graphDataHelpers.add(graphDataHelper);
                        riskDate = new int[months];
                    }
                }
            }

        HashMap<List, List> result = new HashMap<>();
        result.put(graphDataHelpers, xaxis);
        return result;
       // return graphDataHelpers;

    }

    private HashMap<List, List> issueReportHelper(ReportObject reportObject, String type){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        ArrayList<String> xaxis = new ArrayList<>();
        List<Issue> issueList = null;

        long[] users = reportObject.getUsers();
        String ids = "";
        for (int i=0; i< users.length; i++){
            if (i != 0) ids += ",";
            ids += users[i]+"";
        }


        List<Issue> issueList1 = null;
        if (type.equals("delayedIssueReport")){

            issueList = issueRepository.getDelayedIssues(1, Integer.MAX_VALUE, ids);
            if(issueList.size() != 0){
                xaxis = Utils.getYearlyMonthBetweenDates(issueList.get(0).getIssueStartDate(),issueList.get(issueList.size()-1).getIssueStartDate());
                LocalDate startDate = LocalDate.parse(issueList.get(0).getIssueStartDate().toString().split(" ")[0]);
                LocalDate endDate = LocalDate.parse(issueList.get(issueList.size()-1).getIssueStartDate().toString().split(" ")[0]);

                int months = (int) ChronoUnit.MONTHS.between(
                        startDate.withDayOfMonth(1),
                        endDate.withDayOfMonth(endDate.lengthOfMonth())) +1;

                int[] issueDate = new int[months];

                for(int i=0; i< users.length; i++) {
                    issueList1 = issueRepository.getDelayedIssues(1, Integer.MAX_VALUE, users[i] + "");
                        for (Issue issue : issueList1) {
                            LocalDate date = LocalDate.parse(issue.getIssueStartDate().toString().split(" ")[0]);
                            int indexOfIssue = (int) ChronoUnit.MONTHS.between(
                                    startDate.withDayOfMonth(1),
                                    date.withDayOfMonth(date.lengthOfMonth()));
                            issueDate[indexOfIssue] += 1;
                        }
                    GraphDataHelper graphDataHelper = new GraphDataHelper();
                    graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                    graphDataHelper.setData(issueDate);
                    graphDataHelpers.add(graphDataHelper);
                    issueDate = new int[months];
                }
            }
        } else if (type.equals("activityProjectIssueRelated")) {

//                issueList = issueRepository.getIssueRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), users[i] + "");
            issueList = issueRepository.getIssueRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), ids);
            if(issueList.size() != 0){
                xaxis = Utils.getYearlyMonthBetweenDates(issueList.get(0).getIssueStartDate(),issueList.get(issueList.size()-1).getIssueStartDate());
                LocalDate startDate = LocalDate.parse(issueList.get(0).getIssueStartDate().toString().split(" ")[0]);
                LocalDate endDate = LocalDate.parse(issueList.get(issueList.size()-1).getIssueStartDate().toString().split(" ")[0]);

                int months = (int) ChronoUnit.MONTHS.between(
                        startDate.withDayOfMonth(1),
                        endDate.withDayOfMonth(endDate.lengthOfMonth())) +1;

                int[] issueDate = new int[months];

                for(int i=0; i< users.length; i++) {
                issueList1 = issueRepository.getIssueRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), users[i] + "");
                    for (Issue issue : issueList1) {
                        LocalDate date = LocalDate.parse(issue.getIssueStartDate().toString().split(" ")[0]);
                        int indexOfIssue = (int) ChronoUnit.MONTHS.between(
                                startDate.withDayOfMonth(1),
                                date.withDayOfMonth(date.lengthOfMonth()));
                        issueDate[indexOfIssue] += 1;
                    }
                    GraphDataHelper graphDataHelper = new GraphDataHelper();
                    graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                    graphDataHelper.setData(issueDate);
                    graphDataHelpers.add(graphDataHelper);
                    issueDate = new int[months];
                }
            }
        } else if (type.equals("userProductivityRiskReport")){
            issueList = issueRepository.getClosedIssuesReport(1, Integer.MAX_VALUE, ids );
            if(issueList.size() != 0){
                xaxis = Utils.getYearlyMonthBetweenDates(issueList.get(0).getIssueStartDate(),issueList.get(issueList.size()-1).getIssueStartDate());
                LocalDate startDate = LocalDate.parse(issueList.get(0).getIssueStartDate().toString().split(" ")[0]);
                LocalDate endDate = LocalDate.parse(issueList.get(issueList.size()-1).getIssueStartDate().toString().split(" ")[0]);

                int months = (int) ChronoUnit.MONTHS.between(
                        startDate.withDayOfMonth(1),
                        endDate.withDayOfMonth(endDate.lengthOfMonth())) +1;

                int[] issueDate = new int[months];

                for(int i=0; i< users.length; i++) {
                issueList1 = issueRepository.getClosedIssuesReport(1, Integer.MAX_VALUE, users[i] +"" );
                    for (Issue issue : issueList1) {
                        LocalDate date = LocalDate.parse(issue.getIssueStartDate().toString().split(" ")[0]);
                        int indexOfIssue = (int) ChronoUnit.MONTHS.between(
                                startDate.withDayOfMonth(1),
                                date.withDayOfMonth(date.lengthOfMonth()));
                        issueDate[indexOfIssue] += 1;
                    }
                    GraphDataHelper graphDataHelper = new GraphDataHelper();
                    graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
                    graphDataHelper.setData(issueDate);
                    graphDataHelpers.add(graphDataHelper);
                    issueDate = new int[months];
                }
            }
        }else if(type.equals("issuesByPrioirtyThenPrecedence")){

            long[] issues = reportObject.getIssues();
            int arraySize = issues.length;
            int[] taskCount = new int[0];
            int[] issueImportance = new int[0];
            issueList1 = issueRepository.getIssuesByPriorityThenPrecedence(ids, 1, Integer.MAX_VALUE);
            issueImportance = new int[issueList1.size()];
            if (issueList1.size() != 0)
                for(int j=0; j< issueList1.size(); j++) {
                    int importance = issueList1.get(j).getEffect() * issueList1.get(j).getProbability();
                    issueImportance[j] += importance;
                    String projectName = issueRepository.findById(issueList1.get(j).getId()).get().getIssueName();
                    xaxis.add(projectName);

                }
//                for (Issue issue : issueList1) {
//                    int importance = issue.getEffect() * issue.getProbability();
//                        issueImportance [] += importance;
//
//                    String projectName = issueRepository.findById(issue.getId()).get().getIssueName();
//                    xaxis.add(projectName);
//                }

                   // String projectName = issueRepository.findById(Long.parseLong(reportObject.getIssueIds().split(",")[i])).get().getIssueName();
                    //xaxis.add(projectName);
//                arraySize = reportObject.getIssueIds().split(",").length;
//                taskCount = new int[arraySize];
//                issueImportance = new int[arraySize];
//
//                for (int i = 0; i < arraySize; i++) {
//                    for(int j=0; j< users.length; j++) {
//                        issueList1 = issueRepository.getIssuesByPriorityThenPrecedence(users[j] + "", reportObject.getPageNumber(), reportObject.getPageSize());
//                        if (issueList1.size() == 0) continue;
//                        for (Issue issue : issueList1) {
//
//                                issueImportance[i] += 1;
//
//                        }
//                    }
//                    String projectName = issueRepository.findById(Long.parseLong(reportObject.getIssueIds().split(",")[i])).get().getIssueName();
//                    xaxis.add(projectName);
//                }
//
//
            GraphDataHelper graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مخاطر");
            graphDataHelper.setData(issueImportance);
            graphDataHelpers.add(graphDataHelper);

        }

//        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
//        long[] users = reportObject.getUsers();
//
//        for(int i=0; i< users.length; i++){
//            List<Issue> issueList = null;
//            if (type.equals("delayedIssueReport")) {
//                issueList = issueRepository.getDelayedIssues(1, Integer.MAX_VALUE, users[i] + "");
//                if (issueList.size() == 0) continue;
//
//                int[] issueDate = new int[12];
//                for (Issue issue : issueList) {
//                    int num = Utils.getMonthFromDate(issue.getIssueEndDate());
//                    issueDate[num] += 1;
//                }
//                GraphDataHelper graphDataHelper = new GraphDataHelper();
//                graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
//                graphDataHelper.setData(issueDate);
//                graphDataHelpers.add(graphDataHelper);
//
//            } else if (type.equals("activityProjectIssueRelated")) {
//                issueList = issueRepository.getIssueRelatedToProjectReport(1, Integer.MAX_VALUE, reportObject.getProjectIds(), users[i] + "");
//                if (issueList.size() == 0) continue;
//
//                int[] issueDate = new int[12];
//                for (Issue issue : issueList) {
//                    int num = Utils.getMonthFromDate(issue.getIssueEndDate());
//                    issueDate[num] += 1;
//                }
//                GraphDataHelper graphDataHelper = new GraphDataHelper();
//                graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
//                graphDataHelper.setData(issueDate);
//                graphDataHelpers.add(graphDataHelper);
//
//            } else if (type.equals("userProductivityRiskReport")){
//                issueList = issueRepository.getClosedIssuesReport(1, Integer.MAX_VALUE, users[i] +"" );
//                if (issueList.size() == 0) continue;
//
//                int[] issueDate = new int[12];
//                for (Issue issue : issueList) {
//                    int num = Utils.getMonthFromDate(issue.getIssueEndDate());
//                    issueDate[num] += 1;
//                }
//                GraphDataHelper graphDataHelper = new GraphDataHelper();
//                graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
//                graphDataHelper.setData(issueDate);
//                graphDataHelpers.add(graphDataHelper);


      //  return graphDataHelpers;
        HashMap<List, List> result = new HashMap<>();
        result.put(graphDataHelpers, xaxis);
        return result;
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
                taskList = taskRepository.getDelayedTaskReportProject(";"+users[i]+";", userHelper.getId(), reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            }else if(type == "FinishedTaskReportProject"){
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getFinishedTaskReportProject(";"+users[i]+";", userHelper.getId(), reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            } else if(type == "AllTaskReportProject") {
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getFinishedAndDelayedTaskReportProject(";"+users[i]+";", userHelper.getId(), reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            }else if (type.equals("delayedTaskRiskReport")){
                taskList = taskRepository.getDelayedTaskRiskReport(1, Integer.MAX_VALUE, reportObject.getRiskIds(), users[i] +"" );
            }else if (type.equals("inProgressDelayedClosedTaskRisksReport")){
                taskList = taskRepository.getInProgressDelayedClosedTaskRisksReport(1, Integer.MAX_VALUE, reportObject.getRiskIds(), users[i] +"" );
            }else if (type.equals("delayedTaskIssueReport")) {
                taskList = taskRepository.getDelayedTaskIssueReport(1, Integer.MAX_VALUE, reportObject.getIssueIds(), users[i] +"" );
            }else if (type.equals("inProgressDelayedClosedTaskIssuesReport")) {
                taskList = taskRepository.getDelayedTaskIssueReport(1, Integer.MAX_VALUE, reportObject.getIssueIds(), users[i] +"" );
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
        }else if (projectStatus == 3) { // inProgress
            return "InProgressTaskReportProject";
        }else { // all
            return "AllTaskReportProject";
        }
    }

    public List<GraphDataHelper> taskReportUserHelper(ReportObject reportObject,String type){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        long[] users = reportObject.getUsers();

        int arraySize = users.length;

        int[] completedTasks = new int[arraySize];
        int[] inProgressTasks = new int[arraySize];
        int[] delayedTasks = new int[arraySize];

        for(int i=0; i< users.length; i++){
            List<Task> taskList = null;
            String user = ";"+  users[i] +";";

            if(type == "DelayedTaskReport"){
                taskList = taskRepository.getDelayedTasks(user, 1, Integer.MAX_VALUE);

                if (taskList.size() == 0) continue;

                delayedTasks[i] = taskList.size();
//                for (Task task : taskList) {
//                    if(task.getStatus() == 1){
//                        delayedTasks[i] += 1;
//                    }else{
//                        completedTasks[i] += 1;
//                    }
//
//                }
            }else if(type == "UserProductivityReport"){
                taskList = taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(),user, 1, Integer.MAX_VALUE);

                if (taskList.size() == 0) continue;

                for (Task task : taskList) {
                    if(task.getStatus() == 3 || task.getStatus() == 12 || task.getProgress() == 100){
                        completedTasks[i] += 1;
                    }else if (task.getDueDate().before(new Date())) {
                        delayedTasks[i] += 1;
                    } else {
                        inProgressTasks[i] += 1;
                    }
                }
            }else if(type == "CompletedTaskReport"){
                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
                taskList = taskRepository.getCompletedTaskReport(user, userHelper.getId(), reportObject.getStartDate(), reportObject.getEndDate(), 1, Integer.MAX_VALUE);

                if (taskList.size() == 0) continue;

                for (Task task : taskList) {
                        completedTasks[i] += 1;
                }
            }else if(type == "delayedTaskRiskReport"){

                taskList= taskRepository.getDelayedTaskRiskReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds(),  users[i] +"");

                //taskList = taskRepository.getDelayedTasks(user, 1, Integer.MAX_VALUE);

                if (taskList.size() == 0) continue;

                for (Task task : taskList) {
                    if(task.getStatus() == 1){
                        delayedTasks[i] += 1;
                    }else{
                        completedTasks[i] += 1;
                    }
                }
            }else if(type == "delayedTaskIssueReport"){
                taskList = taskRepository.getDelayedTaskIssueReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getIssueIds(), users[i] +"");
                for (Task task : taskList) {
                    if(task.getStatus() == 1){
                        delayedTasks[i] += 1;
                    }else{
                        completedTasks[i] += 1;
                    }
                }
            }
        }

        GraphDataHelper graphDataHelper = null;
        if(type == "UserProductivityReport" || type == "CompletedTaskReport") {
            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام منجزة");
            graphDataHelper.setData(completedTasks);
            graphDataHelpers.add(graphDataHelper);
        }

        if(type == "UserProductivityReport") {
            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام تحت التنفيذ");
            graphDataHelper.setData(inProgressTasks);
            graphDataHelpers.add(graphDataHelper);
        }

        if(type == "DelayedTaskReport" || type == "UserProductivityReport"){
            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
            graphDataHelper.setData(delayedTasks);
            graphDataHelpers.add(graphDataHelper);
        }

        if(type == "delayedTaskRiskReport" || type == "delayedTaskIssueReport"){
            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
            graphDataHelper.setData(delayedTasks);
            graphDataHelpers.add(graphDataHelper);
            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام منجزة");
            graphDataHelper.setData(completedTasks);
            graphDataHelpers.add(graphDataHelper);
        }

        return graphDataHelpers;
    }

    public HashMap<List, List> taskReportHelper(ReportObject reportObject,String type){
        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
        List<Task> taskList = null;
        GraphDataHelper graphDataHelper = null;
        ArrayList<String> xaxis = new ArrayList<>();
        if(type == "DelayedTaskReportProject"){
            taskList = taskRepository.getDelayedTaskReportProject("", 0, reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            int[] taskCount = new int[1];
            taskCount[0] = taskList.size();

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
            graphDataHelper.setData(taskCount);
            graphDataHelpers.add(graphDataHelper);

            xaxis.add("مهام متأخرة");

        }else if(type == "FinishedTaskReportProject") {
            taskList = taskRepository.getFinishedTaskReportProject("", 0, reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            int[] taskCount = new int[1];
            taskCount[0] = taskList.size();

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام منجزة");
            graphDataHelper.setData(taskCount);
            graphDataHelpers.add(graphDataHelper);

            xaxis.add("مهام منجزة");

        }else if(type == "InProgressTaskReportProject") {
            taskList = taskRepository.getInProgressTaskReportProject("", 0, reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            int[] taskCount = new int[1];
            taskCount[0] = taskList.size();

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام تحت التنفيذ");
            graphDataHelper.setData(taskCount);
            graphDataHelpers.add(graphDataHelper);

            xaxis.add("مهام تحت التنفيذ");

        } else if(type == "AllTaskReportProject"){
            taskList = taskRepository.getFinishedAndDelayedTaskReportProject("", 0, reportObject.getStartDate(), reportObject.getEndDate(),reportObject.getProjectIds(), 1, Integer.MAX_VALUE );
            int[] taskCount = new int[3];
            for(Task task : taskList){
                if(task.getStatus() == 3 || task.getStatus() == 12){
                    taskCount[0] += 1;
                }else if(task.getDueDate().before(new Date())){
                    taskCount[2] += 1;
                }else {
                    taskCount[1] += 1;
                }
            }

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام");
            graphDataHelper.setData(taskCount);
            graphDataHelpers.add(graphDataHelper);

            xaxis.add("مهام منجزة");
            xaxis.add("مهام تحت التنفيذ");
            xaxis.add("مهام متأخرة");

//        } else if (type == "TaskReportFromSource"){
//            long[] projects = reportObject.getProjects();
//            int arraySize = projects.length;
//            if(arraySize == 0){
//                String[] projectsIds = reportObject.getProjectIds().split(",");
//                projects = new long[projectsIds.length];
//                for(int i = 0; i<projectsIds.length ; i++){
//                    projects[i] = Long.parseLong(projectsIds[i]);
//                }
//                arraySize = projects.length;
//            }
//
//            ArrayList<Integer> completedTasksAL = new ArrayList<>();
//            ArrayList<Integer> delayedTasksAL = new ArrayList<>();
//            ArrayList<Integer> inProgressTasksAL = new ArrayList<>();
//            int[] completedTasks = new int[arraySize];
//            int[] delayedTasks = new int[arraySize];
//            int[] inProgressTasks = new int[arraySize];
//            int counter = 0;
//            for(int i = 0; i < arraySize ; i++){
//                String project = projects[i] + "";
//                taskList = taskRepository.getFinishedAndDelayedTaskReportProject("", 0, reportObject.getStartDate(), reportObject.getEndDate(), project, 1, Integer.MAX_VALUE );
//
//                if(taskList.size() <= 0){
//                    continue;
//                }
//
//                completedTasksAL.add(0);
//                delayedTasksAL.add(0);
//                inProgressTasksAL.add(0);
//
//                for (Task task : taskList) {
//                    if(task.getStatus() == 3 || task.getStatus() == 12){
//                        completedTasksAL.set(counter,completedTasksAL.get(counter)+1);
////                        completedTasks[i] += 1;
//                    }else if (task.getDueDate().before(new Date())) {
////                        delayedTasks[i] += 1;
//                        delayedTasksAL.set(counter,delayedTasksAL.get(counter)+1);
//                    } else {
//                        inProgressTasksAL.set(counter,inProgressTasksAL.get(counter)+1);
////                        inProgressTasks[i] += 1;
//                    }
//                }
//
//                counter++;
//                String projectName = projectRepository.findById(projects[i]).get().getName();
//                xaxis.add(projectName);
//            }
//
//            completedTasks = new int[completedTasksAL.size()];
//            inProgressTasks = new int[inProgressTasksAL.size()];
//            delayedTasks = new int[delayedTasksAL.size()];
//            for(int j = 0 ; j< completedTasksAL.size(); j++){
//                completedTasks[j] = completedTasksAL.get(j);
//                delayedTasks[j] = delayedTasksAL.get(j);
//                inProgressTasks[j] = inProgressTasksAL.get(j);
//            }
//
//            graphDataHelper = new GraphDataHelper();
//            graphDataHelper.setName("مهام منجزة");
//            graphDataHelper.setData(completedTasks);
//            graphDataHelpers.add(graphDataHelper);
//
//            graphDataHelper = new GraphDataHelper();
//            graphDataHelper.setName("مهام تحت التنفيذ");
//            graphDataHelper.setData(inProgressTasks);
//            graphDataHelpers.add(graphDataHelper);
//
//            graphDataHelper = new GraphDataHelper();
//            graphDataHelper.setName("مهام متأخرة");
//            graphDataHelper.setData(delayedTasks);
//            graphDataHelpers.add(graphDataHelper);
        } else if (type == "TaskAssignmentReport"){
            taskList = taskRepository.getTaskAssignmentReport(reportObject.getStartDate(), reportObject.getEndDate(), reportObject.getType(), 1, Integer.MAX_VALUE );
            if(reportObject.getType() == -1){
                int[] countArray = new int[5];
                for (Task task : taskList){
                    int assignmentType = task.getTypeOfAssignment();
                    if(assignmentType > 0 && assignmentType < 6){
                        countArray[assignmentType-1] += 1;
                    }
                }

                graphDataHelper = new GraphDataHelper();
                graphDataHelper.setName("مهام");
                graphDataHelper.setData(countArray);
                graphDataHelpers.add(graphDataHelper);

                xaxis.add("طلب تقرير/دراسة");
                xaxis.add("طلب معلومات");
                xaxis.add("طلب ملخص");
                xaxis.add("طلب ملاحظات/مرئيات");
                xaxis.add("أخرى");
            }else{
                int[] countArray = new int[1];
                countArray[0] = taskList.size();

                graphDataHelper = new GraphDataHelper();
                graphDataHelper.setName("مهام");
                graphDataHelper.setData(countArray);
                graphDataHelpers.add(graphDataHelper);

                String typeName = "";

                switch (reportObject.getType()){
                    case 1:
                        typeName = "طلب تقرير/دراسة";
                        break;
                    case 2:
                        typeName = "طلب معلومات";
                        break;
                    case 3:
                        typeName = "طلب ملخص";
                        break;
                    case 4:
                        typeName = "طلب ملاحظات/مرئيات";
                        break;
                    case 5:
                        typeName = "أخرى";
                        break;
                    default:
                        typeName = "طلب تقرير/دراسة";
                }

                xaxis.add(typeName);
            }
        }else if(type == "AllTaskReportRisk"){
            long[] users = reportObject.getUsers();
            long[] risks = reportObject.getRisks();
            int arraySize = risks.length;
            int[] taskCount = new int[0];
            int[] completedTasks = new int[0];
            int[] delayedTasks = new int[0];
            int[] inProgressTasks = new int[0];

            if(arraySize == 0) {
                arraySize = reportObject.getRiskIds().split(",").length;
                taskCount = new int[arraySize];
                completedTasks = new int[arraySize];
                delayedTasks = new int[arraySize];
                inProgressTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                    taskList = taskRepository.getInProgressDelayedClosedTaskRisksReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds().split(",")[i], users[j] + "");
                    if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                            if (task.getStatus() == 3 || task.getStatus() == 12) {
                                completedTasks[i] += 1;
                            } else if (task.getDueDate().before(new Date())) {
                           // } else if (task.getDueDate().before(new Date()) && task.getStatus() != 11) {
                                delayedTasks[i] += 1;
                            } else {
                                inProgressTasks[i] += 1;
                            }
                        }

                    }
                    String projectName = riskRepository.findById(Long.parseLong(reportObject.getRiskIds().split(",")[i])).get().getRiskName();
                    xaxis.add(projectName);
                }
            }else {
                 taskCount = new int[arraySize];
                 completedTasks = new int[arraySize];
                 delayedTasks = new int[arraySize];
                 inProgressTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                        String risk = risks[i]+"";
                        taskList = taskRepository.getInProgressDelayedClosedTaskRisksReport(reportObject.getPageNumber(), reportObject.getPageSize(), risk, users[j] + "");
                        if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                            if (task.getStatus() == 3 || task.getStatus() == 12) {
                                completedTasks[i] += 1;
                            } else if (task.getDueDate().before(new Date())) {
                                delayedTasks[i] += 1;
                            } else {
                                inProgressTasks[i] += 1;
                            }
                        }
                    }
                    String projectName = riskRepository.findById(risks[i]).get().getRiskName();
                    xaxis.add(projectName);
                }
            }

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام منجزة");
            graphDataHelper.setData(completedTasks);
            graphDataHelpers.add(graphDataHelper);

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام تحت التنفيذ");
            graphDataHelper.setData(inProgressTasks);
            graphDataHelpers.add(graphDataHelper);

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
            graphDataHelper.setData(delayedTasks);
            graphDataHelpers.add(graphDataHelper);

        }else if(type == "AllTaskReportIssue"){

            long[] users = reportObject.getUsers();
            long[] issues = reportObject.getIssues();
            int arraySize = issues.length;
            int[] taskCount = new int[0];
            int[] completedTasks = new int[0];
            int[] delayedTasks = new int[0];
            int[] inProgressTasks = new int[0];

            if(arraySize == 0) {
                arraySize = reportObject.getIssueIds().split(",").length;
                taskCount = new int[arraySize];
                completedTasks = new int[arraySize];
                delayedTasks = new int[arraySize];
                inProgressTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                        taskList = taskRepository.getInProgressDelayedClosedTaskIssuesReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getIssueIds().split(",")[i], users[j] + "");
                        if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                            if (task.getStatus() == 3 || task.getStatus() == 12) {
                                completedTasks[i] += 1;
                            } else if (task.getDueDate().before(new Date())) {
                                delayedTasks[i] += 1;
                            } else {
                                inProgressTasks[i] += 1;
                            }
                        }
                    }
                    String projectName = issueRepository.findById(Long.parseLong(reportObject.getIssueIds().split(",")[i])).get().getIssueName();
                    xaxis.add(projectName);
                }
            }else {
                taskCount = new int[arraySize];
                completedTasks = new int[arraySize];
                delayedTasks = new int[arraySize];
                inProgressTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                        String issue = issues[i]+"";
                        taskList = taskRepository.getInProgressDelayedClosedTaskIssuesReport(reportObject.getPageNumber(), reportObject.getPageSize(), issue, users[j] + "");
                        if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                            if (task.getStatus() == 3 || task.getStatus() == 12) {
                                completedTasks[i] += 1;
                            } else if (task.getDueDate().before(new Date())) {
                                delayedTasks[i] += 1;
                            } else {
                                inProgressTasks[i] += 1;
                            }
                        }
                    }
                    String projectName = issueRepository.findById(issues[i]).get().getIssueName();
                    xaxis.add(projectName);
                }
            }

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام منجزة");
            graphDataHelper.setData(completedTasks);
            graphDataHelpers.add(graphDataHelper);

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام تحت التنفيذ");
            graphDataHelper.setData(inProgressTasks);
            graphDataHelpers.add(graphDataHelper);

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
            graphDataHelper.setData(delayedTasks);
            graphDataHelpers.add(graphDataHelper);

        }else if(type == "delayedTaskRiskReport"){
            long[] users = reportObject.getUsers();
            long[] risks = reportObject.getRisks();
            int arraySize = risks.length;
            int[] taskCount = new int[0];
            int[] delayedTasks = new int[0];
            int[] delayedTasksFinal = new int[0];
            String riskIdsWithTasks = "";

            if(arraySize == 0) {
                arraySize = reportObject.getRiskIds().split(",").length;
                taskCount = new int[arraySize];
                //int[] tempDelayedTasks = new int[arraySize];
                delayedTasks = new int[arraySize];
                for (int i = 0; i < arraySize; i++) {
                    for (int j = 0; j < users.length; j++) {
                        taskList = taskRepository.getDelayedTaskRiskReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getRiskIds().split(",")[i], users[j] + "");
                        if (taskList.size() == 0) continue;

                        for (Task task : taskList) {
                            delayedTasks[i] += 1;
                        }
                        riskIdsWithTasks += reportObject.getRiskIds().split(",")[i] + ",";
                    }

                    String riskName = riskRepository.findById(Long.parseLong(reportObject.getRiskIds().split(",")[i])).get().getRiskName();
                    xaxis.add(riskName);
                }
//                String[] riskIds = riskIdsWithTasks.split(",");
////                delayedTasksFinal = (int[]) resizeArray(delayedTasks, riskIds.length);
//                delayedTasks = new int[riskIds.length];
//                int delayedTasksIndex = 0;
//                for(int i = 0; i < tempDelayedTasks.length ; i++){
//                    if(tempDelayedTasks[i] == 0) continue;
//                    delayedTasks[delayedTasksIndex] = tempDelayedTasks[i];
//                    delayedTasksIndex++;
//                }
//                for (int i = 0; i< riskIds.length; i++){
//                    if(riskIds[i] == "") continue;
//                    String riskName = riskRepository.findById(Long.parseLong(riskIds[i])).get().getRiskName();
//                    xaxis.add(riskName);
//                }
            }else {
                taskCount = new int[arraySize];
                delayedTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                        String risk = risks[i]+"";
                        taskList= taskRepository.getDelayedTaskRiskReport(reportObject.getPageNumber(), reportObject.getPageSize(), risk,  users[j] +"");
                        if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                                if (task.getDueDate().before(new Date())) {
                                delayedTasks[i] += 1;
                            }
                        }
                    }
                    String riskName = riskRepository.findById(risks[i]).get().getRiskName();
                    xaxis.add(riskName);

                }
            }

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
//            graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
            graphDataHelper.setData(delayedTasks);
            graphDataHelpers.add(graphDataHelper);

        }else if(type == "delayedTaskIssueReport"){
            long[] users = reportObject.getUsers();
            long[] issues = reportObject.getIssues();
            int arraySize = issues.length;
            int[] taskCount = new int[0];
            int[] delayedTasks = new int[0];

            String issueIdswithTasks = "";
            if(arraySize == 0) {
                arraySize = reportObject.getIssueIds().split(",").length;
                taskCount = new int[arraySize];
                delayedTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                        taskList= taskRepository.getDelayedTaskIssueReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getIssueIds().split(",")[i], users[j] + "");
                        if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                            delayedTasks[i] += 1;
                        }
                    }
                    String projectName = issueRepository.findById(Long.parseLong(reportObject.getIssueIds().split(",")[i])).get().getIssueName();
                    xaxis.add(projectName);
                }

//            if(arraySize == 0) {
//                arraySize = reportObject.getIssueIds().split(",").length;
//                taskCount = new int[arraySize];
//               // int[] tempDelayedTasks = new int[arraySize];
//                 delayedTasks = new int[arraySize];
//                for (int i = 0; i < arraySize; i++) {
//                    for(int j=0; j< users.length; j++) {
//                        taskList= taskRepository.getDelayedTaskIssueReport(reportObject.getPageNumber(), reportObject.getPageSize(), reportObject.getIssueIds().split(",")[i], users[j] + "");
//                        if (taskList.size() == 0) continue;
//
//                        for (Task task : taskList) {
//                            delayedTasks[i] += 1;
//                        }
//                        issueIdswithTasks += reportObject.getIssueIds().split(",")[i]+",";
//                    }
//                }
            }else {
                taskCount = new int[arraySize];
                delayedTasks = new int[arraySize];

                for (int i = 0; i < arraySize; i++) {
                    for(int j=0; j< users.length; j++) {
                        String issue = issues[i]+"";
                        taskList= taskRepository.getDelayedTaskIssueReport(reportObject.getPageNumber(), reportObject.getPageSize(), issue,  users[j] +"");
                        if (taskList.size() == 0) continue;
                        for (Task task : taskList) {
                            if (task.getDueDate().before(new Date())) {
                                delayedTasks[i] += 1;
                            }
                        }
                    }
                    String projectName = issueRepository.findById(issues[i]).get().getIssueName();
                    xaxis.add(projectName);
                }
            }

            graphDataHelper = new GraphDataHelper();
            graphDataHelper.setName("مهام متأخرة");
            graphDataHelper.setData(delayedTasks);
            graphDataHelpers.add(graphDataHelper);
        }

        HashMap<List, List> result = new HashMap<>();
        result.put(graphDataHelpers, xaxis);
        return result;
    }
//    public List<GraphDataHelper> reportDateHelper( ReportObject reportObject,  String type ){
//        List<GraphDataHelper> graphDataHelpers = new ArrayList<>();
//        long days = Utils.differenceBetweenTwoDates(reportObject.getStartDate(), reportObject.getEndDate());
//        long[] users = reportObject.getUsers();
//
//        int arraySize = (days >= 0 && days <= 8)? 7 : (days > 8 && days <= 31)? 31 : 12;
//
//        for(int i=0; i< users.length; i++){
//            List<Task> taskList = null;
//            String user = ";"+  users[i] +";";
//            if(type == "UserProductivityReport"){
//                taskList = taskRepository.getUserProductivityReport(reportObject.getStartDate(), reportObject.getEndDate(),user, 1, Integer.MAX_VALUE);
//            }else
//            if(type == "CompletedTaskReport"){
//                UserHelper userHelper = sessionService.getSession(reportObject.getSAMLart());
//                taskList = taskRepository.getCompletedTaskReport(user, userHelper.getId(), reportObject.getStartDate(), reportObject.getEndDate(), 1, Integer.MAX_VALUE);
//            }
//
//            if (taskList.size() == 0) continue;
//
//            int[] taskDate = new int[arraySize];
//            for (Task task : taskList) {
//                int num = 0;
//                if (arraySize == 7){
//                    num = Utils.getDayNameFromDate(task.getDueDate());
//                }else if (arraySize == 31){
//                    num = Utils.getDayFromDate(task.getDueDate());
//                }else if (arraySize == 12){
//                    num = Utils.getMonthFromDate(task.getDueDate());
//                }
//                taskDate[num] += 1;
//            }
//            GraphDataHelper graphDataHelper = new GraphDataHelper();
//            graphDataHelper.setName(userRepository.findById(users[i]).get().getDisplayName());
//            graphDataHelper.setData(taskDate);
//            graphDataHelpers.add(graphDataHelper);
//        }
//        return graphDataHelpers;
//    }
}
