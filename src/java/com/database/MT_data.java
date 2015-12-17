package com.database;

//import java.util.Base64;
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

    private List<data_user> id_user_reg;
    private List<data_user> id_user_unreg;
    private List<data_user> id_user_thank_sms;

    String id_user = "";
    String encode = "";
    String RegXML = "";
    String GetXML = "";

    @Override
    public void run() {

        //String U_test = "0101102156:qWACgXb4";
//        post_xml_true = "http://192.168.0.126:8080/Artemis/DeliveryRequest_true";
//        post_xml_true = "http://10.4.13.39:8004/tmcss2/fh.do";
//        post_xml_true = "203.144.187.120:55000";
        ////////////////////////////////////////// mt ส่งสมัคร
        List<data_user> id_user_reg = ProcessRegister();
        for (data_user r : id_user_reg) {
            try {
                //byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                //String encod = "0101102156:qWACgXb4";
                String encod = "7112402000:H84pL9aG";
                byte[] b = encod.getBytes(Charset.forName("UTF-8"));
                encode = new sun.misc.BASE64Encoder().encode(b);
                //default //TIS-620 //UTF-8 //
                RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode, "default");
                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
                //System.out.println("XML GET : " + GetXML);
                insert_r.insert_r(GetXML, "MT");
                this.Log.info("Get Xml : " + GetXML);
            } catch (Exception e) {
                this.Log.info("Error Reg : " + e);
            }
        }

        ////////////////////////////////////////////////////// mt ส่งยกเลิก
//        List<data_user> id_user_unreg = ProcessUnRegister();
//        for (data_user r : id_user_unreg) {
//            try {
//                byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
//                encode = new sun.misc.BASE64Encoder().encode(b);
//                RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode, "default");
//                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
//
//            } catch (Exception e) {
//                this.Log.info("Error Unreg : " + e);
//            }
//            //System.out.println("test Unreg : " + r.getNumber_type());
//        }
//        //////////////////////////////////////////////////////////////////
//        List<data_user> id_user_thank_sms = ProcessSMS();
//        for (data_user r : id_user_thank_sms) {
//            try {
//                byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
//                encode = new sun.misc.BASE64Encoder().encode(b);
//                RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode, "unicode");
//                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "sent");
//            } catch (Exception e) {
//                this.Log.info("Error SMS : " + e);
//            }
//        }
//        if (id_user_reg.size() > 0) {
//
//        }
    }

    public List<data_user> ProcessRegister() {
        user_room.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            String jdbcutf8 = "&useUnicode=true&characterEncoding=UTF-8";
            conn = DriverManager.getConnection(connectionUrl + jdbcutf8);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select TOP(500)*,services.service_id service_user from register "
                    + "INNER JOIN services  ON services.id  = register.service_id  "
                    + "INNER JOIN mobile    ON mobile.mobile_id = register.mobile_id   "
                    + "INNER JOIN mgr       ON mgr.operator_id = mobile.operator_id "
                    + "where register.status = '0' and register.status_code = '0' and register.api_req = 'REG' and mgr.api_req = 'REG' "
                    + "COLLATE  thai_ci_as");
            //INNER JOIN sms		 ON sms.msisdn =  mobile.msisdn
            while (rs.next()) {
                String content_sms = "";
                data_user iduser = new data_user();
                id_user = rs.getString("reg_id");
                String service = rs.getString("service_user");
                //service = "7112409002";
                String number = rs.getString("msisdn");
                String Text_Service = rs.getString("detail_reg");
                String access = rs.getString("access_number");

//                if (access.equals("4557878")) {
//                    rs = stmt.executeQuery("select * from sms where msisdn = '" + number + "' and service_id = '7112402001' ");
//                    while (rs.next()) {
//                        content_sms = rs.getString("content");
//                    }
//                    //dumpString();
//                }
                //access = "4557000";
                String date = rs.getString("cdate");
                String user = rs.getString("api_user");
                String pass = rs.getString("api_password");
                //TIS-620//UTF-8
//                String encode_test = URLEncoder.encode(Text_Service, "UTF-8");
//                this.Log.info("Test Reg : " + encode_test);

                //Text_Service = "ยินดีต้อนรับสู้ PLAYBOY จาก " + number;
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                iduser.setAccess(access);
                iduser.setEncoding(user + pass);
                iduser.setContent_sms(content_sms);

                //System.out.println("Test Reg : " + Text_Service);
                this.Log.info("ID reg : " + id_user);
                String sql = "UPDATE register SET status = '10' WHERE reg_id='" + id_user + "' ";
                stmt.executeUpdate(sql);
                user_room.add(iduser);
            }
        } catch (Exception e) {
            this.Log.info("Error select sql reg " + e);
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
            rs = stmt.executeQuery("select TOP(500)*,services.service_id service_user from register "
                    + "INNER JOIN services  ON services.id  = register.service_id  "
                    + "INNER JOIN mobile    ON mobile.mobile_id = register.mobile_id   "
                    + "INNER JOIN mgr       ON mgr.operator_id = mobile.operator_id "
                    + "where register.status = '0' and register.status_detail = '0'  and register.api_req = 'UNREG' and mgr.api_req = 'UNREG'");

            while (rs.next()) {
                data_user iduser = new data_user();
                id_user = rs.getString("reg_id");
                String service = rs.getString("service_user");
                //service = "7112402000";
                String number = rs.getString("msisdn");
                String Text_Service = rs.getString("detail_reg");
                String access = rs.getString("access_number");
                //access = "4557000";
                String date = rs.getString("cdate");
                String user = rs.getString("api_user");
                String pass = rs.getString("api_password");
                //System.out.println("Sql : " + " 1 " + service + " 2 " + number + " 3 " + Text_Service + " 4 " + access);
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                iduser.setAccess(access);
                iduser.setEncoding(user + pass);
                String sql = "UPDATE register SET status = '60' WHERE reg_id='" + id_user + "' ";
                stmt.executeUpdate(sql);
                user_room.add(iduser);
            }
        } catch (Exception e) {
            this.Log.info("Error select sql unreg " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return user_room;
    }

    public List<data_user> ProcessSMS() {
        user_room.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT TOP(500)* FROM sms "
                    + "INNER JOIN services  ON services.service_id  = sms.service_id  "
                    + "INNER JOIN mobile    ON mobile.msisdn = sms.msisdn "
                    + "INNER JOIN mgr       ON mgr.operator_id = mobile.operator_id "
                    + "WHERE mgr.api_req = 'REG' AND sms.statuscode = '0'");
            String id_user = "";
            while (rs.next()) {
                data_user iduser = new data_user();
                id_user = rs.getString("sms_id");
                String number = rs.getString("msisdn");
                String service_id = rs.getString("service_id");
                service_id = "7112402000";
                String product_id = rs.getString("Product_ID");
                String content = rs.getString("content");
                String access = rs.getString("access_number");
                //access = "4557000";
                String date = rs.getString("cdate");
                String user = rs.getString("api_user");
                String pass = rs.getString("api_password");

                iduser.setService_id(service_id);
                iduser.setNumber_type(number);
                String unicode_test = dumpStrings("ขอบคุณที่ใช้บริการ");
                iduser.setDescriptions(unicode_test);
                iduser.setAccess(access);
                iduser.setEncoding(user + pass);

                user_room.add(iduser);
            }

            String sql = "UPDATE sms SET status = '90' WHERE sms_id ='" + id_user + "' ";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            //System.out.println("Error : " + e);
            this.Log.info("Error select sql thank " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return user_room;
    }

    public String dumpStrings(String text) {
        String str_unicode = "";
        for (int i = 0; i < text.length(); i++) {
            str_unicode = str_unicode + "&#" + (int) text.charAt(i) + ";";
        }
        return str_unicode;
    }
}
