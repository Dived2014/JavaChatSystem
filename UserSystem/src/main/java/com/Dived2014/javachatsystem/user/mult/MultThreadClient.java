package com.Dived2014.javachatsystem.user.mult;/*
 *国伟
 *2019/2/24
 *10:02
 *
 *
 *
 */

import java.io.IOException;
import java.net.Socket;

public class MultThreadClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",6666);

            new WriteDataToServerThread(socket).start();
            new ReadDataFromServerThread(socket).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
