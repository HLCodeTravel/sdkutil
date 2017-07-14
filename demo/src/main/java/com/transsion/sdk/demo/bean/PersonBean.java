package com.transsion.sdk.demo.bean;


import com.transsion.json.annotations.TserializedName;

/**
 * Created by wenshuai.liu on 2017/4/28.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class PersonBean {
    private String classname;
    @TserializedName(name = "lastname")
    private String firstname;
    @TserializedName(name = "firstname")
    private String lastname;
    private int age;
    private String birthplace;

    private PersonBean() {
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassname() {
        return classname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getBirthplace() {
        return birthplace;
    }
}
