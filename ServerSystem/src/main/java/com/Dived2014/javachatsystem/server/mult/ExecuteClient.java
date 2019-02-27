package com.Dived2014.javachatsystem.server.mult;/*
 *国伟
 *2019/2/24
 *9:15
 *
 *
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ExecuteClient implements Runnable {


    private static final Map<String, Socket> ONLINE_USER_MAP
            = new ConcurrentHashMap<String, Socket>();

    private final Socket client;

    public ExecuteClient(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            InputStream clientInput = this.client.getInputStream();
            Scanner sc = new Scanner(clientInput);
            while (true) {
                String data = sc.nextLine();
                /**
                 * userName:<name>
                 * private:<name>:<message>
                 * group:<message>
                 * bye
                 */
                if (data.startsWith("userName")) {
                    String userName = data.split("\\:")[1];
                    if (data.split("\\:")[0].equals("userName")) {
                        this.register(userName, client);
                        continue;
                    }
                }
                if (data.startsWith("private")) {
                    String[] segments = data.split("\\:");
                    if (segments[0].equals("private")) {
                        String userName = segments[1];
                        String message = segments[2];
                        this.privateChat(userName, message);
                        continue;
                    }
                }
                if (data.startsWith("group")) {
                    String message = data.split("\\:")[1];
                    if (data.split("\\:")[0].equals("group")) {
                        this.groupChat(message);
                        continue;
                    }
                }
                if (data.startsWith("help")) {
                    this.helpInfo();
                    continue;
                }
                if (data.startsWith("bye")) {
                    if (data.split("\\:")[0].equals("bye")) {
                        this.quit();
                        break;
                    }
                }
                sendMessage(this.client, "Error:undefined command!\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void helpInfo() {
        sendMessage(this.client,"Supported command");
        sendMessage(this.client,"userName:<name>");
        sendMessage(this.client,"private:<name>:<message>" );
        sendMessage(this.client,"group:<message>");
        sendMessage(this.client,"bye");
    }

    private void printOnlineUser() {
        System.out.println("CurrentOnlineUserNum :" + ONLINE_USER_MAP.size());
        System.out.println("UserList:");
        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private boolean isUserexist(String username) {
        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            if (entry.getKey().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void sendMessage(Socket socket, String message) {
        try {
            OutputStream clientOutput = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUsername() {
        String currentUserName = "";
        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            if (this.client.equals(entry.getValue())) {
                currentUserName = entry.getKey();
                break;
            }
        }
        return currentUserName;
    }

    private void quit() {
        String currentUserName = this.getCurrentUsername();
        System.out.println("Offline:" + currentUserName);
        Socket socket = ONLINE_USER_MAP.remove(currentUserName);
        this.sendMessage(socket, "bye\n");
    }

    private void groupChat(String message) {
        String currentUserName = this.getCurrentUsername();

        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            if (!entry.getKey().equals(currentUserName)) {
                this.sendMessage(entry.getValue(), currentUserName
                        + " Send a Full Server Message:" + message + "\n");
            }
        }
    }

    private void privateChat(String userName, String message) {
        String currentUserName = this.getCurrentUsername();
        Socket target = ONLINE_USER_MAP.get(userName);

        if (target != null) {
            this.sendMessage(target, currentUserName +
                    " said to you:" + message + "\n");
        }
    }

    private void register(String userName, Socket client) {
        if (!isUserexist(userName)) {
            System.out.println(userName + " Register Success!"
                    + client.getRemoteSocketAddress());
            ONLINE_USER_MAP.put(userName, client);
            printOnlineUser();
            sendMessage(this.client, "Register Success!" + "\n");
        } else {
            sendMessage(this.client, "Error:Username Had already Existed!\n");
        }

    }

}
