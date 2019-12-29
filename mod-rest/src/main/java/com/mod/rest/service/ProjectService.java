package com.mod.rest.service;

import com.mod.rest.model.Project;
import com.mod.rest.model.User;
import com.mod.rest.model.UserHelper;
import com.mod.rest.repository.ProjectRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.system.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by karim.omaya on 11/4/2019.
 */
@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionService sessionService;


    public String getProjectUnderHeadUnit(String SAMLart){
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Project> projects  = projectRepository.getProjectByHeadUnit(userHelper.getUnitId(), 1, Integer.MAX_VALUE, "", 1);
        String projectIds = "";
        for (Project project : projects){
            projectIds += project.getId() + ",";
        }
        return projectIds;
    }

    public List<Project> getProjectsReportTable(long userId, Date startDate, Date endDate, int pageNumber, int pageSize) {

        Optional<Project> p = projectRepository.findById(new Long(1));

        System.out.println(p.get().getName());
        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(startDate);
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(endDate);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Project> projects = projectRepository.getProjects(startDateCal.getTime(), endDateCal.getTime(), userId, pageable);

        for (Project project : projects){

            Optional<User> userOwner =  userRepository.findById(project.getOwner());
            if (userOwner.isPresent()) project.setUserOwner(userOwner.get());
        }



        return projects;
    }

    public HashMap<Integer, ProjectResult> getProjectsReportGraph(long userId, Date startDate, Date endDate){

        long days = Utils.differenceBetweenTwoDates(startDate, endDate);

        HashMap<Integer, ProjectResult> results = new HashMap<Integer, ProjectResult>();

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTime(startDate);
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(endDate);

//            String hql = "from Project project where project.startDate <= :endDate  and project.startDate >= :startDate and (project.createdBy = :userId or project.owner = :userId)";
//            Query query = sessionObj.createQuery(hql)
//                    .setDate("startDate", startDateCal.getTime())
//                    .setDate("endDate", endDateCal.getTime())
//                    .setLong("userId", userId);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        List<Project> projects = projectRepository.getProjects(startDateCal.getTime(), endDateCal.getTime(), userId, pageable);

        for (Project project : projects) { //1: inprogress, 2: done, 3: stopped

            if (days >= 0 && days <= 8) { // weekly report
                results = fillHashMap(results, project, 7);


            } else if (days > 8 && days <= 31) { // monthly report
                results = fillHashMap(results, project, 31);
            } else if (days > 31) { // yearly report
                results = fillHashMap(results, project, 12);
            }


        }

        return results;

    }

    public long countProjectByDate(Date startDate, Date endDate, long userId){
        Long countResults = projectRepository.cProject(startDate, endDate, userId);

        return countResults;
    }


    public HashMap<Integer, ProjectResult> fillHashMap(HashMap<Integer, ProjectResult> results, Project project, int arrayLength){
        ProjectResult projectResult = results.get(project.getStatus());
        if (projectResult == null) projectResult = new ProjectResult();
        int[] projectData = projectResult.getData();
        if(projectData == null) projectData = new int[arrayLength];
        int month = 0;
        if (arrayLength == 7) {
            month = Utils.getDayNameFromDate(project.getStartDate());
        }
        else if(arrayLength == 31){
            month = Utils.getDayFromDate(project.getStartDate());
        }
        else if(arrayLength == 12){
            month = Utils.getMonthFromDate(project.getStartDate());
        }
        projectData[month] += 1;
        projectResult.setData(projectData);
        List<Project> projects = projectResult.getProjects();
        if (projects == null) projects = new ArrayList<Project>();
        projects.add(project);
        projectResult.setProjects(projects);
        results.put(project.getStatus(), projectResult);
        return results;
    }




    @Getter
    @Setter
    public class ProjectResult{
        List<Project> projects;
        int[] data;

    }
}
