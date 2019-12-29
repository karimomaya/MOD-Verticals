package com.mod.rest.controller;

import com.mod.rest.repository.TaskRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by karim.omaya on 10/29/2019.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/task")
public class TaskController  {

    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionService sessionService;
    @Autowired
    ProjectService projectService;


}
