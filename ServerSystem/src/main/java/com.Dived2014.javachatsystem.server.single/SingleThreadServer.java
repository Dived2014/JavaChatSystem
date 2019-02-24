package com.Dived2014.javachatsystem.server.single;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SingleThreadServer{

    public static void main(String[] args) {
        try {
            int port = 6666;
            if(args.length>0){
                try{
                    port = Integer.parseInt(args[0]);
                }catch (NumberFormatException e){
                    System.out.println("Error Input ,Current port:6666");
                }
            }
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("waiting connection....");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection success:"+clientSocket.getRemoteSocketAddress());

            InputStream clientInput = clientSocket.getInputStream();
            Scanner scanner = new Scanner(clientInput);
            String clientData = scanner.nextLine();
            System.out.println("FromServer " + clientData);

            OutputStream clientoutput = clientSocket.getOutputStream();
            OutputStreamWriter writer  = new OutputStreamWriter(clientoutput);
            writer.write("Test2\n");
            writer.flush();

        }catch(IOException e){
            e.printStackTrace();
        }

    }

}