package com.example.amscopy.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

public class EncryptDecryptUtil {

    private static char[] chars = "0123456789ABCDEF".toCharArray();

    private String privKeyPem = "";

    public PrivateKey getPrivateKey() throws Exception {
        if (StringUtils.isEmpty(privKeyPem)) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("pk.pem");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            privKeyPem = sb.toString();
        }
        byte[] keys = Base64.decodeBase64(privKeyPem.getBytes("UTF-8"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keys);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 用的地方很多,私钥解密
     *
     * @param password
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String password) throws Exception {
        byte[] encryptedDate = Base64.decodeBase64(password.getBytes("UTF-8"));
        PrivateKey privateKey = new EncryptDecryptUtil().getPrivateKey();
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(2, privateKey);
        int inputLen = encryptedDate.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        for (int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedDate, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedDate, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            ++i;
        }
        byte[] decryptedDate = out.toByteArray();
        out.close();
        return new String(decryptedDate, "UTF-8").replaceAll("%22", "");
    }

    public static String decryptPassword(String loginId, String encryptedPassword) throws Exception {
        return loginId + "+" + decryptByPrivateKey(encryptedPassword);
    }

    public static String getEncodedPassword(String loginId, String encryptedPassword) throws Exception {
        return new String(DigestUtils.md5DigestAsHex(decryptPassword(loginId, encryptedPassword).getBytes()));
    }

    public static String byteToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            builder.append(chars[v / 0x10]).append(chars[v % 0x10]);
        }
        return builder.toString();
    }

    public static byte[] hexToByte(String s) {
        if (s == null) return null;
        byte[] bytes = s.getBytes();
        if ((bytes.length % 2) != 0) {
            throw new IllegalArgumentException();
        }
        byte[] b2 = new byte[bytes.length / 2];
        for (int i = 0; i < bytes.length; i += 2) {
            String item = new String(bytes, i, 2);
            b2[i / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    //请求cyberark前，制作请求签名放进head
    public static String makeSign(String s) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(s.getBytes());
        byte[] messageDigest = digest.digest();
        return byteToHex(messageDigest);
    }

    public static String encrypt(String s, String k) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (s == null || k == null) throw new IllegalArgumentException();
        SecretKeySpec key = new SecretKeySpec(k.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(s.getBytes()); //加密
        return byteToHex(result);
    }

    //原用于cyberArk返回的密码解密
    public static String decrypt(String s, String k) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (s == null || k == null) throw new IllegalArgumentException();
        SecretKeySpec key = new SecretKeySpec(k.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(hexToByte(s)); //加密
        return new String(result);
    }


}
