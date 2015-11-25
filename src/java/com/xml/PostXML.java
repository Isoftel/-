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
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.FileRequestEntity;

import org.apache.log4j.Logger;

public class PostXML {

    Date date = new Date();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    String local = msg.getString("localhost");
    String post_xml_true = msg.getString("true_url");
    String ip_source = msg.getString("ip_post");
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
        sb.append("<?xml version=\"1.0\" encoding=\"").append("").append("\"?>");
        sb.append("message id=\"").append("").append("\">");
        sb.append("<rsr type=\"reply\">");
        sb.append("<service-id>").append("").append("</service-id>");
        sb.append("<destination messageid=\"").append("").append("\">");
        sb.append("<address>");
        sb.append("</destination>");
        sb.append("<source>");
        sb.append("<address>");
        sb.append("</source>");
        sb.append("<rsr_detail status=\"success\">");
        sb.append("<code>").append("").append("</code>");
        sb.append("<description>").append("").append("</description>");
        sb.append("</rsr_detail>");
        sb.append("</rsr>");
        sb.append("</message>");
        
/*
        <?xml version="1.0" encoding="UTF-8"?>
<message id="routerTestbed@Testbed:3104400">
<rsr type="reply">
<service-id>0101102156</service-id>
<destination messageid="6156634A">
<address>
<number type="abbreviated">1042</number>
</address>
</destination>
<source>
<address>
<number type="international">668xxxxxxxx</number>
</address>
</source>
<rsr_detail status="success">
<code>0</code>
<description>Success receive request</description>
</rsr_detail>
</rsr>
</message>
        */


        /////////////////////////////////
//        Responsed rsp = new Responsed();
//        rsp.setCode(getdata(sb.toString(), "service-id"));
//        rsp.setDescription(getdata(sb.toString(), "scts"));
        //ip_post = "http://192.168.0.126:8080/Artemis/DeliveryRequest_true";
        ///////ส่งค่า XML
        System.out.println("Post Xml : " + sb.toString());
        this.Log.info("Post XML : " + sb.toString());
        try {
            PostMethod post = new PostMethod(post_xml_true);
            post.setRequestHeader("Connection", "Close");
            post.setRequestHeader("Authorization", "Basic " + id_pass);
            post.setRequestHeader("Host", ip_source);
            post.setRequestHeader("Content-Length", String.valueOf(sb.toString().length()));
            post.setRequestHeader("Content-Type", "text/xml");
            
            //RequestEntity entity = new StringRequestEntity(sb.toString(), "text/xml", "TIS-620");
            RequestEntity entity = new StringRequestEntity(sb.toString(), "text/xml", "UTF-8");
            post.setRequestEntity(entity);
            HttpClient httpclient = new HttpClient();

            //////รับค่ากลับมาเป็น XML จากตัวที่เราส่งไป
            int returnCode = httpclient.executeMethod(post);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                post.getResponseBodyAsString();
            } else {
                InputStream inStream = post.getResponseBodyAsStream();
                xmlRes = parseISToString(inStream, false);
            }

        } catch (Exception e) {
            System.out.println("Error Port : " + e);
            this.Log.info("Error Post : " + e);
        }
        //this.Log.info("Get Xml true : " + xmlRes);
        //System.out.println("Get Xml : " + xmlRes);
        return xmlRes;
    }

    /////ส่งหลายค่า
    public String getXml_true(List<data_user> sub, String oper, String lot, String service) {
        StringBuilder sb = new StringBuilder();
//        sb.append("<Request>");
//        sb.append("<transId>").append(sub.get(0).getApi_job()).append("</transId>");
//        sb.append("<command>Reg</command>");
//        sb.append("<lots>").append(lot).append("</lots>");
//        sb.append("<service>").append(service).append("</service>");
//        sb.append("<pack>43</pack>");
//        sb.append("<oper>").append(oper).append("</oper>");
//        int r = 0;
//        StringBuilder ani = new StringBuilder();
//        for (data_user c : sub) {
//            ani.append("<msisdn>").append(c.getApi_job()).append("</msisdn>");
//            r++;
//        }
//        sb.append("<number size=\"").append(r).append("\">");
//        sb.append(ani.toString());
//        sb.append("</number>");
//        sb.append("</Request>");
        return sb.toString();
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
        rsp.setEncoding(getdata(str, "encoding"));
//        rsp.setSize(getdata(str, "size"));
//        rsp.setStatus(getdata(str, "status"));
//        rsp.setDescription(getdata(str, "description"));
//        rsp.setRdate(getdata(str, "dates"));
        return rsp;
    }

    ///ตัดแยก String
    public String getdata(String in, String Tag) {
        StringBuilder sb = new StringBuilder();
        String result = null;
        try {
            String document = in;
            String startTag = "<" + Tag + ">";
            String endTag = "</" + Tag + ">";
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
