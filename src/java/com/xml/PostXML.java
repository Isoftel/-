package com.xml;

import com.table_data.Responsed;
import com.table_data.data_user;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
        //sb.append("<?xml version=\"1.0\" encoding=\"").append("").append("\"?>");

        /*
         <?xml version="1.0" encoding="TIS-620"?>
         <message>
         <sms type="mt">
         <service-id>0101102156</service-id>
         <destination>
         <address>
         <number type="international">668xxxxxxxx</number>
         </address>
         </destination>
         <source>
         <address>
         <number type="abbreviated">1037</number>
         <originate type="international">668xxxxxxxx</originate>
         </address>
         </source>
         <ud type="text" encoding="default">Test</ud>
         <scts>2009-05-21T11:05:29+07:00</scts>
         <dro>true</dro>
         </sms>
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
//        try {
//            POST / HTTP/1.1
//Authorization: Basic MDEwMTEwMjE1NjpxV0FDZ1hiNA==
//Content-type: text/xml
//Connection: Close
//Host:203.144.187.120:55000
//Content-length: 465
           //PostXml(String StrXml, String StrUrl);
//            PostMethod post = new PostMethod(post_xml_true);
//            post.setRequestHeader("POST /", "HTTP/1.1");
//            post.setRequestHeader("Authorization:", "Basic " + id_pass);
//            post.setRequestHeader("Content-Type:", "text/xml");
//            post.setRequestHeader("Connection:", "Close");
//            post.setRequestHeader("Host:", ip_source);
//            post.setRequestHeader("Content-Length", String.valueOf(sb.toString().length()));
//
//            //RequestEntity entity = new StringRequestEntity(sb.toString(), "text/xml", "TIS-620");
//            RequestEntity entity = new StringRequestEntity(sb.toString(), "text/xml", "UTF-8");
//            post.setRequestEntity(entity);
//            HttpClient httpclient = new HttpClient();
//
//            //////รับค่ากลับมาเป็น XML จากตัวที่เราส่งไป
//            int returnCode = httpclient.executeMethod(post);
//
//            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
//                System.err.println("The Post method is not implemented by this URI");
//                post.getResponseBodyAsString();
//            } else {
//                InputStream inStream = post.getResponseBodyAsStream();
//                xmlRes = parseISToString(inStream, false);
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error Port : " + e);
//            this.Log.info("Error Post : " + e);
//        }
//        //this.Log.info("Get Xml true : " + xmlRes);
//        System.out.println("Get Xml : " + xmlRes);
//        getResponsed(xmlRes);
        return xmlRes;
    }

    public void PostXml(String StrXml, String StrUrl)
            throws URISyntaxException, MalformedURLException, IOException {
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
