package com.mod.rest.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBuilder<D> {

    private D data;
    private Pagination pagination;
    private Error error;
    private ResponseCode responseCode = ResponseCode.SUCCESS;


    public ResponseBuilder<D> data(D data) {
        if(data != null) {
            this.data = data;
        }
        else {
            this.responseCode = ResponseCode.NOT_FOUND;
        }
        return this;
    }

    public ResponseBuilder<D> status(ResponseCode responseCode) {
        if(responseCode != null && responseCode != ResponseCode.SUCCESS)
            this.responseCode = responseCode;
        return this;
    }

    public ResponseBuilder<D> build() {
        if (this.data == null && responseCode == null){
            this.responseCode = ResponseCode.NOT_FOUND;
        }else if (this.data != null){
            this.responseCode = ResponseCode.SUCCESS;
        }
        return this;
    }

    public ResponseBuilder<D> setPagination(Pagination pagination){
        this.pagination = pagination;
        return this;
    }



    public class Error {
        private int status;
        private String message;

        public Error(ResponseCode responseCode, String message) {


            if(responseCode != null && responseCode != ResponseCode.SUCCESS) {
                this.status = responseCode.getCode();
                this.message = message;
            }

        }
    }

}
