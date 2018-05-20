package com.guohao.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class ChannelTest {

	/***
	 * ֱ�ӻ��������������������������ڴ���
	 * @throws IOException
	 */
	@Test
	public void test1() throws IOException{
		//FileChannel.open(�ļ������ļ����еĲ���)
		FileChannel inChannel = FileChannel.open(Paths.get("DSC02003.JPG"), StandardOpenOption.READ);
		//StandardOpenOption.CREATE û���ļ��򴴽������ļ��򸲸�
		FileChannel outChannel = FileChannel.open(Paths.get("DSC02003_1.JPG"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inChannel.close();
		outChannel.close();
		System.out.println("�ļ��������");
	}
	
	/**
	 * ��ֱ�ӻ���������������������JVM��
	 * @throws IOException
	 */
	public void test2() throws IOException{
		//��ȡ�ļ���
		FileInputStream inStream = new FileInputStream("DSC02003.JPG");
		FileOutputStream outStream = new FileOutputStream("DSC02003_2.JPG");
		//ͨ���ļ�����ȡͨ����channel��
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		//���仺����
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//��ͨ��ȥ�����ݵ�������
		while(inChannel.read(buf) != -1){
			//�л�����ģʽ
			buf.slice();
			//������д�뵽ͨ��
			outChannel.write(buf);
			//����������׼���ٴζ�ȡ
			buf.clear();
		}
		inStream.close();
		outStream.close();
		inChannel.close();
		outChannel.close();
	}
}
