package com.asm.util;

/**
 * @author dongxiaohong
 * @date 2018/12/19 16:07
 */
public class EncoderUtil {
    //byte转String
    public String bytes2Str(byte[] datas,int length){
        int offset = 0;
        int end = offset + datas.length;
        char[] value = new char[length];
        int srcLength = 0;
        while (offset<end){
            int currentByte = datas[offset++];
            //纯ASCII码,0-127
            if ((currentByte&0x80)==0){
                value[srcLength++] = (char)(currentByte & 0x7F);
            }else if ((currentByte&0xE0)==0xC0){//2个字节的字符集
                value[srcLength++] =
                        (char) (((currentByte & 0x1F) << 6) + (datas[offset++] & 0x3F));
            }else {//3个字节,中文
                value[srcLength++] =
                        (char)
                                (((currentByte & 0xF) << 12)
                                        + ((datas[offset++] & 0x3F) << 6)
                                        + (datas[offset++] & 0x3F));
            }
        }
        return new String(value, 0, srcLength);
    }

    public static void main(String[] args) {
        EncoderUtil util = new EncoderUtil();
        byte[] datas = "果冻".getBytes();
        String str =util.bytes2Str(datas,4);
        System.out.println(str);
    }
}
