package com.database;

import com.table_data.data_sms;
import com.xml.PostXML;
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
    PostXML xml = new PostXML();
    insert_xml_data insert_r = new insert_xml_data();
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

//        <?xml version="1.0" encoding="ISO-8859-1"?>
//<message id="1243505867213">
//<rsr type="sent">
//<service-id>0101102156</service-id>
//<destination>
//<address>
//<number type="international">668xxxxxxxx</number>
//</address>
//</destination>
//<source>
//<address>
//<number type="abbreviated">1010</number>
//</address>
//</source>
//<rsr_detail status="success">
//<description>Message acknowledged by SMSC</description>
//<code>000</code>
//</rsr_detail>
//</rsr>
//</message>
        for (data_sms r : sms_data) {

            try {

            } catch (Exception e) {

            }
        }

    }

    public List<data_sms> SMS() {
        user_data.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT *  FROM [PLAYBOY].[dbo].[register] "
                    + "join [dbo].[subscribe] s on r.mobile_id = s.mobile_id "
                    + "join [dbo].[mobile] m on s.mobile_id = m.mobile_id "
                    + "join [dbo].[services] sv on s.service_id = sv.id "
                    + "where convert(varchar(10),reg_date,110) = convert(varchar(10),dateadd(day,-5,getdate()),110) "
                    + "and s.description = 'REG' and r.status = '000'");
            while (rs.next()) {
                data_sms iduser = new data_sms();
                iduser.setService_id(rs.getString("service_id"));
                iduser.setNumber(rs.getString("msisdn"));
                iduser.setAccess(rs.getString("access_number"));
                iduser.setText_sms(rs.getString("detail_unreg"));
                iduser.setCode(rs.getString("status"));
                user_data.add(iduser);
            }
            conn.close();
        } catch (Exception e) {

        }
        return user_data;
    }
}
