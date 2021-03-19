package com.janvy.server.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: liuweijie
 * @CreateDate: 2021/3/19 10:18
 * @Version: 1.0
 * @Description:
 */
public class BIOServerSocket {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);

        while (true) {
            System.out.println("等待客户端连接...");
            //accept阻塞，等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("客户端连接成功...");

            handle(socket);

            /*new Thread(()->{
                try {
                    BIOServerSocket.handle(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();*/
        }
    }

    public static void handle(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("等待客户端数据...");
        int read = socket.getInputStream().read(bytes);
        if (read != -1) {
            System.out.println("客户端数据："+new String(bytes,0,read));
        }
        socket.getOutputStream().write("服务端发来慰问".getBytes());
        socket.getOutputStream().flush();
    }
}
