package com.run;

import com.database.ProcessDatabase;
import com.database.SMS_Worning;
import com.database.MT_data;
import com.database.Wap_Push;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import com.xml.Post_XML;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
                Thread.sleep(ThreadSleep);
                System.out.println("Runing API");
                this.Log.info("Runing API");
                /////  ส่ง MT
//                Thread tt = new Thread(new MT_data());
//                tt.setPriority(1);
//                tt.start();
                
                ///// ส่ง Wap Push โดยเช็ควันหลังสมัคร 5 วัน ส่ง URL มี2แบบ
//                Thread tt2 = new Thread(new Wap_Push());
//                tt2.setPriority(1);
//                tt2.start();
                
//                worning();
            } catch (Exception ex) {
                //System.out.println("Error Runing : " + ex);
                this.Log.info("application exception " + ex);
            } finally {
                try {
                    Thread.sleep(ThreadSleep);
                } catch (InterruptedException ex) {
                    
                }
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
            if (convertedDate.compareTo(start) == -1 && convertedDate.compareTo(end) == 1) {
//                Thread tt = new Thread(new SMS_Worning());
//                tt.setPriority(1);
//                tt.start();
            }

        } catch (Exception e) {
            System.out.println("Error Time : " + e);
        }
    }

}
