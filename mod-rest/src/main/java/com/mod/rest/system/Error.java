package com.mod.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by karim.omaya on 10/30/2019.
 */
@Getter
@Setter
public class Error {
    int errorCode;
    String message;
}
