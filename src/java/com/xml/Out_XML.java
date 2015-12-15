package com.xml;

import java.io.PrintWriter;
import org.apache.log4j.Logger;

public class Out_XML {

    Logger Log = Logger.getLogger(this.getClass());

    public PrintWriter OutXmlr(String encoding, String message, String service, String destination, String number, String text, PrintWriter out) {
        //encoding, message, service, destination, number
        //if(encoding.equals("ISO-8859-1"))
        out.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
        out.println("<message id=\"" + message + "\">");
        out.println("<rsr type=\"reply\">");
        out.println("<service-id>" + service + "</service-id>");
        out.println("<address>");
        out.println("<number type=\"abbreviated\">" + destination + "</number>");
        out.println("</address>");
        out.println("</destination>");
        out.println("<source>");
        out.println("<address>");
        out.println("<number type=\"international\">" + number + "</number>");
        out.println("</address>");
        out.println("</source>");
        out.println("<code>0</code>");
        out.println("<description>" + text + "</description>");
        out.println("</rsr_detail>");
        out.println("</rsr>");
        out.println("</message>");
        this.Log.info("PrintWriter : " + "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"
                + "<message id=\"" + message + "\">"
                + "<rsr type=\"reply\">"
                + "<service-id>" + service + "</service-id>"
                + "<address>"
                + "<number type=\"abbreviated\">" + destination + "</number>"
                + "</address>"
                + "</destination>"
                + "<source>"
                + "<address>"
                + "<number type=\"international\">" + number + "</number>"
                + "</address>"
                + "</source>"
                + "<code>0</code>"
                + "<description>" + text + "</description>"
                + "</rsr_detail>"
                + "</rsr>"
                + "</message>");
        //this.Log.info("PrintWriter : " + out.toString());
        return out;
    }

}
