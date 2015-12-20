package com.xml;

import com.database.ProcessDatabase;
import com.database.SMS_Worning;
import static com.sun.org.apache.regexp.internal.RETest.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class DeliveryRequest_true extends HttpServlet {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat Format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Date date = new Date();

    ProcessDatabase insert = new ProcessDatabase();
    Logger Log = Logger.getLogger(this.getClass());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        //////////////////////////////////////////////////
        this.Log.info("DeliveryRequest Runing");
        PrintWriter out = null;
        ////////////////////  mo
        try {
            String http = "http://";
            http = "www.truemove.co.th";
            http = asciiToHex(http);
            
            System.out.println("http1 " + http);
//            http = dumpStrings(http);
//            System.out.println("http2 " + http);

//            String s = "0123456789";
//            byte ptext[] = http.getBytes("UTF8");
//            for (int i = 0; i < ptext.length; i++) {
//                System.out.print(ptext[i] + ",");
//            }

            //response.setContentType("text/xml;charset=UTF-8");
            //response.setContentType("text/xml;charset=TIS-620");
            out = response.getWriter();
            //String encoding = "TIS-620";

            //////////////////แปลง InputStream to String
            InputStream inStream = request.getInputStream();
            String result = getStringFromInputStream(inStream);
            this.Log.info("Request Get XML : " + result);
            //System.out.println("XML Http : " + result);
            //4557777//4557555//4557878//457000//457001//457002
            //////////////////รับ XML แยกการทำงาน MO,MT,Worning ไปตัดและส่ง Database
            String sms = (insert.getdata(result, "sms type=\"", 3, ""));
            String ud = (insert.getdata(result, "ud type=\"text\"", 4, "ud"));
            String rsr = (insert.getdata(result, "rsr type=\"", 3, ""));

            //System.out.println("SMS : " + sms + " UD : " + ud + " rsr " + rsr);
            //this.Log.info("MO//" + "SMS : " + sms + " UD : " + ud);
            if (sms.equals("mo")) {
                //รับ สมัคร ยกเลิก
                response.setContentLength(result.length());
                response.setHeader("Connection", "close");
                response.setContentType("text/xml");
                insert.ProcessDatabase(result, out);
            } else if (rsr.equals("sent") || rsr.equals("sent_delivered")) {
                this.Log.info("Sent Runing");
                //รับ SMS ส่งมาสองตรั้ง ยังไม่เก็บก่นอ กับ เก็บตัง
                response.setContentLength(0);
                response.setHeader("Connection", "close");
                response.setContentType("text/xml");
                insert.ProcessSMS(result, out);
            }
            //////////////////ส่งค่า HTTP กลับ

            //Dispatcher.writeServletResponse(resp, response);
        } catch (Exception e) {
            this.Log.info("Error HttpServletRequest : " + e);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    public String dumpStrings(String text) {
        String str_unicode = "";
        for (int i = 0; i < text.length(); i++) {
            //System.out.println("Len "+(int)text.charAt(i));
            str_unicode = str_unicode + (int) text.charAt(i);
        }
        return str_unicode;
    }

}
