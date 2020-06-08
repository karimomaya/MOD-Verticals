package com.mod.rest.service;

import com.mod.rest.model.*;
import com.mod.rest.repository.TaskReportHelperRepository;
import com.mod.rest.repository.TaskRepository;
import com.mod.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by karim.omaya on 10/29/2019.
 */
@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskReportHelperRepository taskReportHelperRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionService sessionService;

    public List<Task> addUserToTask(List<Task> tasks){
        for (Task task : tasks){
            Optional<User> userOwner =  userRepository.findById(task.getOwner());
            if (userOwner.isPresent()) task.setUserOwner(userOwner.get());
        }
        return tasks;
    }

    public List<TaskReportHelper> getTaskReportHelperProjectBasedOnStatus(int projectStatus, String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart){
        List<TaskReportHelper> tasks = null;
        if (projectStatus == 1){ // delayed
            tasks = getDelayedTaskReportHelperProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        }else if(projectStatus == 2) { // finished
            tasks = getFinishedTaskReportHelperProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        }else if(projectStatus == 3) { // inProgress
            tasks = getInProgressTaskReportHelperProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        } else { // all
            tasks = getFinishedAndDelayedTaskReportHelperProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        }
        return tasks;
    }

    public List<Task> getTaskReportProjectBasedOnStatus(int projectStatus, String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart){
        List<Task> tasks = null;
        if (projectStatus == 1){ // delayed
            tasks = getDelayedTaskReportProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        }else if(projectStatus == 2) { // finished
            tasks = getFinishedTaskReportProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        }else if(projectStatus == 3) { // inProgress
            tasks = getInProgressTaskReportProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        } else { // all
            tasks = getFinishedAndDelayedTaskReportProject(userIds, startDate, endDate, projectIds, pageNumber, pageSize, SAMLart);
        }
        return tasks;
    }

    public List<Task> getFinishedAndDelayedTaskReportProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getFinishedAndDelayedTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);

        return tasks;
    }

    public List<TaskReportHelper> getFinishedAndDelayedTaskReportHelperProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<TaskReportHelper> tasks = taskReportHelperRepository.getFinishedAndDelayedTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);

        return tasks;
    }

    public List<Task> getFinishedTaskReportProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getFinishedTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);

        return tasks;
    }

    public List<TaskReportHelper> getFinishedTaskReportHelperProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<TaskReportHelper> tasks = taskReportHelperRepository.getFinishedTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);

        return tasks;
    }

    public List<Task> getDelayedTaskReportProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getDelayedTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);
        return tasks;
    }

    public List<TaskReportHelper> getDelayedTaskReportHelperProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<TaskReportHelper> tasks = taskReportHelperRepository.getDelayedTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);
        return tasks;
    }

    public List<Task> getInProgressTaskReportProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getInProgressTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);
        return tasks;
    }

    public List<TaskReportHelper> getInProgressTaskReportHelperProject( String userIds, Date startDate, Date endDate, String projectIds, int pageNumber, int pageSize, String SAMLart) {
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<TaskReportHelper> tasks = taskReportHelperRepository.getInProgressTaskReportProject(userIds, userHelper.getId(), startDate, endDate, projectIds,  pageNumber, pageSize);
        return tasks;
    }

    public List<TaskReportHelper> getTaskAssignmentReport(Date startDate, Date endDate, int assignmentType, int pageNumber, int pageSize) {
        List<TaskReportHelper> tasks = taskReportHelperRepository.getTaskAssignmentReport(startDate, endDate, assignmentType,  pageNumber, pageSize);
        return tasks;
    }
}
