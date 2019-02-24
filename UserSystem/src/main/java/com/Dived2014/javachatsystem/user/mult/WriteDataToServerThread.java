package com.Dived2014.javachatsystem.user.mult;/*
 *国伟
 *2019/2/24
 *10:08
 *
 *
 *
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteDataToServerThread extends Thread {

    private final Socket client;

    public WriteDataToServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            OutputStream clientOutput = client.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            Scanner sc = new Scanner(System.in);
            while(true){
                System.out.println("MsgInput:>");
                String message = sc.nextLine();
                writer.write(message+"\n");
                writer.flush();
                if(message.equals("bye")){
                    client.close();
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
