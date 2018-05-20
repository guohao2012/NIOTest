package com.guohao.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

/**
 * NIO非阻塞测试
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
public class SocketNonBlockTest {

	@Test
	public void server() throws IOException{
		//打开服务端通道，并且绑定9999端口
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.configureBlocking(false);
		ssChannel.bind(new InetSocketAddress(9999));
		//打开选择器，并注册通道的accept和connetc监听事件
		Selector selector = Selector.open();
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("通道注册完成");
		//有监听的时间发生
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key.isAcceptable()){
					SocketChannel sChannel = ssChannel.accept();
					sChannel.configureBlocking(false);
					sChannel.register(selector, SelectionKey.OP_READ);
					System.out.println("accept:有客户端连接上来,已将客户端通道的读就绪事件注册到选择器");
				}else if(key.isReadable()){
					ByteBuffer buf = ByteBuffer.allocate(100);
					SocketChannel sChannel = (SocketChannel) key.channel();
					int len = 0;
					while((len = sChannel.read(buf)) > 0){
						buf.flip();
						System.out.println(new String(buf.array(),0,len));
						buf.clear();
					}

				}
				//删除掉该事件
				it.remove();
			}
		}
		System.out.println("server stop");
	}
	
	@Test
	public void client() throws IOException, InterruptedException{
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("localhost",9999));
		sChannel.configureBlocking(false);
		System.out.println("已连接到localhost9999");
		ByteBuffer buf = ByteBuffer.allocate(1024);
		Thread.sleep(5000);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		int i = 0;
		while(i < 50){
			String msg = ++i+",客户端"+Thread.currentThread().getId()+","+sdf.format(new Date());
			buf.put(msg.getBytes());
			buf.flip();
			sChannel.write(buf);
			buf.clear();
			Thread.sleep(500);
			System.out.println("客户端发送数据："+msg);
		}
		sChannel.close();
	}
}
