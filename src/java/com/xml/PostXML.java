package com.xml;

import com.table_data.Responsed;
import com.table_data.data_user;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;


import org.apache.log4j.Logger;

public class PostXML {

    Date date = new Date();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    String local = msg.getString("localhost");
    String ip_source = msg.getString("ip_mo");
    String ip_destination = msg.getString("ip_and_part");

    Logger Log = Logger.getLogger(this.getClass());

    ///ส่งค่าเดียวแล้ว reture String ที่ได้รับจากฟั่ง True หลังจากส่ง XML แล้ว
    public String getXmlReg(String encoding, String mt, String service_id, String number, String abbreviated, String sender, String text, String dro, String id_pass) {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z // Z // X // a // G // E // S");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz");
        //System.out.println("Day : " + dateFormat.format(date));
        //System.out.println("Day : " + dateFormat2.format(date));

        String xmlRes = null;

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"TIS-620").append("\"?>");
        sb.append("<message>");
        sb.append("<sms type=\"mt\">");
        sb.append("<service-id>").append("").append("</service-id>");
        sb.append("<destination>");
        sb.append("<address>");
        sb.append("<number type=\"international\">").append("").append("</number>");
        sb.append("</address>");
        sb.append("</destination>");
        sb.append("<source>");
        sb.append("<address>");
        sb.append("<number type=\"abbreviated\">").append("").append("</number>");
        sb.append("<originate type=\"international\">").append("").append("</originate>");
        sb.append("</address>");
        sb.append("</source>");
        sb.append("<ud type=\"text\" encoding=\"default\">").append("").append("</ud>");
        sb.append("<scts>").append("").append("</scts>");
        sb.append("<dro>").append("").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");

        ///////ส่งค่า XML
        //this.Log.info("Get Xml true : " + xmlRes);
        System.out.println("Get Xml : " + xmlRes);
        return sb.toString();
    }

    public String PostXml(String StrXml, String StrUrl, String id_pass) {
        String xmlRes = null;
       
        return xmlRes;
    }

    public String parseISToString(InputStream is, boolean appendNewLine) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                if (appendNewLine) {
                    sb.append("\n");
                }
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

    /////////  แปลงค่าxml
    public Responsed getResponsed(String str) {
        Responsed rsp = new Responsed();
        getdata(str, "encoding", 1, "");
        //rsp.setEncoding(getdata(str, "encoding"));
//        rsp.setSize(getdata(str, "size"));
//        rsp.setStatus(getdata(str, "status"));
//        rsp.setDescription(getdata(str, "description"));
//        rsp.setRdate(getdata(str, "dates"));
        return rsp;
    }

    ///ตัดแยก String
    public String getdata(String in, String Tag, int ifroob, String back) {
        StringBuilder sb = new StringBuilder();
        String result = null;
        try {
            String document = in;
            String startTag = "";
            String endTag = "";
            if (ifroob == 1) {
                startTag = "<" + Tag + ">";
                endTag = "</" + Tag + ">";
            } else if (ifroob == 2) {
                startTag = "<" + Tag;
                endTag = "\">";
            } else if (ifroob == 3) {
                startTag = "<" + Tag + "";
                endTag = "\"?>";
            } else if (ifroob == 4) {
                startTag = "<" + Tag + ">";
                endTag = "<" + back + ">";
            }
            int start = document.indexOf(startTag) + startTag.length();
            int end = document.indexOf(endTag);
            result = document.substring(start, end);
        } catch (Exception ex) {
            //System.out.println("error : "+ex.getMessage());
            return result;
        }
        return result;
    }

}
