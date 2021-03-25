package com.example.incubatorui.utils.ImageUtils;

public class ByteToHex {
    /**
     * 将指定byte数组以16进制的形式打印到控制台
     * @param b byte[]
     * @return void
     */
    public static void printHexString(byte[] b) {

        //System.out.print(hint+"长度"+b.length);
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");

        }
        System.out.println("");
    }
}
