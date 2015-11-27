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
import com.xml.PostXML;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
import org.apache.log4j.Logger;

public class get_data implements Runnable {

    Logger Log = Logger.getLogger(this.getClass());
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    PostXML xml = new PostXML();

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

    private List<data_user> id_user_port = ProcessRegister_one();
    //private List<data_user> Reg = ProcessRegister();

    @Override
    public void run() {

        //String U_test = "0101102156:qWACgXb4";
        //System.out.print("Base64 : " + s);
        post_xml_true = "http://192.168.0.126:8080/Artemis/DeliveryRequest_true";
        post_xml_true = "http://10.4.13.39:8004/tmcss2/fh.do";
        post_xml_true = "203.144.187.120:55000";
        this.Log.info("Test found data[ " + id_user_port.size() + "] Records");
        for (data_user r : id_user_port) {
            String encode = "";
            byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
            encode = new sun.misc.BASE64Encoder().encode(b);
            try {
                String RegXML = xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode);
                this.Log.info("Post Xml : " + RegXML);
                String GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode);
                
                System.out.println("Get XML Test : " + GetXML);
                this.Log.info("Get Xml : " + GetXML);
            } catch (Exception e) {
                this.Log.info("Error : " + e);
            }
        }
        if (id_user_port.size() > 0) {
        }
    }

    public List<data_user> ProcessRegister_one() {
        user_room.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            local = "192.168.50.11";
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select TOP(1)*,services.service_id service_user from register "
                    + "INNER JOIN subscribe ON subscribe.mobile_id = register.mobile_id "
                    + "INNER JOIN services  ON services.id  = register.service_id "
                    + "INNER JOIN mobile    ON mobile.mobile_id = register.mobile_id "
                    + "where status = '10'");
            while (rs.next()) {
                data_user iduser = new data_user();

                String service = rs.getString("service_user");
                String number = rs.getString("msisdn");
                String Text_Service = rs.getString("detail_reg");
                // String detail = rs.getString("detail_unreg");
                String access = rs.getString("access_number");
                String date = rs.getString("cdate");

                System.out.println("Sql : " + " 1 " + service + " 2 " + number + " 3 " + Text_Service + " 4 " + access);


                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(Text_Service);
                //iduser.setDetail(detail);
                iduser.setAccess(access);

                iduser.setEncoding("7112409001");
//                iduser.setSms_type(sms_type);
//                iduser.setSender(sender);
//                iduser.setSms(text);
//                iduser.setOper(oper);

                //iduser.set
//                String sql = "UPDATE register SET status = '3' WHERE reg_id='" + id + "' ";
//                stmt.executeUpdate(sql);
                user_room.add(iduser);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        return user_room;
    }

    
    
    /*
    <?xml version="1.0" encoding="ISO-8859-1"?>
<message id="1242878588600">
<rsr type="ack">
<service-id>0101102156</service-id>
<destination messageid="1242878588600">
<address>
<number type="international">668xxxxxxxx</number>
</address>
</destination>
<source>
<address>
<number type="">True Move</number>
</address>
</source>
<rsr_detail status="success">
<code>000</code>
<description>success</description>
</rsr_detail>
</rsr>
</message>
    
    */

}
