/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.run;

import com.database.get_data;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

public class run_api extends HttpServlet implements Runnable {

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
                Log.info("Runing");
                Thread tt = new Thread(new get_data());
                tt.setPriority(1);
                tt.start();
                Thread.sleep(ThreadSleep);

            } catch (InterruptedException ex) {
                //Log.info("application exception " + ex.getMessage());
            }

        }
    }

}
