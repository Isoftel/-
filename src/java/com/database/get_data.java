package com.database;

//import java.util.Base64;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.table_data.Responsed;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.table_data.data_user;
import com.xml.PostXML;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import sun.nio.cs.StandardCharsets;

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
        this.Log.info("Test");
        for (data_user r : id_user_port) {
            byte[] b = id_user_port.get(0).getNumber_type().getBytes(Charset.forName("UTF-8"));
            String encode = new sun.misc.BASE64Encoder().encode(b);
            try {
                String RegXML = xml.getXmlReg(id_user_port.get(0).getEncoding(), id_user_port.get(0).getSms_type(), id_user_port.get(0).getService_id(), id_user_port.get(0).getNumber_type(), id_user_port.get(0).getAccess(), id_user_port.get(0).getSender(), id_user_port.get(0).getSms(), id_user_port.get(0).getOper(), encode);
                
                System.out.println("Post Xml : " + RegXML);
                String GetXML = xml.PostXml(RegXML, post_xml_true,encode);
                
                System.out.println("Get Xml Test : " + GetXML);
                this.Log.info("Get Xml : " + GetXML);
            } catch (Exception e) {

            }
        }
        if (id_user_port.size() > 0) {

        }

    }

    public List<data_user> ProcessRegister_one() {
        user_room.clear();

        try {
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select TOP(1)* from register "
                    + "INNER JOIN subscribe ON subscribe.mobile_id = register.mobile_id "
                    + "INNER JOIN services  ON services.id      = register.service_id "
                    + "where status = '10'");
            while (rs.next()) {
                data_user iduser = new data_user();
//                String user = rs.getString("api_user");
//                String pass = rs.getString("api_password");
                String en = rs.getString("api_req");
                String sms_type = rs.getString("api_req");
                String service = rs.getString("service_id");
                String number = rs.getString("Product_ID");
                String access = rs.getString("access_number");
//                String sender = "True";
//                String text = "test have sender TrueMove ";
//                String oper = "True";

                if (en.equals("T")) {
                    iduser.setEncoding("TIS-620");
                } else if (en.equals("E")) {
                    iduser.setEncoding("UTF-8");
                } else if (en.equals("H")) {
                    iduser.setEncoding("HEX");
                }
                iduser.setSms_type(sms_type);
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setAccess(access);
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

    public List<data_user> ProcessRegister() {
        user_room.clear();
        System.out.println("GG");
        try {
            System.out.println("");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            local = "192.168.50.11";
            local ="27.100.44.80,1133";
             String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
             conn = DriverManager.getConnection(connectionUrl);
             
//            SQLServerDataSource ds = new SQLServerDataSource();
//            ds.setUser("isfotel");
//            ds.setPassword("isoftelthailand");
//            ds.setServerName("local");
//            ds.setPortNumber(1133);
//            ds.setDatabaseName("PLAYBOY");
//            conn = ds.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from register");
            while (rs.next()) {
                data_user iduser = new data_user();
                String id = rs.getString("reg_id");
                //System.out.println("Test ID : " + id);

                user_room.add(iduser);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
//        int gg = user_room.size();
//        String gg2 = user_room.get(0).getApi_job();
        return user_room;
    }

}
