package com.xml;

import com.table_data.Responsed;
//import com.table_data.data_user;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import org.apache.log4j.Logger;

public class Post_XML {

    Date date = new Date();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    String local = msg.getString("localhost");
    String ip_source = msg.getString("ip_mo");
    String ip_destination = msg.getString("ip_and_part");

    Logger Log = Logger.getLogger(this.getClass());

    public String PostXml(String StrXml, String StrUrl, String id_pass) {
        String xmlRes = null;
        StrUrl = "http://192.168.0.126:8080/Artemis/DeliveryRequest_true";
        //StrUrl = "http://10.4.13.39:8004/tmcss2/fh.do";
        //StrUrl = "http://203.144.187.120:55000";

        try {
            Log.info("URL Post : " + StrUrl);
            PostMethod post = new PostMethod(StrUrl);

            //post.setRequestBody("POST /HTTP/1.1");
            post.setRequestHeader("Authorization:", "Basic " + id_pass);
            post.setRequestHeader("Content-Type:", "text/xml");
            post.setRequestHeader("Connection:", "Close");
            //post.setRequestHeader("Host:", ip_source);
            post.setRequestHeader("Content-Length", String.valueOf(StrXml.length()));
            RequestEntity entity = new StringRequestEntity(StrXml, "text/xml", "TIS-620");
            //RequestEntity entity = new StringRequestEntity(StrXml, "text/xml", "UTF-8");
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
            this.Log.info("Error Post : " + e);
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
//        getdata(str, "encoding", 1, "");
        //rsp.setEncoding(getdata(str, "encoding"));
//        rsp.setSize(getdata(str, "size"));
//        rsp.setStatus(getdata(str, "status"));
//        rsp.setDescription(getdata(str, "description"));
//        rsp.setRdate(getdata(str, "dates"));
        return rsp;
    }

    

}