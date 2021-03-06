/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.table_data.Responsed;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class XML_insert {

    ProcessDatabase insert = new ProcessDatabase();
    Logger Log = Logger.getLogger(this.getClass());

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    ResourceBundle msg = ResourceBundle.getBundle("configs");
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String data_base2 = msg.getString("data2");
    String user = msg.getString("user");
    String pass = msg.getString("pass");
    
    Locale locale = new Locale("en", "US");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", locale);
    Date NewDate = new Date();

    public String insert_r(String xml, String id, String Request, String detail_status, String reg_unreg) {
        String time = dateFormat.format(NewDate);

        String service = insert.getdata(xml, "service-id", 1, "");
        //<destination messageid=\"
        String messageid = insert.getdata(xml, "destination messageid=\"", 3, "");
        String number = insert.getdata(xml, "number type=\"international\"", 4, "number");
        String code = insert.getdata(xml, "code", 1, "");
        String description = insert.getdata(xml, "description", 1, "description");
        String number_text = "non";
        //String number_text = insert.getdata(xml, "number type=\"\"", 1, "number");
        number_text = insert.getdata(xml, "number type=\"abbreviated\"", 4, "number");
        //number_text = insert.getdata(xml, "number type=\"\"", 1, "number");
//            if (number_text.equals("non")) {
//                
//            }

        //this.Log.info("service " + service + " messageid " + messageid + " number " + number + " number_text " + number_text + " code " + code + " description " + description);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            //String sql = "UPDATE register SET status_code = '" + code + "',status = '" + Request + "',send_date ='" + time + "' WHERE status='10' and  ";
            String sql = "update r set r.status = '" + Request + "',r.status_code='" + code + "',r.txid ='" + messageid + "'"
                    + ",r.status_detail='" + description + "',r.send_date = getdate() "
                    + "from register r inner join mobile m on m.mobile_id = r.mobile_id "
                    + "and m.msisdn = '" + number + "' and r.status = '" + detail_status + "' and api_req ='" + reg_unreg + "'";
            this.Log.info(sql);
            stmt.executeUpdate(sql);
            conn.close();

        } catch (Exception e) {
            this.Log.info("insert_r : " + e);
            //System.out.println("Error : " + e);
        }
        return xml;
    }

    public String insert_sms(String xml) {
        String service = insert.getdata(xml, "service-id", 1, "");
        String messageid = insert.getdata(xml, "destination messageid=\"", 3, "");
        String number = insert.getdata(xml, "number type=\"international\"", 4, "number");
        String code = insert.getdata(xml, "code", 1, "");
        String description = insert.getdata(xml, "description", 1, "description");
        String number_text = "non";
        number_text = insert.getdata(xml, "number type=\"abbreviated\"", 4, "number");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base2 + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            String sql = "UPDATE sms SET statuscode = '100' WHERE msisdn ='" + number + "' AND service_id = '" + service + "' AND statuscode = '90' ";
            stmt.executeUpdate(sql);

            conn.close();
        } catch (Exception e) {
            this.Log.info("insert_sms : " + e);
        }

        return xml;
    }

    public String insert_worning(String xml, String id) {
        String time = dateFormat.format(NewDate);

        String service = insert.getdata(xml, "service-id", 1, "");
        //<destination messageid=\"
        String messageid = insert.getdata(xml, "destination messageid=\"", 3, "");
        String number = insert.getdata(xml, "number type=\"international\"", 4, "number");
        String code = insert.getdata(xml, "code", 1, "");
        String description = insert.getdata(xml, "description", 1, "description");
        String number_text = "non";
        //String number_text = insert.getdata(xml, "number type=\"\"", 1, "number");
        number_text = insert.getdata(xml, "number type=\"abbreviated\"", 4, "number");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            String id_service = "";
            String id_number = "";
            String sql = "select *,register.service_id id_ser from register "
                    + "INNER JOIN services  ON services.id  = register.service_id "
                    + "INNER JOIN mobile    ON mobile.mobile_id = register.mobile_id "
                    + "where services.service_id = '" + service + "' and mobile.msisdn = '" + number + "' and  register.status = '40'";
            rs = stmt.executeQuery(sql);
            String id_register = "";
            while (rs.next()) {
                id_register = rs.getString("reg_id");
                id_service = rs.getString("id_ser");
                id_number = rs.getString("mobile_id");
            }


            sql = "UPDATE register SET status_code = '50',send_date=getdate() WHERE reg_id ='" + id_register + "' ";
            stmt.executeUpdate(sql);
            
            conn.close();
        } catch (Exception e) {
            this.Log.info("insert_worning : " + e);
            //System.out.println("Error : " + e);
        }
        return xml;
    }
}
