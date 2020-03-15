package com.oneplat.oap.mgmt.util.auth;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

public class RsaModule {

	private PublicKey rsaPublicKey;
	private Cipher cipher;
	private String encodingType = "UTF-8";

	public RsaModule() {
		try {
			BigInteger modulus = new BigInteger("16731853446012880701971111305217108648316492130365022365150269333362563480432849651263026505273276721026627833257770022748234165426982275766525390723761809403507004976847766834788455595202971134756328441371699169058444262425110528246026070131563757787891027617585208903392098203843100236557475223145794878971250081628008851481291127962329098475902418196175524870916995583159426082531030549463182616822121732263775518658580124373290571459417827585948605928157923110045905106575209071872945878212913594868249579677916456876314083128525910952868996736312680810171481133284251207724369348337505130329265918857353659854951");
			BigInteger publicExponet = new BigInteger("65537");
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponet);

			rsaPublicKey = keyFactory.generatePublic(keySpec);
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String encryption(String message) {
		// TODO Auto-generated method stub
		String result = null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
			byte[] data = cipher.doFinal(message.getBytes(encodingType));
			result = new String(Base64.encodeBase64(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
