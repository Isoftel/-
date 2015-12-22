package com.database;

//import java.util.Base64;
import com.table_data.data_message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.table_data.data_user;
import com.table_data.data_userun;
import com.xml.Post_XML;
import com.xml.Set_XML;
import java.net.URLEncoder;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
import org.apache.log4j.Logger;

public class MT_data implements Runnable {

    Logger Log = Logger.getLogger(this.getClass());
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    Post_XML xml = new Post_XML();
    Set_XML str_xml = new Set_XML();
    XML_insert insert_r = new XML_insert();
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String user = msg.getString("user");
    String pass = msg.getString("pass");
    String port = msg.getString("port");

    String url = msg.getString("true_url");
    String post_xml_true = msg.getString("true_url");
    String url_mo = msg.getString("ip_mo");

        String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";&useUnicode=true&characterEncoding=UTF-8";

    int id_user = 0;
    String encode = "";
    String RegXML = "";
    String GetXML = "";

    @Override
    public void run() {

        Thread Reg = new Thread(new ThreaRegister());
        Reg.start();

        Thread UnReg = new Thread(new ThreaUnRegister());
        UnReg.start();

        Thread DraCo = new Thread(new ThreaDraco());
        DraCo.start();

    }

    private class ThreaRegister implements Runnable {

        Logger Log = Logger.getLogger(this.getClass());

        @Override
        public void run() {
            List<data_user> id_user_reg = ProcessRegister();
            this.Log.info("Found data register : " + id_user_reg.size());
            for (data_user r : id_user_reg) {
                try {
                    byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                    encode = new sun.misc.BASE64Encoder().encode(b);
                    String Text_Service = dumpStrings(r.getDescriptions());
                    RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), Text_Service, r.getAccess(), encode, "TIS-620");
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
                    insert_r.insert_r(GetXML, "MT", "30","10","REG");
                    this.Log.info("Get Xml Reg : " + GetXML);
                } catch (Exception e) {
                    this.Log.info("Error Reg : " + e);
                }
            }

        }

    }

    private class ThreaUnRegister implements Runnable {

        Logger Log = Logger.getLogger(this.getClass());

        @Override
        public void run() {
            ////////////////////////////////////////////////////// mt ส่งยกเลิก
            List<data_userun> id_user_unreg = ProcessUnRegister();
            this.Log.info("Found data Unregister : " + id_user_unreg.size());
            for (data_userun r : id_user_unreg) {
                try {
                    byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                    encode = new sun.misc.BASE64Encoder().encode(b);
                    String Text_Service = dumpStrings(r.getDescriptions());

                    RegXML = str_xml.getXmlUnreg(r.getService_id(), r.getNumber_type(), Text_Service, r.getAccess(), encode, "TIS-620");
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
                    insert_r.insert_r(GetXML, "MT", "50","40","UNREG");
                    this.Log.info("Get Xml UnReg : " + GetXML);
                } catch (Exception e) {
                    this.Log.info("Error Unreg : " + e);
                }
                //System.out.println("test Unreg : " + r.getNumber_type());
            }
        }

    }

    private class ThreaDraco implements Runnable {

        Logger Log = Logger.getLogger(this.getClass());

        @Override
        public void run() {
            List<data_message> id_user_thank_sms = ProcessSMS();
            for (data_message r : id_user_thank_sms) {
                try {
                    insert_r.insert_sms("Test : " + r.getDescriptions());
                    byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                    encode = new sun.misc.BASE64Encoder().encode(b);
                    String Text_Service = dumpStrings("ขอบคุณที่ใช้บริการคะ");
                    RegXML = str_xml.getXmlSMS(r.getService_id(), r.getNumber_type(), Text_Service, r.getAccess(), encode, "unicode");
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "sms");
                    this.Log.info("Get Xml SMS : " + GetXML);
                    insert_r.insert_sms(GetXML);
                } catch (Exception e) {
                    this.Log.info("Error SMS : " + e);
                }
            }
        }

    }

    public List<data_user> ProcessRegister() {
        List<data_user> user_room = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");     
            conn = DriverManager.getConnection(connectionUrl);
            
            stmt = conn.createStatement();
            String sql = "exec sp_getServiceDetail 'REG'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String content_sms = "";
                data_user iduser = new data_user();
                id_user = rs.getInt("reg_id");
                String service = "7112402000";
                String number = rs.getString("msisdn");
                String Text_Service = rs.getString("mt_msg");
                String access = rs.getString("access_number");
                String date = rs.getString("cdate");
                //rs.getString("api_user")
                String user = "7112402000";
                //rs.getString("api_password")
                String pass = "H84pL9aG";
                String data_user = "DATA Unreg : id_user " + id_user + " service " + service + " number " + number + " Text_Service " + Text_Service + " access " + access + " date " + date + " User " + user + " : " + pass;
                this.Log.info("DATA Reg : id_user " + data_user);
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                iduser.setAccess(access);
                iduser.setEncoding(user + ":" + pass);
                //iduser.setContent_sms(content_sms);
                this.Log.info("Test Reg : " + Text_Service);
                user_room.add(iduser);
 
                // sql = "UPDATE register SET status = '10' WHERE reg_id='" + id_user + "' ";
                sql = "exec sp_UpdateRegister '" + id_user + "' ";
                Statement st = conn.createStatement();
                st.executeUpdate(sql);

            }
            rs.close();
            stmt.close();
            conn.close();
             this.Log.info("return ProcessRegister " + user_room.size()+" Database Connection close "+conn.isClosed());
            //conn //stmt // rs
            //this.Log.info(conn.get
            return user_room;
        } catch (Exception e) {
            this.Log.info("Error ProcessRegister " + e);
            return user_room;
        }

    }

    public List<data_userun> ProcessUnRegister() {
        List<data_userun> user_roomun = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");     
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            String sql = "exec sp_getServiceDetail 'UNREG'";
            this.Log.info("ProcessUnRegister " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data_userun iduser = new data_userun();
                id_user = rs.getInt("reg_id");

                Log.info("id_user " + id_user);
                //rs.getString("service_user")
                String service = "7112402000";
                String number = rs.getString("msisdn");
                String Text_Service = rs.getString("mt_msg");
                String access = rs.getString("access_number");
                String date = rs.getString("cdate");
                //rs.getString("api_user")
                String user = "7112402000";
                //rs.getString("api_password")
                String pass = "H84pL9aG";
                String data_user = "DATA Unreg : id_user " + id_user + " service " + service + " number " + number + " Text_Service " + Text_Service + " access " + access + " date " + date + " User " + user + " : " + pass;
                this.Log.info("DATA Unreg : id_user " + data_user);
                //System.out.println("DATA Unreg : id_user " + data_user);
                //System.out.println("Sql : " + " 1 " + service + " 2 " + number + " 3 " + Text_Service + " 4 " + access);
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                iduser.setAccess(access);
                iduser.setEncoding(user + ":" + pass);

                user_roomun.add(iduser);

                sql = "UPDATE register SET status = '40' WHERE reg_id='" + id_user + "' ";
                Statement st = conn.createStatement();
                st.executeUpdate(sql);
                
            }
             rs.close();
            stmt.close();
            conn.close();
             this.Log.info("return ProcessRegister " + user_roomun.size()+" Database Connection close "+conn.isClosed());
            return user_roomun;
        } catch (Exception e) {
            this.Log.info("Error ProcessUnRegister " + e);
            return user_roomun;
        }

    }

    public List<data_message> ProcessSMS() {
        List<data_message> data_message = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");     
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP(500)* FROM sms "
                    + "INNER JOIN services  ON services.service_id  = sms.service_id  "
                    + "INNER JOIN mobile    ON mobile.msisdn = sms.msisdn "
                    + "INNER JOIN mgr       ON mgr.operator_id = mobile.operator_id "
                    + "INNER JOIN api_sms   ON api_sms.service_id = mgr.service_id "
                    + "WHERE mgr.api_req = 'REG' AND sms.status = '0' AND mgr.service_id = '6' and api_sms.mt_type = 'FREE'");
            String id_user = "";
            while (rs.next()) {
                data_message iduser = new data_message();
                id_user = rs.getString("sms_id");
                String number = rs.getString("msisdn");
                String service_id = rs.getString("service_id");
                //service_id = "7112402000";
                String product_id = rs.getString("Product_ID");
                String content = rs.getString("content");
                String access = rs.getString("access_number");
                //access = "4557000";
                String date = rs.getString("cdate");
                String user = rs.getString("api_user");
                String pass = rs.getString("api_password");

                iduser.setService_id(service_id);
                iduser.setNumber_type(number);
                //ขอบคุณที่ใช้บริการ
                String unicode_test = dumpStrings(rs.getString("mt_msg"));

                iduser.setDescriptions(unicode_test);
                iduser.setAccess(access);
                iduser.setEncoding(user + ":" + pass);
                String sql = "UPDATE sms SET status = '90' WHERE sms_id ='" + id_user + "' ";
                Statement st = conn.createStatement();
                st.executeUpdate(sql);
                data_message.add(iduser);
            }
            rs.close();
            stmt.close();
            conn.close();
             this.Log.info("return ProcessRegister " + data_message.size()+" Database Connection close "+conn.isClosed());
             
        } catch (Exception e) {
            //System.out.println("Error : " + e);
            this.Log.info("Error ProcessSMS " + e);
        }
        return data_message;
    }

    public String dumpStrings(String text) {
        String str_unicode = "";
        for (int i = 0; i < text.length(); i++) {
            str_unicode = str_unicode + "&#" + (int) text.charAt(i) + ";";
        }
        return str_unicode;
    }

}
