package com.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Set_XML {
    Locale locale = new Locale("en", "US");
    Date date = new Date();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z",locale);

    ///ส่งค่าเดียวแล้ว reture String ที่ได้รับจากฟั่ง True หลังจากส่ง XML แล้ว
    public String getXmlReg(String Service_id, String Number_type, String Text_Service, String Access, String id_pass, String type) {
        //System.out.println("Day : " + dateFormat2.format(date));
        StringBuilder sb = new StringBuilder();
        //TIS-620 //UTF-8
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
        //sb.append("<sender>").append("True Move").append("</sender>");
        sb.append("</address>");
        sb.append("</source>");
        //String Test = "Test"; //Text_Service
        //type = "TIS-620";
        sb.append("<ud type=\"text\" encoding=\"").append(type).append("\">").append(Text_Service).append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }
     public String getXmlUnreg(String Service_id, String Number_type, String Text_Service, String Access, String id_pass, String type) {
        //System.out.println("Day : " + dateFormat2.format(date));
        StringBuilder sb = new StringBuilder();
        //TIS-620 //UTF-8
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
        //sb.append("<sender>").append("True Move").append("</sender>");
        sb.append("</address>");
        sb.append("</source>");
        //String Test = "Test"; //Text_Service
        //type = "TIS-620";
        sb.append("<ud type=\"text\" encoding=\"").append(type).append("\">").append(Text_Service).append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }
    public String getXmlSMS(String Service_id, String Number_type, String Text_Service, String Access, String id_pass, String type) {
        //System.out.println("Day : " + dateFormat2.format(date));
        StringBuilder sb = new StringBuilder();
        //TIS-620 //UTF-8
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
        //String Test = "Test"; //Text_Service
        //type = "TIS-620";
        sb.append("<ud type=\"text\" encoding=\"").append(type).append("\">").append(Text_Service).append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        //false
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }
    

    ///// ส่ง Worning เตือนเมื่อสมัครครบ 5 วัน
    public String getXmlWorning(String Service_id, String Number_type, String Text_Service, String Access, String id_pass, String type) {
        StringBuilder sb = new StringBuilder();
        //TIS-620 //UTF-8 //ISO-8859-1
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
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
        //String Test = "Test"; //Text_Service
        //type = "TIS-620";
        sb.append("<ud type=\"text\" encoding=\"").append(type).append("\">").append(Text_Service).append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }

    public String getXmlWapPush(String Service_id, String Number_type, String Text_Service, String Access, String id_pass, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
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
        sb.append("<ud type=\"text\" encoding=\"").append(type).append("\">").append(Text_Service).append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }

    public String getXmlWapPush2(String Service_id, String Number_type, String Text_Service, String Access, String id_pass, String type) {
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
        sb.append("<ud type=\"binary\">").append(Text_Service).append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        return sb.toString();
    }

    
}
