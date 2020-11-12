package com.example.gulupoetry.netWork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class GetParams {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String Ksort(Map<String, String> map, String app_key) throws UnsupportedEncodingException {
        String sb2 = "";
        List<String> Key2 = new ArrayList<String>(map.keySet());
        Collections.sort(Key2);
        for (String s : Key2) {
            if (map.get(s) != "") {
                String value = URLEncoder.encode(map.get(s), "UTF-8");
                sb2 += s + "=" + value + "&";
            }
        }
        sb2 = sb2 + "app_key=" + app_key;
        return encodeByMD5(sb2).toUpperCase();
    }

    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] results = md.digest(originString.getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public Map<String, String> returnParams(String str) throws UnsupportedEncodingException {
        String appkey = "5QDzXG56zhTWKQkZ";
        String time = Long.toString(System.currentTimeMillis() / 1000);
        int length = (int) (Math.random() * 32) + 1;
        String randomStr = getRandomString(length);
        Map<String, String> params = new HashMap<String, String>() {
            {
                put("app_id", "2159471923");
                put("speaker", "7");
                put("format", "2");
                put("volume", "0");
                put("speed", "100");
                put("text", str);
                put("aht", "0");
                put("apc", "58");
                put("time_stamp", time);
                put("nonce_str", randomStr);
                put("sign", "");
            }
        };
        String ddd = Ksort(params, appkey);
        params.put("sign", ddd);
        return params;
    }
}