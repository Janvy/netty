package com.janvy.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: liuweijie
 * @CreateDate: 2021/3/19 14:01
 * @Version: 1.0
 * @Description:
 */
public class NIOServerSelectorSocket {

    public static void main(String[] args) throws IOException {
        //创建ServerChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9000));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //打开selector处理channel，即创建epoll，底层为在Linux创建epfd文件描述符
        Selector selector = Selector.open();
        //把ServerSocketChannel注册到Selector上，并且selector对客户端accept链接操作监听
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务已启动...");

        while (true) {
            //阻塞等待需要处理的accept事件发生。
            selector.select();

            //获取selector中注册的全部事件的selectionkey实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            //遍历selectionKey对事件处理
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //如果是OP_ACCEPT事件，则需要连接获取和事件注册
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);

                    //注册channel读事件，如果需要给客户端发送数据，则可以注册写事件
                    channel.register(selector,SelectionKey.OP_READ);
                } else if (key.isReadable()) { //如果是OP_READ事件，则进行读取和打印
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = channel.read(byteBuffer);
                    //read == 0时，客户端处于连接，但未发送数据
                    if (len == 0) {

                    } else if (len > 0) {
                        System.out.println("接收消息"+new java.lang.String(byteBuffer.array()));

                    } else if (len == -1) {
                        channel.close();
                        System.out.println("客户端断开连接");
                    }
                }

            }
            //从事件集合里删除本次处理的key，防止下次select重复处理
            iterator.remove();
        }
    }
}
