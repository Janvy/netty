package com.janvy.client.bio;

import java.io.IOException;
import java.net.Socket;

/**
 * @Author: liuweijie
 * @CreateDate: 2021/3/19 10:57
 * @Version: 1.0
 * @Description:
 */
public class BIOClientSocket {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",9000);
        //向服务端发送消息
        socket.getOutputStream().write("Hello Server".getBytes());
        socket.getOutputStream().flush();
        System.out.println("读取服务端数据...");

        byte[] bytes = new byte[1024];
        int read = socket.getInputStream().read(bytes);
        System.out.println("服务端数据:"+new String(bytes,0,read));
        socket.close();

    }
}
