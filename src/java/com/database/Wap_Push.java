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

    Connection conn2 = null;
    Statement stmt2 = null;
    ResultSet rs2 = null;

    List<data_user> user_room = new ArrayList<data_user>();

    Date date = new Date();
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
        System.out.println("GG");
        for (data_user r : id_user_reg) {
            byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
            encode = new sun.misc.BASE64Encoder().encode(b);
            try {
                /////////// Wap Push
                String http = asciiToHex("0605040b8423f0DC0601AE02056A0045C60C03");
                System.out.println("http " + http);
                String www = asciiToHex(r.getUrl());
                String fig1 = "000103";
                String name_api = asciiToHex(r.getApi_name());
                String ref = asciiToHex(r.getRef());
                String fig2 = "000101";
                String url = http + www + fig1 + name_api + ref + fig2;
                this.Log.info("Url Wap Push : " + url);
//                if (wap.equals("ส่งแบบธรรมดา")) {
//                    RegXML = str_xml.getXmlWapPush(r.getService_id(), r.getNumber_type(), r.getUrl(), r.getAccess(), encode, "TIS-620");
//                } else if (wap.equals("ส่งแบบ binary ทำการแปลง url ก่อน")) {
                System.out.println("r.getService_id() " + r.getService_id() + " r.getNumber_type() " + r.getNumber_type() + " url " + url + " r.getAccess() " + r.getAccess() + " encode " + encode);
                RegXML = str_xml.getXmlWapPush2(r.getService_id(), r.getNumber_type(), url, r.getAccess(), encode, "binary");
//                }
                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
//                insert_r.insert_r(GetXML, "MT");
            } catch (Exception e) {
                this.Log.info("Error : " + e);
            }
            System.out.println("WOW");
        }

    }

    public List<data_user> ProcessWapPush() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user_room.clear();
        String sql = "";
        String service_id = "";
        String id_content = "";
        String row_id_con = "";
        String time_con = "";
        try {
            //เวลาปุจจุบัน
            String date_new = dateFormat.format(NewDate);

            /////////////////content ที่ยังสมัครไม่เกิน 7 วัน
//            Date cdate_sms = Format_content.parse(date_format);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

//            and convert(varchar(10),getdate(),110)  >= contents.send_datetime  
//            and convert(varchar(10),dateadd(day,-7,getdate()),110) < contents.send_datetime 
//            and convert(varchar(10),dateadd(day,-7,getdate()),110) >= contents.send_datetime 
            rs = stmt.executeQuery("SELECT contents.id id_row_con,services.service_id ser_id,contents.service_id ser_con,contents.id id_con,* FROM contents "
                    + "INNER JOIN services  ON services.id  = contents.service_id "
                    + "INNER JOIN url  ON url.api_statename  = contents.contents_name "
                    + "INNER JOIN mobile  ON mobile.operator_id  = url.operator_id "
                    + "INNER JOIN subscribe  ON subscribe.mobile_id  = mobile.mobile_id "
                    + "where contents.id not in (select content_sended.content_id from content_sended where content_sended.oper='3')"
                    + "and subscribe.description = 'REG'"
                    + "and convert(varchar(10),getdate(),110) >= contents.send_datetime ");
            while (rs.next()) {
                data_user iduser = new data_user();
                //rs.getString("ser_id")  
                row_id_con = rs.getString("id_row_con");
                time_con = rs.getString("send_datetime");
                service_id = rs.getString("ser_con");

                Date date = dateFormat.parse(time_con);
                Date tomorrow = new Date(NewDate.getTime() - (7000 * 60 * 60 * 24));
                // ปัจจุบันน้อยกว่า -1
                if (tomorrow.compareTo(date) < 0) {
                    conn2 = DriverManager.getConnection(connectionUrl);
                    stmt2 = conn2.createStatement();
                    rs2 = stmt2.executeQuery("SELECT * FROM mgr where operator_id = '3' and api_req ='REG' and service_id = '3'");
                    while (rs2.next()) {
                        iduser.setEncoding(rs2.getString("api_user") + ":" + rs2.getString("api_password"));
                    }
                    conn2.close();
                    //iduser.setEncoding("7112409001:H84pL9aG");
                    iduser.setService_id("7112409001");
                }
                if (tomorrow.compareTo(date) >= 0) {
                    conn2 = DriverManager.getConnection(connectionUrl);
                    stmt2 = conn2.createStatement();
                    rs2 = stmt2.executeQuery("SELECT * FROM mgr where operator_id = '3' and api_req ='REG' and service_id = '" + service_id + "'");
                    while (rs2.next()) {
                        iduser.setEncoding(rs2.getString("api_user") + ":" + rs2.getString("api_password"));
                    }
                    conn2.close();
                    iduser.setService_id(rs.getString("ser_id"));
                }
                iduser.setUrl(rs.getString("api_url"));
                iduser.setApi_name(rs.getString("api_statename"));
                iduser.setRef(rs.getString("ref"));
                iduser.setNumber_type(rs.getString("msisdn"));
                iduser.setAccess(rs.getString("access_number"));

                id_content = rs.getString("id_con");

                user_room.add(iduser);
//                System.out.println("'" + rs.getString("msisdn") + "','" + rs.getString("ref") + "','" + date_new + "','" + service_id + "','" + id_content + "'");
                sql = "INSERT INTO download(MSISDN,REF_ID,TIMESTAMP,SERVICE_ID,CONTEN_ID) "
                        + "VALUES ('" + rs.getString("msisdn") + "','" + rs.getString("ref") + "','" + date_new + "','" + service_id + "','" + id_content + "')";
                stmt.execute(sql);

                String content_sen = "non";
                conn2 = DriverManager.getConnection(connectionUrl);
                stmt2 = conn2.createStatement();
                rs2 = stmt2.executeQuery("SELECT * FROM content_sended where service_id = '" + service_id + "' and content_id = '" + row_id_con + "' and oper = '3'");
                while (rs2.next()) {
                    content_sen = "post";
                }
                conn2.close();

                if (content_sen.equals("non")) {
                    sql = "INSERT INTO content_sended(send_date,service_id,content_id,oper) "
                            + "VALUES ('" + date_new + "','" + service_id + "','" + id_content + "','3')";
                    stmt.execute(sql);
                }
            }
            conn.close();

            ////////////////content ที่ยังสมัครเกิน 7 วัน
            // ดีง url รอเปลี่ยน
            //rs = stmt.executeQuery("SELECT * FROM download ");
        } catch (Exception e) {
//            System.out.println("Error select sql : " + e);
//            this.Log.info("Error select Wap push " + e);
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
