package com.Dived2014.javachatsystem.user.single;/*
 *国伟
 *2019/2/23
 *10:18
 *
 *
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SingleThreadUser {
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
            String host = "127.0.0.1";
            if(args.length>1){
                host = args[1];

            }

            Socket userSocket = new Socket(host,port);

            OutputStream useroutput = userSocket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(useroutput);
            writer.write("Test\n");
            writer.flush();

            InputStream userInput = userSocket.getInputStream();
            Scanner scanner = new Scanner(userInput);
            String serverData =scanner.nextLine();
            System.out.println("From Server "+serverData);

            userSocket.close();
            userInput.close();
            useroutput.close();
            System.out.println("UserSystem Close");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
