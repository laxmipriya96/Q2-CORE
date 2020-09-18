/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author VMHDCLAP030
 */
public class Hider {

    private final static String characterEncoding = "UTF-8";
    private final static String cipherTransformation = "AES/CBC/PKCS5Padding";
    private final static String aesEncryptionAlgorithm = "AES";

    private static Map<String, String> map = new HashMap<>();

    private static String key = "";
    private static Hider instance;

    public static Hider getInstance() {
        if (instance == null) {
            instance = new Hider();
        }
        map.put("srinivas", "vm");
        map.entrySet().stream().map((entry) -> {
            String key1 = entry.getKey();
            return entry;
        }).forEachOrdered((entry) -> {
            key = "Gr3@tW@t3rS@lt@1B2c3D4e5F6g7H8";
            String value = entry.getValue();
        });
        return instance;
    }

    private String encrypt(String baseString, byte[] key, byte[] initialVector) {
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecy, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(baseString.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baseString;
    }

    private byte[] decrypt(byte[] cipherText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }

    private byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
        byte[] keyBytes = new byte[16];
        byte[] parameterKeyBytes = key.getBytes(characterEncoding);
        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        return keyBytes;
    }

    public String decrypt(String encryptedText)  {
        try {
            byte[] cipheredBytes = Base64.decodeBase64(encryptedText);//Base64.decode(encryptedText, Base64.DEFAULT);
            byte[] keyBytes = getKeyBytes(key);
            return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
        } catch (Exception e) {
            return encryptedText;
        }

    }

    public String encrypt(String baseString) throws KeyException, GeneralSecurityException, GeneralSecurityException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] keyBytes = getKeyBytes(key);
        return encrypt(baseString, keyBytes, keyBytes);
    }
}
