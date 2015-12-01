package com.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;



public class Set_XML {
    
    Date date = new Date();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    
    ///ส่งค่าเดียวแล้ว reture String ที่ได้รับจากฟั่ง True หลังจากส่ง XML แล้ว
     public String getXmlReg(String Service_id, String Number_type, String Text_Service, String Access, String id_pass) {
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        //System.out.println("Day : " + dateFormat2.format(date));
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"TIS-620\"?>");
        sb.append("<message>");
        sb.append("<sms type=\"mt\">");
        sb.append("<service-id>").append(Service_id).append("</service-id>");
        sb.append("<destination>");
        sb.append("<address>");
        sb.append("<number type=\"international\">").append(Number_type).append("</number>");
        sb.append("</address>");
        sb.append("</destination>");
        sb.append("<source>");
        sb.append("<address>");
        sb.append("<number type=\"abbreviated\">").append(Access).append("</number>");
        sb.append("<originate type=\"international\">").append(Number_type).append("</originate>");
        sb.append("</address>");
        sb.append("</source>");
        sb.append("<ud type=\"text\" encoding=\"default\">").append("Test").append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }
      public String getXmlSms(String Service_id, String Number_type, String Text_Service, String Access, String id_pass) {
          StringBuilder sb = new StringBuilder();
          
          
          
          return sb.toString();
      }
}