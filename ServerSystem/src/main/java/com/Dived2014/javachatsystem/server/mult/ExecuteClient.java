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
import java.sql.*;
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

    Connection connection;

    private final String selectSql = "select fromwhere,towhere,time,message from chatlog where fromwhere =? and towhere =?";
    private final String logInsql = "select username,password from userlist where username =? and password =?";

    public void run() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaChatSystem",
                    "root","Dived2014");
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            InputStream clientInput = this.client.getInputStream();
            Scanner sc = new Scanner(clientInput);
            while (true) {
                String data = sc.nextLine();
                /**
                 * register:<username>:<password>
                 * userName:<name>:<password>
                 * private:<name>:<message>
                 * group:<message>
                 * bye
                 *
                 */
                if (data.startsWith("register")) {
                    String userName = data.split("\\:")[1];
                    String password = data.split("\\:")[2];
                    if (data.split("\\:")[0].equals("register")) {
                        this.register(userName,password ,client);
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


    private void helpInfo() {
        sendMessage(this.client, "Supported command");
        sendMessage(this.client, "Register->userName:<name>");
        sendMessage(this.client, "Direct Message->private:<name>:<message>");
        sendMessage(this.client, "Group Chat->group:<message>");
        sendMessage(this.client, "Leave System->bye");
    }

    private void printOnlineUser() {
        System.out.println("CurrentOnlineUserNum :" + ONLINE_USER_MAP.size());
        System.out.println("UserList:");
        for (Map.Entry<String, Socket> entry : ONLINE_USER_MAP.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private boolean isUserexist(String username) {
        try {
            String registSelectsql = "select username from userslist where username =?";
            PreparedStatement preparedStatement = connection.prepareStatement(registSelectsql);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                resultSet.close();
                preparedStatement.close();
                return true;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void register(String userName,String password, Socket client) {
        if (!isUserexist(userName)) {
            System.out.println(userName + " Register Success!"
                    + client.getRemoteSocketAddress());
//            ONLINE_USER_MAP.put(userName, client);

            String insertSQL = "insert into userslist values(?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1,userName);
                preparedStatement.setString(2,password);
                int ret = preparedStatement.executeUpdate();
                if(ret != 1){
                    sendMessage(this.client,"Error:register failure" + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            printOnlineUser();
            sendMessage(this.client, "Register Success!" + "\n");
        } else {
            sendMessage(this.client, "Error:Username Had already Existed!\n");
        }

    }

}
