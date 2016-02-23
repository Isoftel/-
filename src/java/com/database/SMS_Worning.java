package com.database;

import com.table_data.data_sms;
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
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class SMS_Worning implements Runnable {

    Logger Log = Logger.getLogger(this.getClass());
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    Post_XML post_xml = new Post_XML();
    Set_XML str_xml = new Set_XML();
    XML_insert insert_data = new XML_insert();
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
    Locale locale = new Locale("en", "US");
    List<data_sms> user_data = new ArrayList<data_sms>();

    private List<data_sms> sms_data = SMS();
    Date NewDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", locale);
    String new_date_format = dateFormat.format(NewDate);

    @Override
    public void run() {
        //////// SMS
        String encode = "";
        for (data_sms r : sms_data) {
            String SmsXML = null;
            String GetXML = null;
            try {

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                String text = "Success receive request";
                String sql = "INSERT INTO register(api_req, reg_channel, mobile_id, service_id, reg_date, status,status_code) "
                        + "VALUES('WORNING','SMS','" + r.getNumber() + "','" + r.getService_id() + "','" + new_date_format + "','40','0')";
                stmt.execute(sql);

                byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                encode = new sun.misc.BASE64Encoder().encode(b);
                String Text_Service = dumpStrings(r.getText_sms());
                SmsXML = str_xml.getXmlWorning(r.getService_id(), r.getNumber(), Text_Service, r.getAccess(), encode, "TIS-620");
                /*
                
                 iduser.setNumber(rs.getString("msisdn"));
                 iduser.setService_id(service);
                 iduser.setAccess(access);
                 iduser.setText_sms(text);
                 iduser.setEncoding(user);
                 iduser.setService_id(id_num_ser);
                 */
                GetXML = post_xml.PostXml(SmsXML, msg.getString("ip_mo"), encode, "mt");
                this.Log.info("Get Xml Worning : " + GetXML);
                insert_data.insert_worning(GetXML, "SMS");

                //str_xml
            } catch (Exception e) {
                this.Log.info("Error Worning P and G : " + e);
            }
        }
    }

    public List<data_sms> SMS() {
        user_data.clear();
        try {
            String id_user = "";
            ///////////////// 457777
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";

            String user = "7112409000:H84pL9aG";
            String service = "7112409000";
            String access = "";
            String text = "";
            String id_num_ser = "";

            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Vw_getApiDetail where access_number = '4557777' and mt_type ='WARNING'");
            while (rs.next()) {
                access = rs.getString("access_number");
                text = rs.getString("mt_msg");
                id_num_ser = rs.getString("id");
                this.Log.info("test ser : " + rs.getString("mt_msg"));
                //iduser.setCode(rs2.getString("status"));
            }
            conn.close();

            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("exec sp_warning '4557777','3'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setNumber(rs.getString("msisdn"));
                iduser.setService_id(service);
                iduser.setAccess(access);
                iduser.setText_sms(text);
                iduser.setEncoding(user);
                iduser.setService_id(id_num_ser);
                user_data.add(iduser);
            }
            conn.close();

            ///////////////// 4557555
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Vw_getApiDetail where access_number = '4557555' and mt_type ='WARNING'");
            while (rs.next()) {
                access = rs.getString("access_number");
                text = rs.getString("mt_msg");
                id_num_ser = rs.getString("id");
                this.Log.info("test ser : " + rs.getString("mt_msg"));
                //iduser.setCode(rs2.getString("status"));
            }
            conn.close();

            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("exec dbo.sp_warning '4557555','3'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setNumber(rs.getString("msisdn"));
                iduser.setService_id(service);
                iduser.setAccess(access);
                iduser.setText_sms(text);
                iduser.setEncoding(user);
                iduser.setService_id(id_num_ser);
                user_data.add(iduser);
            }
            conn.close();

            //////////////// 4557002
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Vw_getApiDetail where access_number = '4557002' and mt_type ='WARNING'");
            while (rs.next()) {
                access = rs.getString("access_number");
                text = rs.getString("mt_msg");
                id_num_ser = rs.getString("id");
                this.Log.info("test ser : " + rs.getString("mt_msg"));
                //iduser.setCode(rs2.getString("status"));
            }
            conn.close();

            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("exec dbo.sp_warning '4557002','3'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setNumber(rs.getString("msisdn"));
                iduser.setService_id(service);
                iduser.setAccess(access);
                iduser.setText_sms(text);
                iduser.setEncoding(user);
                iduser.setService_id(id_num_ser);
                user_data.add(iduser);
            }
            conn.close();

//            String sql = "UPDATE subscribe SET sub_status = '40' WHERE id ='" + id_user + "' ";
//            String sql = "UPDATE register SET status_code = '50' WHERE sms_id ='" + id_user + "' ";
//            stmt.executeUpdate(sql);
        } catch (Exception e) {
            this.Log.info("Error Worning SQL : " + e);
        }
        return user_data;
    }

    public String dumpStrings(String text) {
        String str_unicode = "";
        for (int i = 0; i < text.length(); i++) {
            str_unicode = str_unicode + "&#" + (int) text.charAt(i) + ";";
        }
        return str_unicode;
    }
}

//            rs = stmt.executeQuery("SELECT s.id id_sub,sv.access_number service_user,m.msisdn,* FROM [PLAYBOY].[dbo].[register] r "
//                    + "join [dbo].[subscribe] s on r.mobile_id = s.mobile_id "
//                    + "join [dbo].[mobile] m on s.mobile_id = m.mobile_id "
//                    + "join [dbo].[services] sv on s.service_id = sv.id "
//                    + "join [dbo].[mgr] mg on mg.service_id = r.service_id "
//                    + "join [dbo].[api_sms] ap on ap.service_id = r.service_id "
//                    + "where convert(varchar(10),s.cdate,110) = convert(varchar(10),dateadd(day,-5,getdate()),110)  "
//                    + "and s.description = 'REG' and s.sub_status = 30 and mg.operator_id = '3' and ap.mt_type = 'WARNING' and r.status_code ='000'");
