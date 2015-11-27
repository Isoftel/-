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

/*  7:   */ import java.io.BufferedInputStream;
/*  8:   */ import java.io.IOException;
/*  9:   */ import java.io.PrintWriter;
/* 10:   */ import java.net.HttpURLConnection;
/* 11:   */ import java.net.MalformedURLException;
/* 12:   */ import java.net.URI;
/* 13:   */ import java.net.URISyntaxException;
/* 14:   */ import java.net.URL;

import org.apache.log4j.Logger;

public class PostXML {

    Date date = new Date();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    String local = msg.getString("localhost");
    String ip_source = msg.getString("ip_mo");
    String ip_destination = msg.getString("ip_and_part");

    Logger Log = Logger.getLogger(this.getClass());

    ///ส่งค่าเดียวแล้ว reture String ที่ได้รับจากฟั่ง True หลังจากส่ง XML แล้ว
    //r.getService_id(),r.getNumber_type(),r.getDescriptions(), r.getDetail(), r.getAccess(), encode);
    public String getXmlReg(String Service_id, String Number_type, String Text_Service, String Access, String id_pass) {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z // Z // X // a // G // E // S");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        //System.out.println("Day : " + dateFormat.format(date));
        //System.out.println("Day : " + dateFormat2.format(date));

        String xmlRes = null;

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
        //Text_Service
        sb.append("<ud type=\"text\" encoding=\"default\">").append("Test").append("</ud>");
        sb.append("<scts>").append(dateFormat2.format(date)).append("</scts>");
        sb.append("<dro>").append("true").append("</dro>");
        sb.append("</sms>");
        sb.append("</message>");
        System.out.println("Post : " + sb.toString());
        ///////ส่งค่า XML
        //this.Log.info("Get Xml true : " + xmlRes);
        System.out.println("Get Xml : " + xmlRes);
        return sb.toString();
    }

    public String PostXml(String StrXml, String StrUrl, String id_pass) {
        String xmlRes = null;
        /*
         try {
         //System.out.println("URL test : "+Log.info();
         Log.info("URL Post : " + StrUrl );
             
            
         PostMethod post = new PostMethod(StrUrl);
            
         post.setRequestBody("POST /HTTP/1.1");
         post.setRequestHeader("Authorization:", "Basic " + id_pass);
         post.setRequestHeader("Content-Type:", "text/xml");
         post.setRequestHeader("Connection:", "Close");
         post.setRequestHeader("Host:", ip_source);
         post.setRequestHeader("Content-Length", String.valueOf(StrXml.length()));
         Log.info("11111");
         RequestEntity entity = new StringRequestEntity(StrXml, "text/xml", "TIS-620");
         Log.info("22222");
         //RequestEntity entity = new StringRequestEntity(StrXml, "text/xml", "UTF-8");
         post.setRequestEntity(entity);
         HttpClient httpclient = new HttpClient();

         //////รับค่ากลับมาเป็น XML จากตัวที่เราส่งไป
         int returnCode = httpclient.executeMethod(post);
         Log.info("request response from true " + httpclient.getHost() + ":" + httpclient.getPort() + " : " + returnCode);
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
         */
        try {

            URI uri = new URI("http", null, StrUrl, null, null);
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST /HTTP/1.1");
            con.setRequestProperty("Content-type", "text/xml");
            con.setRequestProperty("Connection", "close");
            con.setRequestProperty("ContentLenght", "0");
            con.setUseCaches(false);
            PrintWriter pw = new PrintWriter(con.getOutputStream());
            pw.write(StrXml);
            pw.close();
            BufferedInputStream InStream = new BufferedInputStream(con.getInputStream());
            InStream.close();
            pw.flush();
            con.connect();
            con.disconnect();
            xmlRes = parseISToString(InStream, false);
            getResponsed(xmlRes);
            
        } catch (Exception e) {
            
        }

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
