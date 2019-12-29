package com.mod.rest.service;

import com.mod.rest.model.*;
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



    public List<Task> getTaskReportProjectBasedOnStatus(int projectStatus, String userIds, String projectIds, int pageNumber, int pageSize, String SAMLart){
        List<Task> tasks = null;
        if (projectStatus == 1){ // delayed
            tasks = getDelayedTaskReportProject(userIds, projectIds, pageNumber, pageSize, SAMLart);
        }else if(projectStatus == 2) { // finished
            tasks = getFinishedTaskReportProject(userIds, projectIds, pageNumber, pageSize, SAMLart);
        } else { // all
            tasks = getFinishedAndDelayedTaskReportProject(userIds, projectIds, pageNumber, pageSize, SAMLart);
        }
        return tasks;
    }

    public List<Task> getFinishedAndDelayedTaskReportProject( String userIds, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getFinishedAndDelayedTaskReportProject(userIds, userHelper.getId(), projectIds,  pageNumber, pageSize);

        return tasks;
    }

    public List<Task> getFinishedTaskReportProject( String userIds, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getFinishedTaskReportProject(userIds, userHelper.getId(), projectIds,  pageNumber, pageSize);

        return tasks;
    }

    public List<Task> getDelayedTaskReportProject( String userIds, String projectIds, int pageNumber, int pageSize, String SAMLart) {

        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Task> tasks = taskRepository.getDelayedTaskReportProject(userIds, userHelper.getId(), projectIds,  pageNumber, pageSize);

        return tasks;
    }
}
