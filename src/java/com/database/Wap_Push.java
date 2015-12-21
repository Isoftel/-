package com.database;

import com.table_data.data_user;
import com.xml.Post_XML;
import com.xml.Set_XML;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
        for (data_user r : id_user_reg) {

            try {
                /////////// Wap Push

//                if (wap.equals("ส่งแบบธรรมดา")) {
//                    RegXML = str_xml.getXmlWapPush(r.getService_id(), r.getNumber_type(), r.getUrl(), r.getAccess(), encode, "TIS-620");
//                } else if (wap.equals("ส่งแบบ binary ทำการแปลง url ก่อน")) {
                //System.out.println("r.getService_id() " + r.getService_id() + " r.getNumber_type() " + r.getNumber_type() + " url " + url + " r.getAccess() " + r.getAccess() + " encode " + encode);
//                }
//                RegXML = str_xml.getXmlWapPush2(r.getService_id(), r.getNumber_type(), url, r.getAccess(), encode, "binary");
//                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "wap_push");
//                insert_r.insert_r(GetXML, "MT");
            } catch (Exception e) {
                this.Log.info("Error : " + e);
            }
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

            String id_ser = "";
            String access = "";
            String text = "";
            /////////////////content ที่ยังสมัครไม่เกิน 7 วัน
//            Date cdate_sms = Format_content.parse(date_format);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("exec sp_CheckContent '" + date_new + "','3'");
            while (rs.next()) {

                Thread th = new Thread(new ProcessContents(rs.getInt("id"), NewDate, rs.getString("contents_name"), rs.getString("ref"), rs.getInt("id")));
                th.start();

            }
            conn.close();

//                sql = "INSERT INTO content_sended(send_date,service_id,content_id,oper) "
//                        + "VALUES ('" + date_new + "','" + service_id + "','" + id_content + "','3')";
//                stmt.execute(sql);
        } catch (Exception e) {

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

    private class ProcessContents implements Runnable {

        int serviceid;
        Date SendDate;
        String ContentName;
        String referid;
        int Contentid;
        HashMap map = new HashMap();

        public ProcessContents(int serviceid, Date SendDate, String ContentName, String referid, int Contentid) {
            this.serviceid = serviceid;
            this.SendDate = SendDate;
            this.referid = referid;
            this.Contentid = Contentid;
            this.ContentName = ContentName;

        }

        @Override
        public void run() {
            map = ProcessVw_getApiDetail();
            getPhoneNummber("exec dbo.sp_getMobileFree '" + serviceid + "','3'");
            getPhoneNummber("exec dbo.sp_getMobileCharge '" + serviceid + "','3'");
        }

        private HashMap ProcessVw_getApiDetail() {
            HashMap m = new HashMap();
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from Vw_getApiDetail where id = '" + this.serviceid + "' and mt_type ='WARNING'");

                while (rs.next()) {

                    m.put("service_id", rs.getString("service_id"));
                    m.put("access_number", rs.getString("access_number"));
                    m.put("api_sender", rs.getString("api_sender"));
                    m.put("api_password", rs.getString("api_password"));
                    break;
                }
                conn.close();

            } catch (Exception ex) {

            } finally {
                return m;
            }

        }

        private void InserSendedConten(ResultSet r) {
            //                System.out.println("'" + rs.getString("msisdn") + "','" + rs.getString("ref") + "','" + date_new + "','" + service_id + "','" + id_content + "'");
            String sql;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                sql = "INSERT INTO download(MSISDN,REF_ID,TIMESTAMP,SERVICE_ID,CONTEN_ID) "
                        + "VALUES ('" + r.getString("msisdn") + "','" + r.getString("ref") + "','" + this.SendDate + "','" + this.serviceid + "','" + this.Contentid + "')";
                stmt.execute(sql);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Wap_Push.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Wap_Push.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void getPhoneNummber(String Command) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(Command);
                while (rs.next()) {

                    //String http = "0605040b8423f0DC0601AE02056A0045C60C03";
                    String http = dumpStrings("http://");
                    String www = dumpStrings(this.referid);
                    String fig1 = dumpStrings("000103");
                    String name_api = dumpStrings(this.ContentName);
                    //String ref = dumpStrings(r.getRef());
                    String ref = "";
                    String fig2 = dumpStrings("000101");
                    String url = http + www + fig1 + name_api + ref + fig2;
                    String user = this.map.get("service_id").toString() + ":" + this.map.get("api_password").toString();
                    byte[] b = user.getBytes(Charset.forName("UTF-8"));
                    encode = new sun.misc.BASE64Encoder().encode(b);

                    RegXML = str_xml.getXmlWapPush2(this.map.get("service_id").toString(), rs.getString("msisdn"), url, this.map.get("access_number").toString(), encode, "unicode");
                    //RegXML = str_xml.getXmlWapPush2(this.map.get("service_id"),rs.getString("msisdn"),url,this.map.get("access_number"), encode, "unicode");
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "wap_push");
                    InserSendedConten(rs);
                }

            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Wap_Push.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Wap_Push.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        private String PraserXml(){
//            return 
//        }
        public String dumpStrings(String text) {
            String str_unicode = "";
            for (int i = 0; i < text.length(); i++) {
                str_unicode = str_unicode + "&#" + (int) text.charAt(i) + ";";
            }
            return str_unicode;
        }
    }
}
