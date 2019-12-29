package com.mod.soap.repository;

import com.mod.soap.model.RequestNumber;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * Created by karim.omaya on 12/7/2019.
 */
@Component
public class RequestNumberRepository {

    @PostConstruct
    public void initData() {

    }

    public RequestNumber generateRequestNumber(String userdId, String type) {
        Assert.notNull(userdId, "The User Id must not be null");
        Assert.notNull(userdId, "The type must not be null");
        RequestNumber requestNumber = new RequestNumber();
        requestNumber.setNumber(userdId);
        return requestNumber;
    }
}
