package com.dhsy.async.utils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;

public class RSAUtils {

	private static final String RSA_ALGORITHM = "RSA";
	
//	public static void main(String[] args) {
//		try {
//			// 测试用公钥加密
//			TreeMap<String, Object> params = new TreeMap<String, Object>();// TreeMap 自带排序
//			params.put("id", 95733900);// 会员id
////			params.put("changeBalance", 233);// 充值余额
//			String test = sign(params);// 生成签名
//			System.out.println(String.format("生成的签名：%s", test));
//			// 测试用私钥解密
////			String decryptStr = Decrypt(test);
////			System.out.println(String.format("解密后：%s", decryptStr));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//
//	}

	/**
	 * 生成签名
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sign(TreeMap<String, Object> params)  {
		// TreeMap<String,Object> 按字典排序 组成 {参数1}=参数值1&{参数2}=参数值2 的字符串 再对该字符串进行加密
		if (params.containsKey("sign"))
			params.remove("sign");
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return RSAStr(sb.toString());
	}

	/**
	 * 公钥加密
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private static String RSAStr(String str)  {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(ClientConfig.KINGDEE_PUBLIC_KEY));
			RSAPublicKey pubkey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);
			byte[] b1 = cipher.doFinal(str.getBytes("UTF-8"));
			return new String(Base64.getEncoder().encode(b1));
			
		} catch (Exception e) {
			return null;
		}
	}

	
}