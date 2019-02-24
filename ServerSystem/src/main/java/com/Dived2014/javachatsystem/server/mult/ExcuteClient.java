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

public class ExcuteClient implements Runnable{


    private static final Map<String,Socket> ONLINE_USER_MAP
            = new ConcurrentHashMap<String, Socket>();

    private final Socket client;
    public ExcuteClient(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            InputStream clientInput = this.client.getInputStream();
            Scanner sc = new Scanner(clientInput);
            while (true){

            String data = sc.nextLine();
            /**
             * userName:<name>
             * private:<name>:<message>
             * group:<message>
             * bye
             */
            if(data.startsWith("userName")){
                String userName =data.split("\\:")[1];
                this.register(userName,client);
                continue;
            }
            if(data.startsWith("private")){
                String[] segments = data.split("\\:");
                String userName = segments[1];
                String message = segments[2];
                this.privateChat(userName,message);
                continue;
            }
            if(data.startsWith("group")){
                String message = data.split("\\:")[1];
                this.groupChat(message);
                continue;
            }
            if(data.startsWith("bye")){
                this.quit();
                break;
            }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printOnlineUser(){
        System.out.println("CurrentOnlineUserNum :"+ONLINE_USER_MAP.size());
        System.out.println("UserList:");
        for(Map.Entry<String,Socket> entry:ONLINE_USER_MAP.entrySet()){
            System.out.println(entry.getKey());
        }
    }

    private void sendMessage(Socket socket,String message){
        try {
            OutputStream clientOutput = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write(message+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUsername(){
        String currentUserName = "";
        for(Map.Entry<String,Socket> entry:ONLINE_USER_MAP.entrySet()){
            if(this.client.equals(entry.getValue())){
                currentUserName =  entry.getKey();
                break;
            }
        }
        return currentUserName;
    }

    private void quit() {
        String currentUserName = this.getCurrentUsername();
        System.out.println("Offline:"+currentUserName);
        Socket socket =  ONLINE_USER_MAP.remove(currentUserName);
        this.sendMessage(socket,"bye");
    }

    private void groupChat(String message) {
        String currentUserName = this.getCurrentUsername();

        for(Map.Entry<String,Socket> entry :ONLINE_USER_MAP.entrySet()){
            if(!entry.getKey().equals(currentUserName)){
            this.sendMessage(entry.getValue(),currentUserName
                    +" Send a Full Server Message:" + message);
            }
        }
    }

    private void privateChat(String userName, String message) {
        String currentUserName = this.getCurrentUsername();
        Socket target = ONLINE_USER_MAP.get(userName);

        if(target!=null){
            this.sendMessage(target,currentUserName+" said to you:"+message);
        }
    }

    private void register(String userName, Socket client) {
        System.out.println(userName + " Register Success!"
                + client.getRemoteSocketAddress());
        ONLINE_USER_MAP.put(userName,client);
        printOnlineUser();
        sendMessage(this.client,"Success!");
    }

}
