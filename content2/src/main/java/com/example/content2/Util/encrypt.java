package com.example.content2.Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class encrypt {

    private static byte[] md5(String data) throws NoSuchAlgorithmException {
        MessageDigest ms = MessageDigest.getInstance("MD5");
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] digest = ms.digest(bytes);
        return digest;
    }
}
