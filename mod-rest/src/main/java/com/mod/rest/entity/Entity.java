package com.mod.rest.entity;

import com.mod.rest.system.Http;

/**
 * Created by karim.omaya on 12/7/2019.
 */
public abstract class Entity {

    public String create(Http request){
        String message = createMessage();
        return request.cordysRequest(message);
    }

    abstract String createMessage();

    public String update(Http request) {
        String message = updateMessage();
        return request.cordysRequest( message);
    }
    abstract String updateMessage();


}
