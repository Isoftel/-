package com.xml;

import com.database.ProcessDatabase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class DeliveryRequest_true extends HttpServlet {

    ProcessDatabase insert = new ProcessDatabase();
    Logger Log = Logger.getLogger(this.getClass());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/xml;charset=TIS-620");
        PrintWriter out = response.getWriter();
        ////////////////////  mo
        try {
            String encoding = "TIS-620";
            //////////////////แปลง InputStream to String
            InputStream inStream = request.getInputStream();
            String result = getStringFromInputStream(inStream);

            //////////////////นับ XML ไปตัดและส่ง Database
            String str_xml = insert.ProcessDatabase(result, out);
            
            //////////////////ส่งค่า HTTP กลับ
            response.setContentLength(result.length());
            response.setHeader("Connection", "close");
            response.setContentType("text/xml");
            
            
            //ส่งค่ากลับ PrintWriter 
            insert.request_printwriter(out, encoding, result);
            


            
            //this.Log.info("Get Xml true : " + result);
            //
        } catch (Exception e) {
            System.out.println("Error De : " + e);
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

}
