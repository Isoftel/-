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
import java.util.ResourceBundle;

public class XML_insert {

    ProcessDatabase insert = new ProcessDatabase();

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    ResourceBundle msg = ResourceBundle.getBundle("configs");
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String user = msg.getString("user");
    String pass = msg.getString("pass");

    public String insert_r(String xml, String id) {
        System.out.println(xml);

        String service = insert.getdata(xml, "service-id", 1, "");
        String messageid = insert.getdata(xml, "<destination messageid=\"", 3, "");
        String number = insert.getdata(xml, "number type=\"international\"", 4, "number");
        //String number_text = "non";
        String number_text = insert.getdata(xml, "number type=\"\"", 1, "number");
        
        try {
            
//            if (number_text.equals("non")) {
//                //number_text = insert.getdata(xml, "number type=\"abbreviated\"", 1, "number");
//            }
            if(messageid.equals("gg")){
                
            }
        } catch (Exception e) {
            System.out.println("Er ?? " + e);
        }

//        String code = insert.getdata(xml, "code", 1, "");
//        String description = insert.getdata(xml, "description", 1, "description");
//        System.out.println("service " + service + " messageid " + messageid + " number " + number + " number_text " + number_text + " code " + code + " description " + description);
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
//            conn = DriverManager.getConnection(connectionUrl);
//            stmt = conn.createStatement();
//
//            //String sql = "INSERT INTO register (api_req)VALUES('" + jumid_schedules + "')";
//            //stmt.execute(sql);
//            String sql = "select * from register "
//                    + "INNER JOIN services  ON services.id  = register.service_id "
//                    + "INNER JOIN mobile    ON mobile.mobile_id = register.mobile_id "
//                    + "where services.service_id = '" + service + "' and mobile.msisdn = '" + number + "' and  register.status = '0'";
//            rs = stmt.executeQuery(sql);
//            String id_register = "";
//            while (rs.next()) {
//                id_register = rs.getString("reg_id");
//            }
//            sql = "UPDATE register SET status_code = '" + code + "' WHERE reg_id='" + id_register + "'";
//            stmt.executeUpdate(sql);
//
//            conn.close();
//        } catch (Exception e) {
//            System.out.println("Error : " + e);
//        }
        return xml;
    }
}
