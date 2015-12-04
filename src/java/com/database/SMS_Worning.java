package com.database;

import com.table_data.data_sms;
import com.xml.Post_XML;
import com.xml.Set_XML;
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

        for (data_sms r : sms_data) {
            String SmsXML = null;
            String GetXML = null;
            try {
                SmsXML = str_xml.getXmlReg(r.getService_id(), r.getNumber(), r.getAccess(), r.getText_sms(), r.getCode(),"TIS-620");
                GetXML = post_xml.PostXml(SmsXML, msg.getString("ip_mo"), "", "sent");
                //insert_data.insert_worning(GetXML,"SMS");

                //str_xml
            } catch (Exception e) {

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
            rs = stmt.executeQuery("SELECT *  FROM [PLAYBOY].[dbo].[register] r "
                    + "join [dbo].[subscribe] s on r.mobile_id = s.mobile_id "
                    + "join [dbo].[mobile] m on s.mobile_id = m.mobile_id "
                    + "join [dbo].[services] sv on s.service_id = sv.id "
                    + "where convert(varchar(10),reg_date,110) = convert(varchar(10),dateadd(day,-5,getdate()),110) "
                    + "and s.description = 'REG' and r.status = '000'");
            while (rs.next()) {
                id_user = rs.getString("reg_id");
                data_sms iduser = new data_sms();
                iduser.setService_id(rs.getString("service_id"));
                iduser.setNumber(rs.getString("msisdn"));
                iduser.setAccess(rs.getString("access_number"));
                iduser.setText_sms(rs.getString("detail_unreg"));
                iduser.setCode(rs.getString("status"));
                user_data.add(iduser);
            }
            String sql = "UPDATE register SET status_code = '40' WHERE sms_id ='" + id_user + "' ";
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {

        }
        return user_data;
    }
}
