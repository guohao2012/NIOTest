package com.guohao.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

/**
 * 分散读取和聚集写入测试类
 * 分散读取：将通道中数据分散到各个缓冲区中
 * 聚集写入：将多个缓冲区的数据聚集到同道中
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
