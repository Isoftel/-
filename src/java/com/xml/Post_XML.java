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
        try {
            Log.info("URL Post : " + StrUrl);

            String hh = "Authorization: Basic " + id_pass + "Content-Type: text/xml" + "Connection: Close" + "Connection: Close" + "Host: " + ip_Host + "Content-Length " + String.valueOf(StrXml.length());
            //Log.info("Header : " + hh);
            Log.info("XML Post : " + StrXml);
            //System.out.println("Header : " + hh);
            ///////////////////////////////////////////////
            //String p_test = "http://203.144.187.119:55000";
            URI uri = new URI(null, null, StrUrl, null, null);
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            //ip_Host = "203.144.187.120:55000";
            //TIS-620 //UTF-8
            // /HTTP/1.1 //Keep-Alive //Close
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Basic " + id_pass);
            con.setRequestProperty("Content-type", "text/xml");
            con.setRequestProperty("charset", "TIS-620");
            con.setRequestProperty("Content-Length", String.valueOf(StrXml.length()));
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Host", ip_Host);
            con.setUseCaches(false);
            PrintWriter pw = new PrintWriter(con.getOutputStream());
            pw.write(StrXml);
            pw.close();
            InputStream InStream = con.getInputStream();
            xmlRes = parseISToString(InStream, false);
            InStream.close();
            pw.flush();
            con.connect();
            con.disconnect();
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

}
