package com.oneplat.oap.mgmt.util.auth;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;
import java.security.spec.KeySpec;

public class DesModule {

	private Key key;

	private Cipher cipher;

	private String encodingType;

	private KeySpec keyspec;

	//type : DES, DESede
	public DesModule(String keyString ,String type) {
		// TODO Auto-generated constructor stub

		//인코딩 방식 설정
		encodingType = "UTF-8";

		try {
			//인수로 받은 key 값으로 DES 암호화 key 생성
			if(type.equals("DES")) keyspec = new DESKeySpec(keyString.getBytes(encodingType));
			else keyspec = new DESedeKeySpec(keyString.getBytes(encodingType));

			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(type);//DESede
			key = secretKeyFactory.generateSecret(keyspec);

			// DES의 경우 64비트가 한 블럭을 형성
			// 입력한 데이터가 64비트가 안되면 length 에러 발생
			// Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
			// PKCS5Padding으로 설정 하면 64비트가 아니어도 정상적으로 처리
			// 불록크기의 배수만큼 나머지를 채워줌
			cipher = Cipher.getInstance(type+"/ECB/PKCS5Padding");//DESede/ECB/PKCS5Padding
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 암호화
	public String encryption(String message) {
		// TODO Auto-generated method stub
		String result = null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] data = cipher.doFinal(message.getBytes(encodingType));
			result = new String(Base64.encodeBase64(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}