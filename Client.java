// Java client program for ANU's comp3310 sockets Lab
// Peter Strazdins, RSCS ANU, 03/18

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class Client {
    static int port = 80;
    static String host = "comp3310.ddns.net";
    static int changes=0;

    public static byte[] run(String method) throws IOException {
        Socket sock = new Socket("comp3310.ddns.net", port);
        System.out.println("client: created socket connected to local port " +
			   sock.getLocalPort() + " and to remote address " +
			   sock.getInetAddress() + " and port " +
			   sock.getPort());

        //Create the http request
        DataOutputStream out = new DataOutputStream(sock.getOutputStream());
        String outMsg = method + "\r\n"
                    + "User-Agent: " + "comp6331\r\n"
                    + "Host: " + host + "\r\n"
                    + "Connection: keep-alive" + "\r\n"
                    + "\r\n";
        out.write(outMsg.getBytes(StandardCharsets.UTF_8));
	    System.out.println("client: sending message: " + outMsg);

        DataInputStream in = new DataInputStream(sock.getInputStream());

        //Deal with the response from destination.
        ArrayList<Byte> res = new ArrayList<>();
        int pos = 0;
        int num = in.read();
        int t = 0;
        StringBuilder status = new StringBuilder();

        StringBuilder checkHTML = new StringBuilder();

        ArrayList<Integer> transfer = new ArrayList<>();
        while (num != -1) {

            if(t<=14){
                if(t>=9){
                    status.append((char) num);
                }
                t++;
            }

            if(num == 116){
                transfer.add(pos);
            }
            res.add((byte) num);
            num = in.read();
            pos++;

            checkHTML.append((char) num);
        }

        Server.logger.log(Level.INFO,"server-status-response: "+status.toString());

        //Check the Content-Type header, if it contains html, then replace "the" in it.
        if(checkHTML.indexOf("text/html")!=-1){
            for (int i = 0; i < transfer.size(); i++) {
                List<Byte> check = res.subList(transfer.get(i)-1,transfer.get(i)+4);
                if(check.get(0)==32&&check.get(1)==116&&check.get(2)==104&&check.get(3)==101&&check.get(4)==32){
                    res.set(transfer.get(i),(byte) 101);
                    res.set(transfer.get(i)+2,(byte) 116);
                    Client.changes++;
                    res.add(transfer.get(i),(byte) 62);
                    res.add(transfer.get(i),(byte) 98);
                    res.add(transfer.get(i),(byte) 60);
                    res.add(transfer.get(i)+6,(byte) 62);
                    res.add(transfer.get(i)+6,(byte) 98);
                    res.add(transfer.get(i)+6,(byte) 47);
                    res.add(transfer.get(i)+6,(byte) 60);
                    for (int j = i; j < transfer.size(); j++) {
                        transfer.set(j,transfer.get(j)+7);
                    }

                }
            }
        }
        int size = res.size();
        byte[] response = new byte[size];
        int i = 0;
        Iterator<Byte> iterator = res.iterator();
        while (iterator.hasNext()) {
            response[i] = iterator.next();
            i++;
        }

        in.close();
        sock.close();
        Server.logger.log(Level.INFO, "The count of changes is: "+String.valueOf(Client.changes));
	    return response;
    }//main()
}//Client
