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
 * NIO����������
 * һ��ͨ����Channel������������
 * 	java.nio.channels.Channel(�ӿ�)
 * 		|--SelectableChannel
 * 			|--SocketChannel
 * 			|--ServerSocketChannel
 * 			|--DatagramChannel
 * 			|--Pipe.SinkChannel
 * 			|--Pipe.SourceChanne;
 * ������������Buffer�����������ݴ�ȡ
 * ����ѡ������Selector������SelectableChannel�Ķ�·�����������ڼ��SelectableChannel��IO״��
 * Ҫ����������ˣ��������ͻ���
 * @author guoha
 *
 */
public class SocketNonBlockTest {

	@Test
	public void server() throws IOException{
		//�򿪷����ͨ�������Ұ�9999�˿�
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.configureBlocking(false);
		ssChannel.bind(new InetSocketAddress(9999));
		//��ѡ��������ע��ͨ����accept��connetc�����¼�
		Selector selector = Selector.open();
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("ͨ��ע�����");
		//�м�����ʱ�䷢��
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key.isAcceptable()){
					SocketChannel sChannel = ssChannel.accept();
					sChannel.configureBlocking(false);
					sChannel.register(selector, SelectionKey.OP_READ);
					System.out.println("accept:�пͻ�����������,�ѽ��ͻ���ͨ���Ķ������¼�ע�ᵽѡ����");
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
				//ɾ�������¼�
				it.remove();
			}
		}
		System.out.println("server stop");
	}
	
	@Test
	public void client() throws IOException, InterruptedException{
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("localhost",9999));
		sChannel.configureBlocking(false);
		System.out.println("�����ӵ�localhost9999");
		ByteBuffer buf = ByteBuffer.allocate(1024);
		Thread.sleep(5000);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		int i = 0;
		while(i < 50){
			String msg = ++i+",�ͻ���"+Thread.currentThread().getId()+","+sdf.format(new Date());
			buf.put(msg.getBytes());
			buf.flip();
			sChannel.write(buf);
			buf.clear();
			Thread.sleep(500);
			System.out.println("�ͻ��˷������ݣ�"+msg);
		}
		sChannel.close();
	}
}
