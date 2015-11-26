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
        this.Log.info("Test found data[ "+id_user_port.size() +"] Records");
        for (data_user r : id_user_port) {
            String encode = "";
            byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
            encode = new sun.misc.BASE64Encoder().encode(b);
            try {
                
                
//                iduser.setService_id(service);
//                iduser.setNumber_type(number);
//                iduser.setDescriptions(descr);
//                iduser.setDetail(detail);
//                iduser.setAccess(access);
//                
//                iduser.setEncoding("7112409001");
                
                String RegXML = xml.getXmlReg(r.getService_id(),r.getNumber_type(),r.getDescriptions(), r.getDetail(), r.getAccess(), encode);

                this.Log.info("Post Xml : " + RegXML);
                String GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode);

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
            rs = stmt.executeQuery("select TOP(100)* from register "
                    + "INNER JOIN subscribe ON subscribe.mobile_id = register.mobile_id "
                    + "INNER JOIN services  ON services.id      = register.service_id "
                    + "where status = '10'");
            while (rs.next()) {
                data_user iduser = new data_user();
//                String user = rs.getString("api_user");
//                String pass = rs.getString("api_password");
                
                
                String service = rs.getString("service_id");
                String number = rs.getString("mobile_id");
                String descr = rs.getString("descriptions");
                String detail = rs.getString("detail_unreg");
                String access = rs.getString("access_number");
                String date = rs.getString("cdate");
//                String sender = "True";
//                String text = "test have sender TrueMove ";
//                String oper = "True";

//                if (en.equals("T")) {
//                    iduser.setEncoding("TIS-620");
//                } else if (en.equals("E")) {
//                    iduser.setEncoding("UTF-8");
//                } else if (en.equals("H")) {
//                    iduser.setEncoding("HEX");
//                }
                
                
                iduser.setService_id(service);
                iduser.setNumber_type(number);
                iduser.setDescriptions(descr);
                iduser.setDetail(detail);
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

    public List<data_user> ProcessRegister() {
        user_room.clear();
        System.out.println("GG");
        try {
            System.out.println("");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            local = "192.168.50.11";
            local = "27.100.44.80,1133";
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
