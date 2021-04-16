import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.SneakyThrows;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.StringTokenizer;

/**
 * @author jujun chen
 * @date 2020/08/02
 */
public class StringTest {

    @Test
    public void test1() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            stringBuilder.append(i);
            stringBuilder.append(";");
        }
        String newStr = stringBuilder.toString();

        //性能第二
        StringTokenizer stringTokenizer = new StringTokenizer(newStr, ";");
        long start = System.currentTimeMillis();
        while (stringTokenizer.hasMoreTokens()) {
            stringTokenizer.nextToken();
        }
        long end = System.currentTimeMillis();
        System.out.println("stringTokenizer:" + (end - start));

        //性能最高
        start = System.currentTimeMillis();
        newStr.split(";");
        end = System.currentTimeMillis();
        System.out.println("split:" + (end - start));


        //无法结束
        String tmp = newStr;
        start = System.currentTimeMillis();
        while (true) {
            int j = tmp.indexOf(";");
            if (j < 0) break;
            tmp.substring(0, j);
            tmp = tmp.substring(j + 1);
        }
        end = System.currentTimeMillis();
        System.out.println("me:" + (end -start));
    }

    /**
     +: 146
     concat: 32
     stringBuilder: 2
     stringBuffer: 1
     */
    @Test
    public void test2() {
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < 10000; i++) {
            str = str + i;
        }
        long end = System.currentTimeMillis();
        System.out.println("+: " + (end - start));


        start = System.currentTimeMillis();
        str = "";
        for (int i = 0; i < 10000; i++) {
            str  = str.concat(String.valueOf(i));
        }
        end = System.currentTimeMillis();
        System.out.println("concat: " + (end - start));

        start = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            stringBuilder.append(i);
        }
        end = System.currentTimeMillis();
        System.out.println("stringBuilder: " + (end - start));


        start = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 10000; i++) {
            stringBuffer.append(i);
        }
        end = System.currentTimeMillis();
        System.out.println("stringBuffer: " + (end - start));
    }


    @SneakyThrows
    @Test
    public void test123() {
        AES aes = SecureUtil.aes("1016e8a7884fadbf".getBytes());
//        System.out.println(new String(aes.decryptStr("âQxP1òn;z®¹\\u0007?Ü\\u0099W".getBytes("ISO-8859-1"))));
//        System.out.println(new String(aes.encrypt("123")));
//        System.out.println(decrypt("âQxP1òn;z®¹\\u0007?Ü\\u0099W".getBytes("ISO-8859-1"), "1016e8a7884fadbf"));
        System.out.println(Base64.decodeStr("U2FsdGVkX1+/zUL2fcTn9mqGqNS7Hk1aQ+ihDcsm2aU="));
    }

    /**
     * AES解密
     * @param encryptStr 密文
     * @param decryptKey 秘钥，必须为16个字符组成
     * @return 明文
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {

        byte[] encryptByte = Base64.decode(encryptStr);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptByte);
        return new String(decryptBytes);
    }

    /**
     * AES加密
     * @param content 明文
     * @param encryptKey 秘钥，必须为16个字符组成
     * @return 密文
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES"));

        byte[] encryptStr = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.encode(encryptStr);
    }

    // 测试加密与解密
    public static void main(String[] args) {
        try {
            String secretKey = "1016e8a7884fadbf";
            String content = "#*451627";
            String s1 = aesEncrypt(content, secretKey);
//            String s1 = "BX12QJHsHdjGIQrvaH5Xyg==";
            System.out.println(s1);
            String s = aesDecrypt(s1, secretKey);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
