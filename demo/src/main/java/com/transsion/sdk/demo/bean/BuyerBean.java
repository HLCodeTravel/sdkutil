package com.transsion.sdk.demo.bean;

import com.transsion.json.annotations.TserializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenshuai.liu on 2017/5/3.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class BuyerBean implements Serializable {
    private static final long serialVersionUID = 130616625461L;
    @TserializedName(name = "buyer")
    private String buyer;
    @TserializedName(name = "sex")
    private String sex;
    @TserializedName(name = "lineItems")
    private List<Lineitems> lineitems;
    @TserializedName(include = false)
    private String address;
    @TserializedName(name = "teststring")
    private List<String> testList;
    private int phone;

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setLineitems(List<Lineitems> lineitems) {
        this.lineitems = lineitems;
    }

    public List<Lineitems> getLineitems() {
        return lineitems;
    }

    public List<String> getTestList() {
        return testList;
    }

    public void setTestList(List<String> list) {
        this.testList = list;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(int number) {
        this.phone = number;
    }

    public int getPhone() {
        return phone;
    }

    public static class Lineitems implements Serializable {
        private static final long serialVersionUID = 130616625462L;
        @TserializedName(name = "name")
        private String name;
        @TserializedName(name = "quantity")
        private int quantity;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() {
            return quantity;
        }
    }

}