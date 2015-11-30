/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.run;

import com.database.ProcessDatabase;
import com.database.get_data;
import com.xml.PostXML;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

public class run_api extends HttpServlet implements Runnable {

    ProcessDatabase xml = new ProcessDatabase();
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    int ThreadSleep = Integer.parseInt(msg.getString("Thread"));
    Thread th;
    String msdfsdfd;
    Logger Log = Logger.getLogger(this.getClass());

    @Override
    public void init(ServletConfig config) {

        th = new Thread(this);
        th.setPriority(1);
        th.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Runing 1");
                this.Log.info("Runing Test");
                Thread tt = new Thread(new get_data());
                tt.setPriority(1);
                tt.start();

                Thread.sleep(ThreadSleep);

                String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                        + "<message id=\"routerTestbed@Testbed:3104400\">\n"
                        + "<sms type=\"mo\">\n"
                        + "<retry count=\"0\" max=\"0\"/>\n"
                        + "<destination messageid=\"6156634A\">\n"
                        + "<address>\n"
                        + "<number type=\"abbreviated\">4688900</number>\n"
                        + "</address>\n"
                        + "</destination>\n"
                        + "<source>\n"
                        + "<address>\n"
                        + "<number type=\"international\">9853435568</number>\n"
                        + "</address>\n"
                        + "</source>\n"
                        + "<ud type=\"text\">R</ud>\n"
                        + "<scts>2009-05-21T11:03:20Z</scts>\n"
                        + "<service-id>7112402001</service-id>\n"
                        + "</sms>\n"
                        + "<from>SMPP_CMG1</from>\n"
                        + "<to>HttpAdapter:: 0101102156</to>\n"
                        + "</message>";
        //7112402001 -3
                //System.out.println("XML : "+result);
                //this.Log.info("Get Xml true : " + result);

                String encoding = (xml.getdata(result, "?xml version=\"1.0\" encoding=\"", 2, ""));
                String message = (xml.getdata(result, "message id=\"", 3, ""));
                String sms = (xml.getdata(result, "sms type=\"", 3, ""));
                String messageid = (xml.getdata(result, "destination messageid=\"", 3, ""));
                String destination = (xml.getdata(result, "number type=\"abbreviated\"", 4, "number"));
                String number = (xml.getdata(result, "number type=\"international\"", 4, "number"));
                String ud = (xml.getdata(result, "ud type=\"text\"", 4, "ud"));
                String time = (xml.getdata(result, "scts", 1, "scts"));
                String service = (xml.getdata(result, "service-id", 1, ""));
                String from = (xml.getdata(result, "from", 1, ""));
                String to = (xml.getdata(result, "to", 1, ""));

        //System.out.println(" 1 " + encoding + " 2 " + sms + " 3 " + service + " 4 " + destination + " 5 " + number + " 6 " + ud + " 7 " +time);
            } catch (InterruptedException ex) {
                Log.info("application exception " + ex.getMessage());
            }

        }
    }

}
