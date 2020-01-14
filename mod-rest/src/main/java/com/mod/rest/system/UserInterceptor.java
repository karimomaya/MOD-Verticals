package com.mod.rest.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.mod.rest.entity.UserDetails;
import com.mod.rest.model.User;
import com.mod.rest.model.UserHelper;
import com.mod.rest.repository.UserHelperRepository;
import com.mod.rest.repository.UserRepository;
import com.mod.rest.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by karim.omaya on 12/24/2019.
 */
@Component
public class UserInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    SessionService sessionService;
    @Autowired
    Config config;
    @Autowired
    UserHelperRepository userHelperRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) throws Exception {
        if (request.getMethod().equals("OPTIONS")) return true;


        String SAMLart = request.getHeader("samlart");
        if (SAMLart == null) return true;

        UserHelper user = sessionService.getSession(SAMLart);

        if (user == null) sessionService.login(SAMLart);

        return true;
    }


}
