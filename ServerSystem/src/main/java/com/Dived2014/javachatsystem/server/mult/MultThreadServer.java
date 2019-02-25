package com.Dived2014.javachatsystem.server.mult;/*
 *国伟
 *2019/2/24
 *9:17
 *
 *
 *
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultThreadServer {

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("Wait connection...");
            while(true){
                Socket client = serverSocket.accept();
                System.out.println("Connecting Success!"+client.getRemoteSocketAddress());
                executorService.submit(new ExcuteClient(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
