package g.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author chengjin.lyf on 2018/8/21 下午6:54
 * @since 1.0.25
 */
public class AES {

    public static boolean OPEN_ENCRYPT = false;
    /**
     * 已确认
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static String sKey="1234567890lyf123";

    private static String ivParameter="1234567890lyf123";

    public static String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        byte[] encrypted = encrypt(sSrc.getBytes(encodingFormat), sKey, ivParameter);
        return new BASE64Encoder().encode(encrypted);
    }

    public static byte[] encrypt(byte[] encryptData, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encryptData);
        return encrypted;
    }

    public static byte[] encrypt(byte[] encryptData) throws Exception {
        if (!OPEN_ENCRYPT){
            return encryptData;
        }
       return encrypt(encryptData, sKey, ivParameter);
    }


    public static String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            byte[] original = decrypt(encrypted1, sKey, ivParameter);
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }


    public static byte[] decrypt(byte []encryptData, String sKey, String ivParameter) throws Exception{
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(encryptData);
        return original;
    }

    public static byte[] decrypt(byte []encryptData) throws Exception{
        if (!OPEN_ENCRYPT){
            return encryptData;
        }
        return decrypt(encryptData, sKey, ivParameter);
    }

    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "没有查询到符合条件的记录";
        System.out.println("加密前的字串是："+cSrc);
        // 加密
        String enString = AES.encrypt(cSrc,"utf-8",sKey,ivParameter);
        System.out.println("加密后的字串是："+ enString);

        // 解密
        String DeString = AES.decrypt(enString,"utf-8",sKey,ivParameter);
        System.out.println("解密后的字串是：" + DeString);
    }
}
