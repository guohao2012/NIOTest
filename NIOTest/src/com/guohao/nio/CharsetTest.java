package com.guohao.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.junit.Test;


/**
 * 字符集测试
 * 编码：字符串 --> 字节数组
 * 解码：字节数组 --> 字符串
 * @author guoha
 *
 */
public class CharsetTest {

	@Test
	public void charsetTest() throws CharacterCodingException{
		//获取到UTF-8字符集
		Charset utf8 = Charset.forName("utf-8");
		//获取到UTF-8字符集的编码器
		CharsetEncoder encoder = utf8.newEncoder();
		//获取到UTF-8字符集的解码器
		CharsetDecoder decoder = utf8.newDecoder();
		//分配一个字符缓冲区
		CharBuffer cBuffer = CharBuffer.allocate(100);
		//向字符缓冲区放入中文
		cBuffer.put("中华人民共和国");
		//切换成读模式
		cBuffer.flip();
		//将缓冲区按照utf-8编码成字节缓冲区
		ByteBuffer bBuffer = encoder.encode(cBuffer);
		for(int i = 0; i < bBuffer.limit(); i++){
			System.out.println(bBuffer.get());
		}
		System.out.println("========================");

		//切换成读模式
		bBuffer.flip();
		//将bBuffer按照utf-8解码成字符缓冲区
		CharBuffer cBuffer2 = decoder.decode(bBuffer);
		System.out.println(cBuffer2.toString());
	}
}
