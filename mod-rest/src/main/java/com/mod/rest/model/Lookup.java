package com.mod.rest.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by MinaSamir on 5/27/2020.
 */

@Entity

public class Lookup {
    @Id
    Long Id;
    @Column(name="category")
    String category;
    @Column(name="ar_value")
    String ar_value;
    @Column(name="eng_value")
    String eng_value;
    @Column(name="parent_id")
    Integer parent_id;
    @Column(name="key")
    String key;
    @Column(name="sequence")
    Integer sequence;
    @Column(name="stringkey")
    String stringkey;

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public String getValueByLanguage(String lang) {
        if (lang.equals("ar"))
            return ar_value;
        else return eng_value;
    }
}
