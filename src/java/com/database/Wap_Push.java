package com.database;

import com.table_data.chack_102;
import com.table_data.data_user;
import com.table_data.data_userun;
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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class Wap_Push implements Runnable {

    Logger Log = Logger.getLogger(this.getClass());
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    Post_XML xml = new Post_XML();
    Set_XML str_xml = new Set_XML();

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
    Locale locale = new Locale("en", "US");

    @Override
    public void run() {

        List<data_user> id_user_reg = ProcessWapPush();

    }

    public List<data_user> ProcessWapPush() {
        user_room.clear();
        String sql = "";
        String service_id = "";
        String id_content = "";
        String row_id_con = "";
        String time_con = "";
        try {
            //เวลาปุจจุบัน
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
            String date_new = sdf.format(NewDate);
            System.out.println("New Date ProcessWapPush : " + date_new);
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
            this.Log.info("Sql ProcessWapPush : " + "exec sp_CheckContent '" + date_new + "','3'");
            while (rs.next()) {

                //this.Log.info("rs : " + rs.getString("contents_name") + " : " + rs.getInt("id"));
                Thread th = new Thread(new ProcessContents(rs.getInt("service_id"), NewDate, rs.getString("contents_name"), rs.getString("url_bitly"), rs.getInt("id"), rs.getString("ref")));
                th.start();

                sql = "INSERT INTO content_sended(send_date,service_id,content_id,oper) "
                        + "VALUES ('" + date_new + "','" + rs.getString("service_id") + "','" + rs.getString("id") + "','3')";
                stmt.execute(sql);
                this.Log.info("Sql ProcessWapPush IN : " + sql);
            }

            conn.close();

        } catch (Exception e) {
            this.Log.info("Error : " + e);
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
        String ref;
        HashMap map = new HashMap();
        Logger Log = Logger.getLogger(this.getClass());
        ProcessDatabase insert = new ProcessDatabase();

        public ProcessContents(int serviceid, Date SendDate, String ContentName, String referid, int Contentid, String ref) {
            this.serviceid = serviceid;
            this.SendDate = SendDate;
            this.referid = referid;
            this.Contentid = Contentid;
            this.ContentName = ContentName;
            this.ref = ref;

        }

        @Override
        public void run() {
            map = ProcessVw_getApiDetail();
            getPhoneNummber("exec dbo.sp_getMobileFree '" + serviceid + "','3'", "free");
            this.Log.info("exec dbo.sp_getMobileFree '" + serviceid + "','3'");
            getPhoneNummber("exec dbo.sp_getMobileCharge '" + serviceid + "','3'", "charge");
            this.Log.info("exec dbo.sp_getMobileCharge '" + serviceid + "','3'");
        }

        private HashMap ProcessVw_getApiDetail() {
            //this.Log.info("select * from Vw_getApiDetail where id = '" + this.serviceid + "' and mt_type ='WARNING'");
            HashMap m = new HashMap();
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select * from Vw_getApiDetail where id = '" + this.serviceid + "' and mt_type ='WARNING'");

                while (rs.next()) {
                    this.Log.info("ProcessVw_getApiDetail : " + rs.getString("access_number"));
                    m.put("service_id", rs.getString("service_id"));
                    m.put("access_number", rs.getString("access_number"));
                    m.put("api_sender", rs.getString("api_sender"));
                    m.put("api_password", rs.getString("api_password"));
                    break;
                }
                conn.close();

            } catch (Exception ex) {
                this.Log.info("Error ProcessVw_getApiDetail : " + ex);
            } finally {
                return m;
            }

        }

        private void InserSendedConten(ResultSet r, String messageid) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date_new = dateFormat.format(this.SendDate);
            String sql;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                this.Log.info("SQL InserSendedConten : " + "'" + r.getString("msisdn") + "','" + this.ref + "','" + this.SendDate + "','" + this.serviceid + "','" + this.Contentid + "'");
                sql = "INSERT INTO download(MSISDN,REF_ID,TIMESTAMP,SERVICE_ID,CONTEN_ID,OPERATOR,TEXTID) "
                        + "VALUES ('" + r.getString("msisdn") + "','" + this.ref + "','" + date_new + "','" + this.serviceid + "','" + this.Contentid + "','3','" + messageid + "')";

                stmt.execute(sql);

            } catch (SQLException ex) {
                this.Log.info("Error InserSendedConten : " + ex);
            } catch (ClassNotFoundException ex) {
                this.Log.info("Error InserSendedConten : " + ex);
            }
        }

        private void getPhoneNummber(String Command, String ch) {
            try {
                List<chack_102> msisdn_number = new ArrayList();
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(Command);
                this.Log.info("SQL PhoneNummber : " + Command);
                while (rs.next()) {

                    String url = dumpStrings(this.ContentName + " " + this.referid);
//                    String url = this.ContentName + " " + this.referid;
                    String user_pass = "";
                    String service = "";
                    if (ch.equals("free")) {
                        service = "7112402000";
                        user_pass = "7112402000:H84pL9aG";
                    } else if (ch.equals("charge")) {
                        service = this.map.get("service_id").toString();
                        user_pass = this.map.get("service_id").toString() + ":" + this.map.get("api_password").toString();
                    }
                    byte[] b = user_pass.getBytes(Charset.forName("UTF-8"));
                    user_pass = new sun.misc.BASE64Encoder().encode(b);

                    RegXML = str_xml.getXmlReg(service, rs.getString("msisdn"), url, this.map.get("access_number").toString(), user_pass, "TIS-620");
                    //RegXML = str_xml.getXmlWapPush2(this.map.get("service_id").toString(), rs.getString("msisdn"), url, this.map.get("access_number").toString(), user_pass, "unicode");
                    this.Log.info("Post XML WapPush : " + RegXML);
                    GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), user_pass, "mt");
                    this.Log.info("Get XML WapPush : " + GetXML);
                    String messageid = insert.getdata(GetXML, "destination messageid=\"", 3, "");
                    String code_get = insert.getdata(GetXML, "code", 1, "code");

                    if (code_get.equals("102")) {
                        chack_102 number = new chack_102();
                        number.setMsisdn(this.map.get("access_number").toString());
                        msisdn_number.add(number);
                    }

                    InserSendedConten(rs, messageid);
                }

                ///////////////////////////
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();

                for (int i = 0; i < msisdn_number.size(); i++) {
                    rs = stmt.executeQuery("SELECT * FROM mobile WHERE mobile_id = '" + msisdn_number.get(i).getMsisdn() + "'");
                    while (rs.next()) {
                        String sql_insert = "INSERT INTO dbo.register (api_req, reg_channel, mobile_id, service_id, "
                                + " reg_date, status,status_code) "
                                + "VALUES('UNREG','SMS', '" + rs.getString("mobile_id") + "', '" + serviceid + "',getdate(),'0','0')";
                        stmt.execute(sql_insert);
                        this.Log.info("SQL insert 102 : " + sql_insert);
                    }
                }
                ////////////////////////////  

            } catch (ClassNotFoundException ex) {
                this.Log.info("Error getPhoneNummber : " + ex);
            } catch (SQLException ex) {
                this.Log.info("Error getPhoneNummber : " + ex);
            } finally {
                try {
                    conn.close();
                } catch (Exception e) {
                }
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
