package com.run;

import com.database.ProcessDatabase;
import com.database.SMS_Worning;
import com.database.MT_data;
import com.database.Wap_Push;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import com.xml.Post_XML;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
//                System.out.println("Runing API");
                //&#3586;&#3629;&#3610;&#3588;&#3640;&#3603;&#3607;
//                String text = "&#xe2a;&#xe27;&#xe31;&#xe14;&#xe14;&#xe35;";
//                text = text.replace("&#", "");
//                text = text.replace(";", "");
//                text = new String(text.getBytes("UTF-8"), "ISO-8859-1");
//                String endo = URLEncoder.encode(text, "UTF-8");
//                text = dumpStrings2(text);

                //String endo2 = URLEncoder.encode(text, "TIS-620");
                //String endo3 = URLDecoder.decode(endo, "TIS-620");
//                System.out.println("Encode 1 : " + text);
                
                

                //System.out.println("Encode 2 : " + endo2);
                this.Log.info("Runing API");
                /////  ส่ง MT
                Thread tt = new Thread(new MT_data());
                tt.setPriority(1);
                tt.start();
                ///// ส่ง Wap Push  ส่ง URL มี2แบบ
//                Thread tt2 = new Thread(new Wap_Push());
//                tt2.setPriority(1);
//                tt2.start();
                worning();
            } catch (Exception ex) {
                //System.out.println("Error Runing : " + ex);
                this.Log.info("application exception " + ex);
            } finally {

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
            /////// Date < Date = -1 | Date = Date = 0 | Date > Date = 1
            if (convertedDate.compareTo(start) == -1 && convertedDate.compareTo(end) == 1) {
            Thread tt = new Thread(new SMS_Worning());
            tt.setPriority(1);
            tt.start();
            }

        } catch (Exception e) {
            System.out.println("Error Time : " + e);
        }
    }

    public String dumpStrings(String text) {
        String str_unicode = "";
        for (int i = 0; i < text.length(); i++) {
            //System.out.println("Len "+(int)text.charAt(i));
            str_unicode = str_unicode + "&#" + (int) text.charAt(i) + ";";
        }
        return str_unicode;
    }

    public String dumpStrings2(String text) {
        text = text.replace("&", "");
        text = text.replace(";", "");
        String[] arr = text.split("#");
        String str_unicode = "";
        System.out.println("Arr : " + text);
        try {
            for (int i = 1; i < arr.length; i++) {

                int hexVal = Integer.parseInt(arr[i]);
                str_unicode += (char) hexVal;
            }

        } catch (Exception e) {
            System.out.println("Err en " + e);
        }
        return str_unicode;
    }
}
