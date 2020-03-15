package com.oneplat.oap.mgmt.util.auth;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class HmacShaModule {

//	//type : SHA-1, SHA-256
//	public String encryption(String str, String type) {
//		// TODO Auto-generated method stub
//
//		String result = null;
//
//		try {
//			MessageDigest md = MessageDigest.getInstance(type);
//
//			md.update(str.getBytes());
//
//			byte byteData[] = md.digest();
//
//			// convert the byte to hex format method 1
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < byteData.length; i++) {
//				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
//						.substring(1));
//			}
//			result = sb.toString();
//
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}

	//type HmacSHA1  HmacSHA256
	public String encryption(String appKey, String uri, long timestamp, String type) {
		// TODO Auto-generated method stub

		String result = null;
//		String type = "HmacSHA1";
 		//appKey를 가지고 HMAC_SHA1 key를 생성
		SecretKeySpec keySpec = new SecretKeySpec(appKey.getBytes(), type);

		StringBuffer sb = new StringBuffer();
		sb.append(appKey).append("/").append(uri).append("/").append(timestamp);
		try {
			Mac mac = Mac.getInstance(type);
			mac.init(keySpec);
			//데이터가 33%가 커짐
			//사용 이유? 모든 플랫폼에서 전송한 데이터의 깨짐을 막기 위함???
			//바이너리를 포함한 모든 데이터를 아스키로 전송 할수 있기 때문에 파일의 내용을 http 파라미터로 전송하는 등의 용도로 사용
			result = Base64.encodeBase64String(mac.doFinal(sb.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}