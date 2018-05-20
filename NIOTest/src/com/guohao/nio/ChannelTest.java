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
	 * 直接缓冲区，将缓冲区创建到物理内存中
	 * @throws IOException
	 */
	@Test
	public void test1() throws IOException{
		//FileChannel.open(文件，对文件进行的操作)
		FileChannel inChannel = FileChannel.open(Paths.get("DSC02003.JPG"), StandardOpenOption.READ);
		//StandardOpenOption.CREATE 没有文件则创建，有文件则覆盖
		FileChannel outChannel = FileChannel.open(Paths.get("DSC02003_1.JPG"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inChannel.close();
		outChannel.close();
		System.out.println("文件拷贝完毕");
	}
	
	/**
	 * 非直接缓冲区，将缓冲区创建到JVM中
	 * @throws IOException
	 */
	public void test2() throws IOException{
		//获取文件流
		FileInputStream inStream = new FileInputStream("DSC02003.JPG");
		FileOutputStream outStream = new FileOutputStream("DSC02003_2.JPG");
		//通过文件流获取通道（channel）
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		//分配缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//从通道去读数据到缓冲区
		while(inChannel.read(buf) != -1){
			//切换到读模式
			buf.slice();
			//将数据写入到通道
			outChannel.write(buf);
			//清理缓冲区，准备再次读取
			buf.clear();
		}
		inStream.close();
		outStream.close();
		inChannel.close();
		outChannel.close();
	}
}
