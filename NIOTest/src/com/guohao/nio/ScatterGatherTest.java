package com.guohao.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

/**
 * ��ɢ��ȡ�;ۼ�д�������
 * ��ɢ��ȡ����ͨ�������ݷ�ɢ��������������
 * �ۼ�д�룺����������������ݾۼ���ͬ����
 * @author guoha
 *
 */
public class ScatterGatherTest {
	
	@Test
	public void test() throws IOException{
		RandomAccessFile raf = new RandomAccessFile("1.txt","rw");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(200);
		ByteBuffer[] bufs = {buf1,buf2};
		channel.read(bufs);
		buf1.flip();
		buf2.flip();
		System.out.println(new String(buf1.array(),0,buf1.limit()));
		System.out.println("======================");
		System.out.println(new String(buf2.array(),0,buf2.limit()));

		RandomAccessFile raf2 = new RandomAccessFile("2.txt","rw");
		raf2.getChannel().write(bufs);
	}
}
