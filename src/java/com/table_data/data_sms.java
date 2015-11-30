package com.table_data;

public class data_sms implements java.io.Serializable {

    private String service_id;
    private String number;
    private String access;
    private String text_sms;
    private String code;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getText_sms() {
        return text_sms;
    }

    public void setText_sms(String text_sms) {
        this.text_sms = text_sms;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    

}
