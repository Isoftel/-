package com.database;

import com.table_data.data_user;
import com.xml.Post_XML;
import com.xml.Set_XML;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class Wap_Push implements Runnable {

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

    private List<data_user> id_user_WapPush;

    String id_user = "";
    String encode = "";
    String RegXML = "";
    String GetXML = "";

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    Date NewDate = new Date();

    @Override
    public void run() {

        List<data_user> id_user_reg = ProcessWapPush();

        for (data_user r : id_user_reg) {
            byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
            encode = new sun.misc.BASE64Encoder().encode(b);

            try {
                /////////// Wap Push
                String wap = "";
//                if (wap.equals("ส่งแบบธรรมดา")) {
//                    RegXML = str_xml.getXmlWapPush(r.getService_id(), r.getNumber_type(), r.getUrl(), r.getAccess(), encode, "TIS-620");
//                } else if (wap.equals("ส่งแบบ binary ทำการแปลง url ก่อน")) {
                    asciiToHex("");  ///
                    RegXML = str_xml.getXmlWapPush2(r.getService_id(), r.getNumber_type(),r.getUrl(), r.getAccess(), encode, "binary");
//                }

//                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
//                System.out.println("Back XML : " + GetXML);
//                insert_r.insert_r(GetXML, "MT");
            } catch (Exception e) {
                this.Log.info("Error : " + e);
                System.out.println("Error Wap Push : " + e);
            }
        }

    }

    public List<data_user> ProcessWapPush() {
        user_room.clear();
        String sql="";
        try {
            String date_format = dateFormat.format(NewDate);
            Date cdate_sms = dateFormat.parse(date_format);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            //send_datetime //send_date
            rs = stmt.executeQuery("SELECT contents.id,contents.contents_file,contents.content_status,mobile.msisdn,services.access_number,services.service_id,mgr.api_user,mgr.api_password FROM contents " +"INNER JOIN subscribe  ON subscribe.service_id  = contents.service_id " +
                        "INNER JOIN mobile    ON mobile.mobile_id   = subscribe.mobile_id " +
                        "INNER JOIN services  ON services.id  = contents.service_id " +
                        "INNER JOIN mgr       ON mgr.operator_id = mobile.operator_id " +
                        "where convert(varchar(10),send_date,110) = convert(varchar(10),dateadd(day,-5,getdate()),110) " +
                        "and content_status = '10' and mgr.api_job = 'REG' and mobile.operator_id = '3' ");
            String id_user = "";
            String service_id = "";
            while (rs.next()) {

                data_user iduser = new data_user();
                id_user = rs.getString("id");
                service_id = rs.getString("service_id");
                
                iduser.setService_id(service_id);
                iduser.setUrl(rs.getString("contents_file"));
                iduser.setNumber_type(rs.getString("msisdn"));
                iduser.setAccess(rs.getString("access_number"));
                iduser.setEncoding(rs.getString("api_user") + rs.getString("api_password"));
                    
                user_room.add(iduser);
                
                sql = "UPDATE sms SET status = '3' WHERE sms_id ='" + id_user + "' ";
                stmt.executeUpdate(sql);
            
                sql = "INSERT INTO content_sended(send_date,service_id,content_id) "
                    + "VALUES ('" + cdate_sms + "','" + service_id + "','" + id_user + "')";
                stmt.execute(sql);
            }
            // ดีง url รอเปลี่ยน
            //rs = stmt.executeQuery("SELECT * FROM download ");
            conn.close();
        } catch (Exception e) {
            //System.out.println("Error : " + e);
            this.Log.info("Error select sql thank" + e);
        }
        return user_room;
    }

    private static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

}
