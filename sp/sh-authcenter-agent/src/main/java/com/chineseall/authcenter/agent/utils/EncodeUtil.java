package com.chineseall.authcenter.agent.utils;

import cn.sh.chineseall.framework.api.random.RandomUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EncodeUtil {

	private static final String charset = "utf-8";
	private static final String KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String key = "ChineseAllShanghai!@#$%^&*()";

	public static String encodePassword(String originPassword) {
		return EncodeUtil.base64Encode(
			EncodeUtil.md5(originPassword + key), "UTF-8").trim();
	}

	/**
	 * 根据指定的字符串生成密钥
	 * @param len 密钥长度;支持128、192、256三种长度,本项目采用128位。
	 * 
	 * @return String 密钥
	 */
	public static String getKey(String key, int len) throws Exception {
		if (len != 128 && len != 192 && len != 256)
			return null;
		key = EncodeUtil.md5(key);
		key = key.substring(0, len / 8);
		byte[] kb = key.getBytes(charset);
		key = EncodeUtil.encodeBase64(kb);

		return key;
	}

	private static Key toKey(byte[] key)
	{
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	public static String encrypt(String data, String key) throws Exception
	{
		//Key k = toKey(Base64.decode(key));
		Key k = toKey(EncodeUtil.decodeBase64(key).getBytes(charset));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		String content = parseByte2HexStr(Base64.encodeBase64((cipher.doFinal(data.getBytes(charset)))));
		return content;
	}
	
	public static String decrypt(String data, String key) throws Exception
	{
		Key k = toKey(EncodeUtil.decodeBase64(key).getBytes(charset));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		String content = new String(cipher.doFinal(Base64.decodeBase64(parseHexStr2Byte(data))), charset);
	
		return content;
	}
	
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}
	/**
	 * 传入字符串，返回一个加密串
	 * 
	 * @param s
	 * @return
	 */
	public static String encode(String s , String key) {
		try {
			byte[] encryptedData = encrypt(s , HEXStringToByte(key));
			s = byteToHEXString(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 传入加密串，返回解密串
	 * 
	 * @param s
	 * @return
	 */
	public static String decode(String s , String key) {
		try {
			return decrypt(HEXStringToByte(s) , HEXStringToByte(key));
		} catch (Exception e) {
		}
		return s;
	}

	/**
	 * 传入字符串，返回一个加密串
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeBase64(String s) {
		byte[] binaryData = null;
		try {
			binaryData = s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
		// byte[] newbt = Base64.encodeBase64(binaryData);

		return encodeBase64(binaryData);// new String(newbt);
	}

	public static String encodeBase64(byte[] binaryData) {
		byte[] newbt = Base64.encodeBase64(binaryData);

		return new String(newbt);
	}

	public static byte[] enBase64(byte[] binaryData) {
		return Base64.encodeBase64(binaryData);
	}

	public static byte[] deBase64(byte[] bytes) throws IOException {
		return Base64.decodeBase64(bytes);
	}

	public static String stringEncode(String str) {
		try {
			return java.net.URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 传入加密串，返回解密串
	 * 
	 * @param s
	 * @return
	 */
	public static String decodeBase64(String s) {
		try {
			return decodeBase64(s.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static String decodeBase64(byte[] bytes) {
		byte[] oldbt = null;
		String t = null;
		try {
			oldbt = Base64.decodeBase64(bytes);
			t = new String(oldbt, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return t;
	}

	public static String byteToHEXString(byte[] bArray) {
		StringBuilder sb = new StringBuilder(100);
		for (int i = 0; i < bArray.length; i++) {
			String hex = Integer.toHexString(bArray[i] & 0xff);
			if (hex.length() == 1) {
				sb.append("0").append(hex);
			} else {
				sb.append(hex);
			}
		}
		return sb.toString().toUpperCase();
	}

	public static byte[] HEXStringToByte(String strString) {
		byte[] ret = new byte[strString.length() / 2];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Integer
					.decode("#" + strString.substring(2 * i, 2 * i + 2))
					.byteValue();
		}
		return ret;
	}

	/**
	 * 加密方法
	 * 
	 * @param rawKeyData
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encrypt(String str , byte [] rawKeyData) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException,
			InvalidKeySpecException, UnsupportedEncodingException {
		// DES算法要求有一个可信任的随机数源
		// SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 现在，获取数据并加密
		byte data[] = str.getBytes("utf-8");
		// 正式执行加密操作
		byte[] encryptedData = cipher.doFinal(data);

		return encryptedData;
	}

	/**
	 * 解密方法
	 * 
	 * @param rawKeyData
	 * @param encryptedData
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(byte[] encryptedData , byte[] rawKeyData)
			throws IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			UnsupportedEncodingException {
		// DES算法要求有一个可信任的随机数源
		// SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 正式执行解密操作
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return new String(decryptedData, "utf-8");
	}

	public String getCurrentMillyTime() {
		return Long.valueOf(System.currentTimeMillis()).toString();
	}

	public static String md5(String content) {
		return DigestUtils.md5Hex(content);
	}


	public static String sha(String content) {
		return DigestUtils.shaHex(content);
	}

	public static String base64Encode(String content, String charset) {
		byte[] bytes = null;
		try {
			bytes = Base64.encodeBase64(content.getBytes(charset), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(bytes);
	}

	public static String base64Decode(String content, String charset) {
		byte[] bytes = null;
		try {
			bytes = Base64.decodeBase64(content.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(bytes);
	}

}
