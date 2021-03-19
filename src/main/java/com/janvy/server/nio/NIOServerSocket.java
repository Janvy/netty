package com.janvy.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: liuweijie
 * @CreateDate: 2021/3/19 11:22
 * @Version: 1.0
 * @Description:
 */
public class NIOServerSocket {
    public static List<SocketChannel> channels = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        //创建NIO ServerSocketChannel与BIO ServerSocket类似
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9000));
        //设置ServerSocketChannel为非阻塞，设置为true时与BIO效果一致
        serverSocketChannel.configureBlocking(false);

        System.out.println("服务启动成功...");

        //进入循环，不断接收客户端连接
        while (true) {
            //接收请求（此时accept为非阻塞）
            SocketChannel socketChannel = serverSocketChannel.accept();
            //由于非阻塞，没有客户端连接时socketChannel为null
            if (socketChannel != null) {
                System.out.println("接收到连接");
                //由于非阻塞的形式接收客户端连接，需要将当前连接的channel保存到集合中。
                socketChannel.configureBlocking(false);
                channels.add(socketChannel);
            }
            //循环判断每个channel是否有数据传输
            if (!channels.isEmpty()) {
                Iterator<SocketChannel> it = channels.iterator();
                while (it.hasNext()) {
                    socketChannel = it.next();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    // 非阻塞模式read方法不会阻塞，否则会阻塞
                    int read = socketChannel.read(byteBuffer);
                    //read == 0时，客户端处于连接，但未发送数据
                    if (read == 0) {

                    } else if (read > 0) {
                        System.out.println("接收消息"+new java.lang.String(byteBuffer.array()));

                    } else if (read == -1) {
                        it.remove();
                        System.out.println("客户端断开连接");
                    }
                }
            }

        }

    }
}
