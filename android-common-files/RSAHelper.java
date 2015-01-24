package com.tenpay.envelope.util;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import Decoder.BASE64Decoder;
import android.util.Base64;
import android.util.Log;

public class RSAHelper {

	private static final String publicKeyStr =
			  "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDE4OXYjiY/kS608JzG4TXahDdb" + "\r"
			+ "6xg8aCr+uQw3uZtcoEvHzpO1N9B38R3KARlqUF+aqK8xmwwxIKdYiz2iBq/QEQNw" + "\r"
			+ "XmqVSIY9vMDIP+PVx/phIUwYG2UioAJDnZpGWq5cXj07W/FqHCM7WgDxPKPIwJFr" + "\r"
			+ "56R+NGZGc/F3ameVpQIDAQAB";

	//正式发布时把私钥删除，防止客户端被反编译
	private static final String privateKeyStr =
			  "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMTg5diOJj+RLrTw" + "\r"
			+ "nMbhNdqEN1vrGDxoKv65DDe5m1ygS8fOk7U30HfxHcoBGWpQX5qorzGbDDEgp1iL" + "\r" 
			+ "PaIGr9ARA3BeapVIhj28wMg/49XH+mEhTBgbZSKgAkOdmkZarlxePTtb8WocIzta" + "\r"
			+ "APE8o8jAkWvnpH40ZkZz8XdqZ5WlAgMBAAECgYEAwSgNGPvN1svv0SHIDu9h0LOr" + "\r"
			+ "RIlQy+M/W43dyMZXAPdbofqwvDi4VRTArzGI599XDPu2dqqxuXlZ9esWWuLIlVdT"+ "\r"
			+ "gXt4dhjEnIwBnmpMJ9t21tbLoiW+lT2gfCqLbXSXA0f/yU5wsxz5wRBXhHS/FFDB" + "\r"
			+ "YDMFtTcru7suEhWKNgECQQDqGpsM/m9mpQ1VfYIvoQk+JqK24WcTUZEUkqEklaVO" + "\r"
			+ "6VZLisvyqtZLl3GDot/IN7Z2MyL7xTu1oVLAkrK+UrnBAkEA10r3EanezOcJXd2q" + "\r"
			+ "RO7y46uFmgh9L/vTv2EaZ/qit9arid2pJDqqqVtn0kWuurqIqv/lgrrkYsiqw74k" + "\r"
			+ "oops5QJAJcdFXkNxFs9r52/pomYKZ800VO7rA2MWVouTFaRQmPtuwaIUZ4TKMEiw"+ "\r"
			+ "ON3/3v+eolR+QMMDb7wo7oq97ZjMAQJAbkXXhxSbKhisk4eWTviVMEgKhnsSpCE4" + "\r"
			+ "tC3oeJnH/qHV+yeuGwBxqY1IAEvw03P3zJ6F0BAqKu7diKU73oVIgQJAdt5oimm+" + "\r"
			+ "UCFXJT32vtrprjZ4eGDoxiLHDzM05w3UkmjELKNCnB9UMX+UtI9Cl1umPDRKbtHI"+ "\r"
			+ "YUQXYLDil8DsNw==" ;

	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];


	/**
	 * 公钥解密
	 * @param encode 经过RSA加密和Base64加密后的密文
	 * @param publicKey 公钥
	 * @return 明文
	 * @throws Exception
	 */
	public static String decryptPublicKey(byte[] encode, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decByte=Base64.decode(encode, Base64.DEFAULT);
		byte[] buffers=cipher.doFinal(decByte);
		return new String(buffers, "UTF-8");
	}

	/**
	 * 公钥加密
	 * @param data 明文
	 * @param publicKey 公钥
	 * @return RSA加密后并经过Base64加密后的密文
	 * @throws Exception
	 */
	public static String encryptPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		byte[] bytes = data.getBytes();
		byte[] encode ;
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		encode=cipher.doFinal(bytes);
		encode=Base64.encode(encode, Base64.NO_WRAP);
		return new String(encode);
	}

	/**
	 * 私钥解密
	 * @param encode 经过RSA加密和Base64加密后的密文
	 * @param privateKey 私钥
	 * @return 明文
	 * @throws Exception
	 */
	public static String decryptPrivateKey(byte[] encode,
			RSAPrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decByte=Base64.decode(encode, Base64.DEFAULT);
		byte[] buffers=cipher.doFinal(decByte);
		return new String(buffers);
	}

	public static byte[] clone(final byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	public static byte[] addAll(final byte[] array1, final byte... array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		final byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] subArray(final byte[] array, int startIndexInclusive,
			int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_BYTE_ARRAY;
		}

		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	public static RSAPublicKey getRsaPublicKey() {
		X509EncodedKeySpec keySpec;
		KeyFactory factory;
		RSAPublicKey publicKey = null;
		try {
			keySpec = new X509EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(publicKeyStr));
			factory = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) factory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static RSAPrivateKey getRsaPrivateKey() {
		RSAPrivateKey privateKey = null;
		KeyFactory factory;
		PKCS8EncodedKeySpec keySpec;
		try {
			keySpec = new PKCS8EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(privateKeyStr));
			factory = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return privateKey;
	}

	public static String encryptPublicKey_2(String rowData,
			RSAPublicKey publicKey) throws Exception {
		String result="";
		int key_len = publicKey.getModulus().bitLength() / 8 - 11;
		System.out.println(key_len);
		int count=rowData.length()/key_len;
		int balance=rowData.length()%key_len;
		if(balance!=0){
			count++;
		}
		System.out.println("count--->"+count);
		for(int i=0;i<count;i++){
			String temp;
			if(balance!=0&&i==(count-1)){
				temp=rowData.substring(i*key_len,i*key_len+ balance);
			}else {
				temp=rowData.substring(i*key_len, i*key_len+key_len);
			} 
			result+=new String(encrypt(temp, publicKey));
		}
		return result;
	}
	public static byte[] encrypt(String message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(message.getBytes());
	}
}
