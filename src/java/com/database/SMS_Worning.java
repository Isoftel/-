package com.database;

import com.table_data.data_sms;
import com.xml.Post_XML;
import com.xml.Set_XML;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    List<data_sms> user_data = new ArrayList<data_sms>();

    private List<data_sms> sms_data = SMS();

    @Override
    public void run() {
        //////// SMS
        String encode = "";
        for (data_sms r : sms_data) {
            String SmsXML = null;
            String GetXML = null;
            try {
                byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
                encode = new sun.misc.BASE64Encoder().encode(b);
                String Text_Service = dumpStrings(r.getText_sms());
                SmsXML = str_xml.getXmlWorning(r.getService_id(), r.getNumber(), Text_Service, r.getAccess(), encode, "TIS-620");
                GetXML = post_xml.PostXml(SmsXML, msg.getString("ip_mo"), encode, "mt");
                this.Log.info("Get Xml Worning : " + GetXML);
                insert_data.insert_worning(GetXML, "SMS");

                //str_xml
            } catch (Exception e) {
                this.Log.info("Error Worning : " + e);
            }
        }
    }
    
    public List<data_sms> SMS() {
        user_data.clear();
        try {
            System.out.println("Run");
            String id_user = "";
            ///////////////// 457777
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            rs = stmt.executeQuery("exec dbo.sp_warning '4557777','3'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setNumber(rs.getString("msisdn"));

                conn2 = DriverManager.getConnection(connectionUrl);
                stmt2 = conn2.createStatement();
                rs2 = stmt2.executeQuery("SELECT sv.access_number acc_id,* FROM [PLAYBOY].[dbo].[register] r "
                        + "join [dbo].[mobile] m on r.mobile_id = m.mobile_id "
                        + "join [dbo].[services] sv on r.service_id = sv.id "
                        + "join [dbo].[api_sms] sms on r.service_id = sms.service_id "
                        + "where   m.msisdn='" + rs.getString("msisdn") + "' and sv.access_number = '4557777' and sms.mt_type ='WARNING'");
                while (rs2.next()) {
                    id_user = rs2.getString("reg_id");
                    //rs.getString("service_id")
                    iduser.setService_id("7112409000");
                    //rs.getString("acc_id")  //4557001
                    iduser.setAccess(rs.getString("acc_id"));
                    iduser.setText_sms(rs2.getString("mt_msg"));
                    this.Log.info("test ser : " + rs2.getString("mt_msg"));
                    //iduser.setCode(rs.getString("status"));
                    String user = "7112409000:H84pL9aG";
                    iduser.setEncoding(user);
                    user_data.add(iduser);

                }
                conn2.close();
            }
            conn.close();
            ///////////////// 4557555
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            rs = stmt.executeQuery("exec dbo.sp_warning '4557555','3'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setNumber(rs.getString("msisdn"));

                conn2 = DriverManager.getConnection(connectionUrl);
                stmt2 = conn2.createStatement();
                rs2 = stmt2.executeQuery("SELECT sv.access_number acc_id,* FROM [PLAYBOY].[dbo].[register] r "
                        + "join [dbo].[mobile] m on r.mobile_id = m.mobile_id "
                        + "join [dbo].[services] sv on r.service_id = sv.id "
                        + "join [dbo].[api_sms] sms on r.service_id = sms.service_id "
                        + "where   m.msisdn='" + rs.getString("msisdn") + "' and sv.access_number = '4557555' and sms.mt_type ='WARNING'");
                while (rs2.next()) {
                    id_user = rs2.getString("reg_id");
                    //rs.getString("service_id")
                    iduser.setService_id("7112409000");
                    //rs.getString("acc_id")  //4557001
                    iduser.setAccess(rs.getString("acc_id"));
                    iduser.setText_sms(rs2.getString("mt_msg"));
                    this.Log.info("test ser : " + rs2.getString("mt_msg"));
                    //iduser.setCode(rs.getString("status"));
                    String user = "7112409000:H84pL9aG";
                    iduser.setEncoding(user);
                    user_data.add(iduser);

                }
                conn2.close();
            }
            conn.close();
            //////////////// 4557002
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            rs = stmt.executeQuery("exec dbo.sp_warning '4557002','3'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setNumber(rs.getString("msisdn"));

                conn2 = DriverManager.getConnection(connectionUrl);
                stmt2 = conn2.createStatement();
                rs2 = stmt2.executeQuery("SELECT sv.access_number acc_id,* FROM [PLAYBOY].[dbo].[register] r "
                        + "join [dbo].[mobile] m on r.mobile_id = m.mobile_id "
                        + "join [dbo].[services] sv on r.service_id = sv.id "
                        + "join [dbo].[api_sms] sms on r.service_id = sms.service_id "
                        + "where   m.msisdn='" + rs.getString("msisdn") + "' and sv.access_number = '4557555' and sms.mt_type ='WARNING'");
                while (rs2.next()) {
                    id_user = rs2.getString("reg_id");
                    //rs.getString("service_id")
                    iduser.setService_id("7112409000");
                    //rs.getString("acc_id") //"4557001"
                    iduser.setAccess(rs.getString("acc_id"));
                    iduser.setText_sms(rs2.getString("mt_msg"));
                    this.Log.info("test ser : " + rs2.getString("mt_msg"));
                    //iduser.setCode(rs.getString("status"));
                    String user = "7112409000:H84pL9aG";
                    iduser.setEncoding(user);
                    user_data.add(iduser);

                }
                conn2.close();
            }
            conn.close();

//            String sql = "UPDATE subscribe SET sub_status = '40' WHERE id ='" + id_user + "' ";
//            String sql = "UPDATE register SET status_code = '50' WHERE sms_id ='" + id_user + "' ";
//            stmt.executeUpdate(sql);
        } catch (Exception e) {
            this.Log.info("Error Worning : " + e);
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
