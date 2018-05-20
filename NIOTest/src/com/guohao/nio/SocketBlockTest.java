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
 * NIO��������
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
public class SocketBlockTest {
	
	@Test
	public void server() throws IOException{
		//���������Socketͨ�������󶨱�����9999�˿�
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress("localhost", 9999));
		System.out.println("�ȴ��ͻ�������.......");
		//��ȡ�ͻ��˵�����
		SocketChannel sChannel = ssc.accept();
		System.out.println("���ֿͻ�������");
		//�ֽڻ�����
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//����һ���ļ�ͨ�������յ����ļ�ͨ����ͨ�����д洢
		FileChannel fChannel = FileChannel.open(Paths.get("DSC02003_recv.JPG"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		//�������ݲ��洢
		while(sChannel.read(buf) != -1){
			buf.flip();
			fChannel.write(buf);
			buf.clear();
		}
		System.out.println("������������ɣ���ʼ�������ָ��");
		//������ɺ���ͻ��˷������ָ��
		buf.put("�������".getBytes());
		buf.flip();
		sChannel.write(buf);
		System.out.println("���ָ���ѷ��ͣ��ر�����");
		//�ر�ͨ��
		ssc.close();sChannel.close();fChannel.close();
	}

	@Test
	public void client() throws IOException{
		//��������localhost 9999��Socket����ͨ��
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("localhost",9999));
		System.out.println("�����ӵ�localhost 9999");
		//�������ӱ����ļ����ļ�ͨ��
		FileChannel fChannel = FileChannel.open(Paths.get("DSC02003.JPG"), StandardOpenOption.READ);
		//����������
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//ѭ��ͨ������ͨ��д��Ŀ���ַ
		System.out.println("��ʼ��������");
		while(fChannel.read(buf) != -1){
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		//SocketChannel������Ҫ���������ˣ����Ҫ�ر�output
		sChannel.shutdownOutput();
		System.out.println("�ͻ��˷�����ɣ��ȴ������������ָ�����������");
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
