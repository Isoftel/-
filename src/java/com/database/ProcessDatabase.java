package com.database;

import com.xml.Post_XML;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class ProcessDatabase {

    Logger Log = Logger.getLogger(this.getClass());

    ResourceBundle msg = ResourceBundle.getBundle("configs");
    Post_XML xml = new Post_XML();
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String user = msg.getString("user");
    String pass = msg.getString("pass");
    String url = msg.getString("true_url");

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public PrintWriter request_printwriter(PrintWriter out, String encoding, String get_string) {

        out.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");

//        <?xml version="1.0" encoding="UTF-8"?>
//<message id="routerTestbed@Testbed:3104400">
//<rsr type="reply">
//<service-id>0101102156</service-id>
//<destination messageid="6156634A">
//<address>
//<number type="abbreviated">1042</number>
//</address>
//</destination>
//<source>
//<address>
//<number type="international">668xxxxxxxx</number>
//</address>
//</source>
//<rsr_detail status="success">
//<code>0</code>
//<description>Success receive request</description>
//</rsr_detail>
//</rsr>
//</message>
        return out;
    }

    public String ProcessDatabase(String result, PrintWriter out) {
        String sql = null;
        this.Log.info("Get Xml true : " + result);

        String sms = (getdata(result, "sms type=\"", 3, ""));
        String ud = (getdata(result, "ud type=\"text\"", 4, "ud"));
        if (sms.equals("mo")) {
            if (ud.equals("R")) {

            }
            if (ud.equals("C")) {

            }
        }

        String encoding = (getdata(result, "?xml version=\"1.0\" encoding=\"", 2, ""));
        String message = (getdata(result, "message id=\"", 3, ""));
        //String sms = (getdata(result, "sms type=\"", 3, ""));
        String messageid = (getdata(result, "destination messageid=\"", 3, ""));
        String destination = (getdata(result, "number type=\"abbreviated\"", 4, "number"));
        String number = (getdata(result, "number type=\"international\"", 4, "number"));
        //String ud = (getdata(result, "ud type=\"text\"", 4, "ud"));
        String time = (getdata(result, "scts", 1, "scts"));
        String service = (getdata(result, "service-id", 1, ""));
        String from = (getdata(result, "from", 1, ""));
        String to = (getdata(result, "to", 1, ""));

        if (ud.equals("R")) {
            ud = "REG";
        } else if (ud.equals("C")) {
            ud = "UNREG";
        }

        //System.out.println(" 1 " + encoding + " 2 " + sms + " 3 " + service + " 4 " + destination + " 5 " + number + " 6 " + ud + " 7 " +time);
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();

            sql = "INSERT INTO delivery_request(TransactionID,product_id,MSISDN,Content,service_id) "
                    + "VALUES ('" + message + "','" + messageid + "','" + number + "','" + ud + "','" + time + "')";
            stmt.execute(sql);

            int check_number = 0;
            int id_number = 0;
            int id_service = 0;

            //////////// mobile ดูว่ามีเบอร์แล้วหรือยังมี ดึง ID ไม่มีให้ INSERT
            sql = "select * from mobile";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (number.equals(rs.getString("msisdn"))) {
                    check_number = 1;
                    id_number = rs.getInt("mobile_id");
                }
            }
            if (check_number == 0) {
                sql = "INSERT INTO mobile(msisdn, cdate, operator_id, udate) "
                        + "VALUES('" + number + "',time,'3',time)";
                stmt.execute(sql);

                sql = "select * from mobile where msisdn = '" + number + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    id_number = rs.getInt("mobile_id");
                }
            }

            //////////////////services หา ID บริการ
            sql = "select * from services where service_id = '" + service + "' ";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id_service = rs.getInt("id");
            }

            //////////////////subscribe เช็คสมัครแล้วหรือยัง
            String id_subscribe = "non";
            sql = "select * from subscribe where service_id = '" + service + "' and mobile_id = '" + id_number + "' ";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id_subscribe = rs.getString("description");
            }

            //////////////////register  non=ยังมีการทำรายการในบริการนั้น | UNREG เคยสมัคร ต้อง UPDATE | REG ส่งข้อความกลับไปแล้วสมัครแล้ว
            Date cdate = dateFormat.parse(time);
            if (id_subscribe.equals("non")) {
                sql = "INSERT INTO subscribe(mobile_id, service_id, description, cdate) "
                        + "VALUES('" + id_number + "','" + id_service + "','REG','" + cdate + "')";
                stmt.execute(sql);
            } else if (id_subscribe.equals("UNREG")) {
                sql = "UPDATE subscribe SET description = 'REG',udate = '" + cdate + "' WHERE id='" + id_subscribe + "' ";
                stmt.executeUpdate(sql);
            } else if (id_subscribe.equals("REG")) {
                /// ส่งกลับทันที
            }

            sql = "INSERT INTO register(api_req, reg_channel, mobile_id, service_id, reg_date, status) "
                    + "VALUES('" + ud + "','SMS','" + id_number + "','" + id_service + "','" + cdate + "','0')";
            stmt.execute(sql);

//            String sql = "UPDATE register SET status = '3' WHERE reg_id='" + id + "' ";
//            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            this.Log.info("Errir SQl : " + e);
            //System.out.println("Error SQL : " + e);

        }
        return result;
    }

    public String getdata(String in, String Tag, int ifroob, String back) {
        StringBuilder sb = new StringBuilder();
        String result = null;
        try {
            String document = in;
            String startTag = "";
            String endTag = "";
            if (ifroob == 1) {
                startTag = "<" + Tag + ">";
                endTag = "</" + Tag + ">";
            } else if (ifroob == 2) {
                startTag = "<" + Tag;
                endTag = "\"?>";
            } else if (ifroob == 3) {
                startTag = "<" + Tag + "\"";
                endTag = "\">";
            } else if (ifroob == 4) {
                startTag = "<" + Tag + ">";
                endTag = "</" + back + ">";
            }
            int start = document.indexOf(startTag) + startTag.length();
            int end = document.indexOf(endTag);
            result = document.substring(start, end);
        } catch (Exception ex) {
            //System.out.println("error : "+ex.getMessage());
            return result;
        }
        return result;
    }

}
