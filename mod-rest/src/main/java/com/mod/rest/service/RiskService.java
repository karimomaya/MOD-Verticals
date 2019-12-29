package com.mod.rest.service;

import com.mod.rest.model.Risk;
import com.mod.rest.model.UserHelper;
import com.mod.rest.repository.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by karim.omaya on 12/26/2019.
 */
@Service
public class RiskService {
    @Autowired
    SessionService sessionService;
    @Autowired
    RiskRepository riskRepository;

    public List<Risk> getAllRisksByUser(String SAMLart){
        UserHelper userHelper = sessionService.getSession(SAMLart);
        return riskRepository.getRiskByName(1, Integer.MAX_VALUE, "", userHelper.getId());
    }

    public String getRisksByUser(String SAMLart){
        UserHelper userHelper = sessionService.getSession(SAMLart);
        List<Risk> risks = riskRepository.getRiskByName(1, Integer.MAX_VALUE, "", userHelper.getId() );
        String riskIds = "";
        for (Risk risk: risks){
            if(!riskIds.equals("")) riskIds += ",";
            riskIds += risk.getId();
        }
        return riskIds;
    }
}
