package com.Dived2014.javachatsystem.user.mult;/*
 *国伟
 *2019/2/24
 *10:04
 *
 *
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class ReadDataFromServerThread extends Thread {
    private final Socket client;

    public ReadDataFromServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        InputStream clientInput;
        try {
            clientInput = client.getInputStream();
            Scanner sc = new Scanner(clientInput);
            while (sc.hasNext()) {
                String message = sc.nextLine();
                System.out.println(message);
                if (message.equals("bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
