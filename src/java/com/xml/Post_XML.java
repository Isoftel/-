package com.xml;

import com.table_data.Responsed;
import java.io.BufferedInputStream;
//import com.table_data.data_user;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
    String ip_Host = msg.getString("ip_Host");

    Logger Log = Logger.getLogger(this.getClass());

    public String PostXml(String StrXml, String StrUrl, String id_pass, String type_header_xml) {
        String xmlRes = null;
        //StrUrl = "http://192.168.0.126:8080/Artemis/DeliveryRequest_true";
        //StrUrl = "http://10.4.13.39:8004/tmcss2/fh.do";
        //StrUrl = "http://203.144.187.120:55000";

        try {
            Log.info("URL Post : " + StrUrl);

            String hh = "Authorization: Basic " + id_pass + "Content-Type: text/xml" + "Connection: Close" + "Connection: Close" + "Host: " + ip_Host + "Content-Length " + String.valueOf(StrXml.length());
            Log.info("Header : " + hh);
            Log.info("XML Post : " + StrXml);
            //System.out.println("Header : " + hh);
            System.out.println("XML Post : " + StrXml);
            ///////////////////////////////////////////////
            //http://203.144.187.120:55000  //"http"
            String p_test = "http://203.144.187.119:55000";
            URI uri = new URI(null, null, p_test, null, null);
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
//            System.out.println("POST /HTTP/1.1");
//            System.out.println("Authorization: Basic " + id_pass);
//            System.out.println("Content-Type: text/xml");
//            System.out.println("Charset: TIS-620");
//            System.out.println("Content-Length " + String.valueOf(StrXml.length()));
//            System.out.println("Connection: Close");
//            System.out.println("Host: " + ip_Host);
            //TIS-620 //UTF-8
            //con.setRequestMethod("POST /HTTP/1.1");
            con.setRequestProperty("Authorization", "Basic " + id_pass);
            con.setRequestProperty("Content-type", "text/xml");
            con.setRequestProperty("Charset", "TIS-620");
            con.setRequestProperty("Content-Length", String.valueOf(StrXml.length()));
            //Close
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Host", ip_Host);
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

            //////////////////////////////////////////////
//            PostMethod post = new PostMethod(StrUrl);
//            if (type_header_xml.equals("mt")) {
//                Log.info("MT Running");
//                System.out.println("MT Run");
//                post.setRequestHeader("Authorization: ", "Basic " + id_pass);
//                post.setRequestHeader("Content-Type: ", "text/xml");
//                post.setRequestHeader("Charset: ", "TIS-620");
//                post.setRequestHeader("Content-Length ", String.valueOf(StrXml.length()));
//                post.setRequestHeader("Connection: ", "Close");
//                post.setRequestHeader("Host: ", ip_Host);
//            } else if (type_header_xml.equals("sent")) {
//                post.setRequestHeader("Content-Length ", String.valueOf(StrXml.length()));
//                post.setRequestHeader("Connection: ", "Keep-Alive");
//                post.setRequestHeader("Host: ", ip_Host);
//                post.setRequestHeader("Content-Type: ", "text/xml");
//            }
//            //post.setRequestBody("POST /HTTP/1.1");
//            //hh
//            RequestEntity entity = new StringRequestEntity(StrXml, "text/xml", "TIS-620");
//            //RequestEntity entity = new StringRequestEntity(StrXml, "text/xml", "UTF-8");
//            post.setRequestEntity(entity);
//            HttpClient httpclient = new HttpClient();
//
//            //////รับค่ากลับมาเป็น XML จากตัวที่เราส่งไป
//            int returnCode = httpclient.executeMethod(post);
//
//            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
//                //System.err.println("The Post method is not implemented by this URI");
//                post.getResponseBodyAsString();
//            } else {
//                InputStream inStream = post.getResponseBodyAsStream();
//                xmlRes = parseISToString(inStream, false);
//            }
        } catch (Exception e) {
            this.Log.info("Error Post : " + e);
            System.out.println("Error Post : " + e);
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
