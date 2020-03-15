package com.mod.rest.service;

import com.mod.rest.model.Issue;
import com.mod.rest.model.UserHelper;
import com.mod.rest.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by karim.omaya on 12/26/2019.
 */
@Service
public class IssueService {
    @Autowired
    SessionService sessionService;
    @Autowired
    IssueRepository issueRepository;

    public List<Issue> getAllIssuesByUser(String SAMLart){
        UserHelper userHelper = sessionService.getSession(SAMLart);
        return issueRepository.getIssueByName(1, Integer.MAX_VALUE, "", userHelper.getId());
    }

    public String getIssuesByUser(String SAMLart){
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Issue> issues = issueRepository.getIssueByName(1, Integer.MAX_VALUE, "", userHelper.getId());
        String issueIds = "";
        for (Issue issue: issues){
            if(!issueIds.equals("")) issueIds += ",";
            issueIds += issue.getId();
        }
        return issueIds;
    }
}
