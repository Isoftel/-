package com.table_data;

public class data_user implements java.io.Serializable {
    
    //private String req_rc;
    private String user;
    private String encoding;
    private String sms_type;
    private String service_id;
    private String number_type;
    private String access;
    private String sender;
    private String sms;
    private String content_sms;
    private String oper;
    private String descriptions;
    private String detail;

    public String getContent_sms() {
        return content_sms;
    }

    public void setContent_sms(String content_sms) {
        this.content_sms = content_sms;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
    
    
    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }
    
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getNumber_type() {
        return number_type;
    }

    public void setNumber_type(String number_type) {
        this.number_type = number_type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
    
}
