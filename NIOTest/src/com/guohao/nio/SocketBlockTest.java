package com.guohao.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * NIO阻塞测试
 * 一、通道（Channel）：负责连接
 * 	java.nio.channels.Channel(接口)
 * 		|--SelectableChannel
 * 			|--SocketChannel
 * 			|--ServerSocketChannel
 * 			|--DatagramChannel
 * 			|--Pipe.SinkChannel
 * 			|--Pipe.SourceChanne;
 * 二、缓冲区（Buffer）：负责数据存取
 * 三、选择器（Selector）：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况
 * 要先启动服务端，再启动客户端
 * @author guoha
 *
 */
public class SocketBlockTest {
	
	@Test
	public void server() throws IOException{
		//创建服务端Socket通道，并绑定本机的9999端口
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress("localhost", 9999));
		System.out.println("等待客户端连接.......");
		//获取客户端的链接
		SocketChannel sChannel = ssc.accept();
		System.out.println("发现客户端连接");
		//字节缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//定义一个文件通道，将收到的文件通过该通道进行存储
		FileChannel fChannel = FileChannel.open(Paths.get("DSC02003_recv.JPG"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		//接收数据并存储
		while(sChannel.read(buf) != -1){
			buf.flip();
			fChannel.write(buf);
			buf.clear();
		}
		System.out.println("服务器接收完成，开始发送完成指令");
		//接收完成后，向客户端发送完成指令
		buf.put("接收完成".getBytes());
		buf.flip();
		sChannel.write(buf);
		System.out.println("完成指令已发送，关闭连接");
		//关闭通道
		ssc.close();sChannel.close();fChannel.close();
	}

	@Test
	public void client() throws IOException{
		//创建连接localhost 9999的Socket网络通道
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("localhost",9999));
		System.out.println("已连接到localhost 9999");
		//创建连接本地文件的文件通道
		FileChannel fChannel = FileChannel.open(Paths.get("DSC02003.JPG"), StandardOpenOption.READ);
		//创建缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//循环通道网络通道写到目标地址
		System.out.println("开始发送数据");
		while(fChannel.read(buf) != -1){
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		//SocketChannel接下来要接收数据了，因此要关闭output
		sChannel.shutdownOutput();
		System.out.println("客户端发送完成，等待服务器的完成指令。。。。。。");
		while(sChannel.read(buf)!= -1){
			buf.flip();
			System.out.println(new String(buf.array(),0,buf.limit()));
			buf.clear();
		}
		fChannel.close();
		sChannel.close();
		fChannel.close();
	}
}
