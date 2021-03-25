package com.example.incubatorui.GetJPEG;

public class MergeByte {

    /**
     * 合并字节数组
     * @param byte1  数组1
     * @param byte2  数组2
     * @param len  合并数组2的有效长度
     * @return
     */
    public static byte[] mergeByte(byte[] byte1,byte[] byte2,int len){
        if(byte1 ==null)
        {
            if(byte2 ==null)
            {
                return null;
            }else {
                return byte2;
            }

        }
        if(byte2 ==null)
        {
            return byte1;
        }
        byte[] byte3 = new byte[byte1.length + len];
        System.arraycopy(byte1,0,byte3,0,byte1.length);
        System.arraycopy(byte2,0,byte3,byte1.length, len);
        return byte3;
    }

}
