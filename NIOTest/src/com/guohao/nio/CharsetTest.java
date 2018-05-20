package com.guohao.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.junit.Test;


/**
 * �ַ�������
 * ���룺�ַ��� --> �ֽ�����
 * ���룺�ֽ����� --> �ַ���
 * @author guoha
 *
 */
public class CharsetTest {

	@Test
	public void charsetTest() throws CharacterCodingException{
		//��ȡ��UTF-8�ַ���
		Charset utf8 = Charset.forName("utf-8");
		//��ȡ��UTF-8�ַ����ı�����
		CharsetEncoder encoder = utf8.newEncoder();
		//��ȡ��UTF-8�ַ����Ľ�����
		CharsetDecoder decoder = utf8.newDecoder();
		//����һ���ַ�������
		CharBuffer cBuffer = CharBuffer.allocate(100);
		//���ַ���������������
		cBuffer.put("�л����񹲺͹�");
		//�л��ɶ�ģʽ
		cBuffer.flip();
		//������������utf-8������ֽڻ�����
		ByteBuffer bBuffer = encoder.encode(cBuffer);
		for(int i = 0; i < bBuffer.limit(); i++){
			System.out.println(bBuffer.get());
		}
		System.out.println("========================");

		//�л��ɶ�ģʽ
		bBuffer.flip();
		//��bBuffer����utf-8������ַ�������
		CharBuffer cBuffer2 = decoder.decode(bBuffer);
		System.out.println(cBuffer2.toString());
	}
}
