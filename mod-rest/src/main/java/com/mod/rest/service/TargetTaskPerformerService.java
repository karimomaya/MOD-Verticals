package com.mod.rest.service;

import com.mod.rest.model.Task;
import com.mod.rest.model.User;
import com.mod.rest.model.UserProductivitySP;
import com.mod.rest.system.Pagination;
import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karim.omaya on 11/1/2019.
 */

public class TargetTaskPerformerService {
/*
    private HibernateSession hibernateSession = null;

    public TargetTaskPerformerService(){
        hibernateSession = HibernateSession.getInstance();
    }

    public int[] getDelayedTasks(long userId){
        Pagination<List<Task>> tasks = executeDelayedTaskTable(userId, 0, Integer.MAX_VALUE);

        int[] taskDate = new int[13];
        if (tasks.getData() == null) return taskDate;


        for(Task task : tasks.getData()){
            int month = Utils.getMonthFromDate(task.getDueDate());
            System.out.println(month);
            int num = taskDate[month]+1;
            taskDate[month] = num;
        }
        return taskDate;
    }

    public Pagination<List<Task>> executeDelayedTaskTable(long userId, int pageNumber, int pageSize){

        Pagination<List<Task>> pagination = new Pagination<List<Task>>();
        Session sessionObj = null;

        try {
            sessionObj = hibernateSession.getHibernateSession().openSession();

            String countQ = "EXEC MOD_TM_SP_GetDelayedTaskCount :UserId";
            Query countQuery = sessionObj.createSQLQuery(countQ)
                    .setLong("UserId", userId);

            List<Integer> countResults = countQuery.list();

            int countResult = countResults.get(0);

            System.out.println(countResults);

            int lastPageNumber = (int) (Math.ceil(countResult / pageSize));
            pagination.setLastPage(lastPageNumber);
            pagination.setTotalPages(countResult);



            Query query = sessionObj.createSQLQuery("EXEC MOD_TM_SP_GetDelayedTask :UserId, :PageNumber, :PageSize")
                    .addEntity(Task.class)
                    .setLong("UserId", userId)
                    .setInteger("PageNumber", pageNumber+1)
                    .setInteger("PageSize", pageSize);

            List<Task> tasks = query.list();

            for (Task task : tasks){
                User userOwner =  (User) sessionObj.get(User.class, new Long(task.getOwner()));
                task.setUserOwner(userOwner);
            }

            pagination.setData(tasks);

        } catch (Exception sqlException){
            sqlException.printStackTrace();
            if(null != sessionObj.getTransaction()) {
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        }
        finally {
            if(sessionObj != null) {
                sessionObj.close();
            }
        }

        return pagination;
    }




    public Pagination<List<UserProductivitySP>> executeUserProductivityReport(long userId, Date startDate, Date endDate, String userIds, int pageNumber, int pageSize){

        Pagination<List<UserProductivitySP>> pagination = new Pagination<List<UserProductivitySP>>();
        Session sessionObj = null;

        try {
            sessionObj = hibernateSession.getHibernateSession().openSession();

            String countQ = "EXEC MOD_TM_SP_UserProductivityReportCount :StartDate, :EndDate, :List";
            Query countQuery = sessionObj.createSQLQuery(countQ)
                    .setDate("StartDate", Utils.convertJavaDateToSQLDate(startDate)).setDate("EndDate", Utils.convertJavaDateToSQLDate(endDate))
                    .setString("List", userIds); // ;123;121;

            List<Integer> countResults = countQuery.list();

            int countResult = countResults.get(0);

            System.out.println(countResults);

            int lastPageNumber = (int) (Math.ceil(countResult / pageSize));
            pagination.setLastPage(lastPageNumber);
            pagination.setTotalPages(countResult);

            Query query = sessionObj.createSQLQuery("EXEC MOD_TM_SP_UserProductivityReport :StartDate, :EndDate, :List, :PageNumber, :PageSize")
                    .addEntity(UserProductivitySP.class)
                    .setDate("StartDate", Utils.convertJavaDateToSQLDate(startDate))
                    .setDate("EndDate", Utils.convertJavaDateToSQLDate(endDate))
                    .setString("List", userIds) // ;123;121;
                    .setInteger("PageNumber", pageNumber + 1)
                    .setInteger("PageSize", pageSize);

            List<UserProductivitySP> tasks = query.list();

            for (UserProductivitySP userProductivitySP : tasks){
                User userOwner =  (User) sessionObj.get(User.class, new Long(userProductivitySP.getOwner()));
                userProductivitySP.setUserOwner(userOwner);
            }

            pagination.setData(tasks);

        } catch (Exception sqlException){
            sqlException.printStackTrace();
            if(null != sessionObj.getTransaction()) {
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        }
        finally {
            if(sessionObj != null) {
                sessionObj.close();
            }
        }

        return pagination;
    }


    public HashMap<Long, ReportResult> executeUserProductivityReport(long userId, Date startDate, Date endDate, String userIds){
        long days = Utils.differenceBetweenTwoDates(startDate, endDate);
        Pagination<List<UserProductivitySP>>  tasks =  executeUserProductivityReport(userId, startDate, endDate, userIds, 0, Integer.MAX_VALUE);
//        int[] taskDate = null;

        HashMap<Long, ReportResult> results = new HashMap<Long, ReportResult>();
        Session sessionObj = hibernateSession.getHibernateSession().openSession();
        try {
            if (days >= 0 && days <= 8) { // weekly report

                for (UserProductivitySP task : tasks.getData()) {
                    int day = Utils.getDayNameFromDate(task.getDueDate());

                    User user = (User) sessionObj.get(User.class, new Long(task.getPerformerId()));
                    ReportResult reportResult = results.get(task.getPerformerId());
                    if (reportResult == null) reportResult = new ReportResult();
                    reportResult.setUser(user);

                    if (reportResult.getData() == null) {
                        reportResult.setData(new int[7]);
                    }
                    int[] taskDate = reportResult.getData();
                    taskDate[day] += 1;
                    reportResult.setData(taskDate);
                    results.put(task.getPerformerId(), reportResult);

                }

            } else if (days > 8 && days <= 31) { // monthly report
                for (UserProductivitySP task : tasks.getData()) {
                    int day = Utils.getDayFromDate(task.getDueDate()) ;
                    User user = (User) sessionObj.get(User.class, new Long(task.getPerformerId()));
                    ReportResult reportResult = results.get(task.getPerformerId());
                    if (reportResult == null) reportResult = new ReportResult();
                    reportResult.setUser(user);

                    if (reportResult.getData() == null) {
                        reportResult.setData(new int[31]);
                    }
                    int[] taskDate = reportResult.getData();
                    taskDate[day] += 1;
                    reportResult.setData(taskDate);
                    results.put(task.getPerformerId(), reportResult);
                }
            } else if (days > 31) { // yearly report
                for (UserProductivitySP task : tasks.getData()) {
                    int month = Utils.getMonthFromDate(task.getDueDate());

                    User user = (User) sessionObj.get(User.class, new Long(task.getPerformerId()));
                    ReportResult reportResult = results.get(task.getPerformerId());
                    if (reportResult == null) reportResult = new ReportResult();
                    reportResult.setUser(user);

                    if (reportResult.getData() == null) {
                        reportResult.setData(new int[13]);
                    }
                    int[] taskDate = reportResult.getData();
                    taskDate[month] += 1;
                    reportResult.setData(taskDate);
                    results.put(task.getPerformerId(), reportResult);
                }

            }
        }catch (Exception sqlException){
            sqlException.printStackTrace();
            if(null != sessionObj.getTransaction()) {
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(sessionObj != null) {
                sessionObj.close();
            }
        }

        return results;

    }

    @Getter
    @Setter
    public class ReportResult{
        User user;
        int[] data;

    }

*/
}
