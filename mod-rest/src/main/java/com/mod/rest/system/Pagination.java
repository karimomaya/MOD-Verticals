package com.mod.rest.system;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by karim.omaya on 10/30/2019.
 */
@Setter
@Getter
public class Pagination {
    int lastPage = 0;
    long numberOfResults = 0;
    int pageNumber = 0;
    int pageSize = 0;
    boolean hasNext = false;
    boolean hasPrevious = false;

    public Pagination setNumberOfResults(long numberOfResults){
        this.numberOfResults = numberOfResults;
        return this;
    }

    public Pagination setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
        return this;
    }

    public Pagination setPageSize(int pageSize){
        this.pageSize = pageSize;
        return this;
    }

    public Pagination build(){
        if(numberOfResults == 0 || pageSize == 0){
            this.hasNext = false;
            this.hasPrevious = false;
        } else {
            // 21 / 10 =
            this.lastPage = (int) (Math.ceil(numberOfResults / pageSize));
            if (this.lastPage == 0 && numberOfResults > 0){
                this.lastPage = 1;
            }

            if (this.lastPage == this.pageNumber) {
                this.hasNext = false;
                this.hasPrevious = true;
            }
            else if(this.pageNumber == 0){
                this.hasNext = true;
                this.hasPrevious = false;
            }
            else {
                this.hasNext = true;
                this.hasPrevious = true;
            }

        }

        return this;

    }


}
