package src.main.java.com.Dived2014.javachatsystem.user.single;/*
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
            Socket userSocket = new Socket("127.0.0.1",6666);

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
