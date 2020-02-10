package com.mod.soap.dao.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

/**
 * Created by karim on 2/9/20.
 */
@Data
public class SecurityConfig {
    JsonNode webservice ;
    int typecode;
    JsonNode output;
}
