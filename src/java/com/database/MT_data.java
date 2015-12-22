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

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    List<data_user> user_room = new ArrayList<data_user>();
    List<data_message> data_message = new ArrayList<data_message>();

    private List<data_user> id_user_reg;
    private List<data_user> id_user_unreg;
    private List<data_message> id_user_thank_sms;

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
//        //////////////////////////////////////////////////////////////////

    }

    private class ThreaRegister implements Runnable {

        Logger Log = Logger.getLogger(this.getClass());

        @Override
        public void run() {
            List<data_user> id_user_reg = ProcessRegister();
            //this.Log.info("Found data register : " + id_user_reg.size());
            for (data_user r : id_user_reg) {
                try {
                    //byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
//                String encod = "7112402000:H84pL9aG";
                    byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                    encode = new sun.misc.BASE64Encoder().encode(b);
                    //default //TIS-620 //UTF-8 //

                    String Text_Service = dumpStrings(r.getDescriptions());

                    RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), Text_Service, r.getAccess(), encode, "TIS-620");
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
                    System.out.println("XML GET ProcessRegister : " + GetXML);
                    insert_r.insert_r(GetXML, "MT", "30");
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
            List<data_user> id_user_unreg = ProcessUnRegister();
            //this.Log.info("Found data Unregister : " + id_user_reg.size());
            for (data_user r : id_user_unreg) {
                try {
                    byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                    encode = new sun.misc.BASE64Encoder().encode(b);
                    RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode, "default");
                    System.out.println("Post ProcessUnRegister : " + RegXML);
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
                    System.out.println("XML GET ProcessUnRegister : " + GetXML);
                    insert_r.insert_r(GetXML, "MT", "50");
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
        user_room.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            String jdbcutf8 = "&useUnicode=true&characterEncoding=UTF-8";
            conn = DriverManager.getConnection(connectionUrl + jdbcutf8);
            stmt = conn.createStatement();
            String sql = "exec sp_getServiceDetail 'REG'";
            rs = stmt.executeQuery(sql);
            Log.info("ProcessRegister " + sql);
            //INNER JOIN sms		 ON sms.msisdn =  mobile.msisdn
            while (rs.next()) {
                String content_sms = "";
                data_user iduser = new data_user();
                id_user = rs.getInt("reg_id");
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
                this.Log.info("Message test : " + Text_Service);
                this.Log.info("id_user " + id_user + " service " + service + " number " + number + " access " + access + " date " + date + " User " + user + " : " + pass);
                //TIS-620//UTF-8
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                iduser.setAccess(access);
                iduser.setEncoding(user + ":" + pass);
                iduser.setContent_sms(content_sms);

                //System.out.println("Test Reg : " + Text_Service);
                //sql = "exec sp_UpdateRegister '" + id_user + "' ";
                sql = "UPDATE register SET status = '10' WHERE reg_id='" + id_user + "' ";
                stmt.executeUpdate(sql);
                user_room.add(iduser);
            }
        } catch (Exception e) {
            this.Log.info("Error ProcessRegister " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return user_room;
    }

    public List<data_user> ProcessUnRegister() {
        user_room.clear();
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            String sql = "exec sp_getServiceDetail 'UNREG'";
            Log.info("ProcessUnRegister " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data_user iduser = new data_user();
                id_user = rs.getInt("reg_id");
                Log.info("id_user " + id_user);
                //rs.getString("service_user")
                String service = "7112402000";
                String number = rs.getString("msisdn");
                String Text_Service = rs.getString("detail_reg");
                String access = rs.getString("access_number");
                String date = rs.getString("cdate");
                //rs.getString("api_user")
                String user = "7112402000";
                //rs.getString("api_password")
                String pass = "H84pL9aG";
                //this.Log.info("Message test : " + Text_Service);
                //this.Log.info("id_user " + id_user + " service " + service + " number " + number + " access " + access + " date " + date + " User " + user + " : " + pass);
                //System.out.println("Sql : " + " 1 " + service + " 2 " + number + " 3 " + Text_Service + " 4 " + access);
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                iduser.setAccess(access);
                iduser.setEncoding(user + ":" + pass);
                sql = "UPDATE register SET status = '40' WHERE reg_id='" + id_user + "' ";
                stmt.executeUpdate(sql);
                user_room.add(iduser);
            }
        } catch (Exception e) {
            this.Log.info("Error ProcessUnRegister " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return user_room;
    }

    public List<data_message> ProcessSMS() {
        data_message.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT TOP(500)* FROM sms "
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

                data_message.add(iduser);
            }

            String sql = "UPDATE sms SET status = '90' WHERE sms_id ='" + id_user + "' ";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            //System.out.println("Error : " + e);
            //this.Log.info("Error ProcessSMS " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
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
