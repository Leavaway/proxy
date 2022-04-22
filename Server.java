// Java server program for ANU's comp3310 sockets Lab 
// Peter Strazdins, RSCS ANU, 03/18

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    static int port = 3310;
    static Logger logger = Logger.getLogger("web_proxy");
    public static void run() throws IOException {
        ServerSocket sSock = new ServerSocket(port);
        System.out.println("server: created socket with port number " +
			   sSock.getLocalPort()); 

        Socket sock = sSock.accept();
        System.out.println("server: received connection from "   +
                           sock.getRemoteSocketAddress() + ", " +
                           "now on port " + sock.getLocalPort());

        //Receive the request from the browser and find out the method of the request.
        InputStreamReader inputStreamReader = new InputStreamReader(sock.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        logger.log(Level.INFO,"The timestamp is: " + new Date().toString());
        logger.log(Level.INFO,"client-request: "+line);

        if (line!=null){
            //transfer the info of request to client and write the result back to browser.
            OutputStream outputStream = sock.getOutputStream();
            DataOutputStream proxyToBroswer = new DataOutputStream(outputStream);
            proxyToBroswer.write(Client.run(line));
            proxyToBroswer.flush();
        }

        sock.close();
        sSock.close();
        System.out.println("server: This connection closed sockets and terminating");
    }

    public static void main(String[] args) throws IOException {

        while (true){
            run();
        }
    }
}//Server
