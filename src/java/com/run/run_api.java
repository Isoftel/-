/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.run;

import com.database.ProcessDatabase;
import com.database.SMS_Worning;
import com.database.MT_data;
import com.xml.Post_XML;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    Date date = new Date();
    //HH:mm:ss
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat_set_start = new SimpleDateFormat("yyyy-MM-dd 16:00:12");
    DateFormat dateFormat_set_end = new SimpleDateFormat("yyyy-MM-dd 16:00:14");

    DateFormat Format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
                Thread tt = new Thread(new MT_data());
                tt.setPriority(1);
                tt.start();
                
                worning();
                
                Thread.sleep(ThreadSleep);
            } catch (Exception ex) {
                Log.info("application exception " + ex.getMessage());
            }

        }
    }

    public void worning() {
        try {
            String date_warning = dateFormat.format(date);
            String date_start = dateFormat_set_start.format(date);
            String date_end = dateFormat_set_end.format(date);

            Date convertedDate = Format.parse(date_warning);
            Date start = Format.parse(date_start);
            Date end = Format.parse(date_end);

           // System.out.println("D1 ttd: " + convertedDate + " Start : " + start + " End : " + end);

            /////// Date < Date = -1 | Date = Date = 0 | Date > Date = 1
//            System.out.println("Show int : " + convertedDate.compareTo(start));
//            System.out.println("Show int : " + convertedDate.compareTo(end));
            if (convertedDate.compareTo(start) == -1 && convertedDate.compareTo(end) == 1) {
                //System.out.println("SMS Worning");
                Thread tt = new Thread(new SMS_Worning());
                tt.setPriority(1);
                tt.start();
            }

        } catch (Exception e) {
            System.out.println("Error Time : " + e);
        }
    }
}
