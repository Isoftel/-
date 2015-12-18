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
            String id_user = "";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT s.id id_sub,sv.access_number service_user,m.msisdn,* FROM [PLAYBOY].[dbo].[register] r "
                    + "join [dbo].[subscribe] s on r.mobile_id = s.mobile_id "
                    + "join [dbo].[mobile] m on s.mobile_id = m.mobile_id "
                    + "join [dbo].[services] sv on s.service_id = sv.id "
                    + "join [dbo].[mgr] mg on mg.service_id = r.service_id "
                    + "join [dbo].[api_sms] ap on ap.service_id = r.service_id "
                    + "where convert(varchar(10),s.cdate,110) = convert(varchar(10),dateadd(day,-5,getdate()),110)  "
                    + "and s.description = 'REG' and s.sub_status = 30 and mg.operator_id = '3' and ap.mt_type = 'WARNING' and r.status_code ='000'");
            while (rs.next()) {
                if (rs.getString("service_user").equals("4557555") || rs.getString("service_user").equals("4557777")) {
                    id_user = rs.getString("id_sub");
                    data_sms iduser = new data_sms();
                    //rs.getString("service_id")
                    iduser.setService_id("7112409000");
                    iduser.setNumber(rs.getString("msisdn"));
                    //rs.getString("access_number")
                    iduser.setAccess("4557001");
                    iduser.setText_sms(rs.getString("mt_msg"));
                    iduser.setCode(rs.getString("status"));
                    String user = "7112409000:H84pL9aG";
                    iduser.setEncoding(user);
                    user_data.add(iduser);
                }
            }
            this.Log.info("id_user : " + id_user);
            String sql = "UPDATE subscribe SET sub_status = '40' WHERE id ='" + id_user + "' ";
            stmt.executeUpdate(sql);
            conn.close();
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
