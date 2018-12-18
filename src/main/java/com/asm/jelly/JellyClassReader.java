package com.asm.jelly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author dongxiaohong
 * @date 2018/12/14 16:47
 */
public class JellyClassReader {
    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;
    public final int header;

    private final int[] cpInfoOffsets;
    /**字符串最长长度*/
    private final int maxStringLength;

    /**data buffer*/
    public final byte[] b;

    /**常量池*/
    private final String[] constantUtf8Values;


    public JellyClassReader(String name) throws Exception{
        this(
                readStream(
                        ClassLoader.getSystemResourceAsStream(name.replace(".", "/") + ".class"),true));
    }

    private static byte[] readStream(InputStream in,boolean close) throws Exception {
        byte[] data = new byte[INPUT_STREAM_DATA_CHUNK_SIZE];
        try {
            ByteArrayOutputStream ous = new ByteArrayOutputStream();
            int byteRead;
            while ((byteRead = in.read(data, 0, data.length)) != -1) {
                ous.write(data, 0, byteRead);
            }
            ous.flush();
            //读取出来的bytes
            return ous.toByteArray();
        }finally {
            if (close) {
                in.close();
            }
        }
    }
    /**
     * class文件的魔数、主版本和小版本的大小分别为4、2、2字节。因此，
     * (1)第一步是求出常量池的数据以及大小。
     *
     * */
    public JellyClassReader(byte[] classFile) throws IOException {
        b = classFile;
        int offset = 8;
        //开始解析bytes数据,魔数、major_version和minor_version不用解析了
        //首先解析的是常量池大小,class文件的第9、10位为常量池大小记录值，第9位为高位，第10位为低位
        int constantPoolSize = ((classFile[offset]&0xFF)<<8) | (classFile[offset+1]&0xFF);
        //解析常量池,从class文件的第10字节开始解析,解析constantPoolSize次
        //cpInfoOffsets用来记录constantPoolSize个常量池的偏移量.第一个偏移量为size(magic)+size(major_version)+size(minor_version)+size(constantPoolSize)+1=4+2+2+2+1=11,也就是说从第11字节首先开始的是常量池
        cpInfoOffsets = new int[constantPoolSize];
        constantUtf8Values = new String[constantPoolSize];

        int currentCpInfoOffset = 0 + 10;
        int currentCpInfoIndex = 1;
        int currentMaxStringLength = 0;
        while (currentCpInfoIndex < constantPoolSize){
            cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset+1;
            int cpInfoSize;
            switch (classFile[currentCpInfoOffset]){
                case 9:
                case 10:
                case 11:
                case 3:
                case 4:
                case 12:
                    cpInfoSize = 5;
                    break;
                case 17:
                    cpInfoSize = 5;
                    break;
                case 18:
                    cpInfoSize = 5;
                    break;
                case 5:
                case 6:
                    cpInfoSize = 9;
                    currentCpInfoIndex++;
                    break;
                case 1:
                    cpInfoSize = 3 + (((classFile[currentCpInfoOffset+1]&0xFF)<<8) | (classFile[currentCpInfoOffset+1+1]&0xFF));
                    if (cpInfoSize > currentMaxStringLength) {
                        // The size in classFile of this CONSTANT_Utf8 structure provides a conservative estimate
                        // of the length in characters of the corresponding string, and is much cheaper to
                        // compute than this exact length.
                        currentMaxStringLength = cpInfoSize;
                    }
                    break;
                case 15:
                    cpInfoSize = 4;
                    break;
                case 7:
                case 8:
                case 16:
                case 20:
                case 19:
                    cpInfoSize = 3;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            currentCpInfoOffset += cpInfoSize;
        }
        System.out.println("常量池结束位置为："+currentCpInfoOffset);
        header = currentCpInfoOffset;
        maxStringLength = currentMaxStringLength;
    }

    public static void main(String[] args) throws Exception {
        JellyClassReader reader = new JellyClassReader("World");
        System.out.println(reader.getClassName());
    }

    /**
     * 获取类名
     * */
    public String getClassName(){
        return readClass(header+2,new char[maxStringLength]);
    }

    public String readClass(final int offset, final char[] charBuffer) {
        return readStringish(offset, charBuffer);
    }

    private String readStringish(int offset, char[] charBuffer) {
        return readUTF8(cpInfoOffsets[readUnsignedShort(offset)], charBuffer);
    }

    private String readUTF8(int offset, char[] charBuffer) {
        int constantPoolEntryIndex = readUnsignedShort(offset);
        if (offset == 0 || constantPoolEntryIndex == 0){
            return null;
        }
        return readUtf(constantPoolEntryIndex,charBuffer);
    }

    /**如果存在则直接返回,如果不存在则查找*/
    private String readUtf(int constantPoolEntryIndex, char[] charBuffer) {

        String value = constantUtf8Values[constantPoolEntryIndex];
        if (value != null){
            return value;
        }
        int cpInfoOffset = cpInfoOffsets[constantPoolEntryIndex];
        return constantUtf8Values[constantPoolEntryIndex] =
                readUtf(cpInfoOffset + 2, readUnsignedShort(cpInfoOffset), charBuffer);
    }

    private String readUtf(int utfOffset, short utfLength, char[] charBuffer) {
        int currentOffset = utfOffset;
        int endOffset = currentOffset + utfLength;
        int strLength = 0;
        byte[] classFileBuffer = b;
        while (currentOffset < endOffset) {
            int currentByte = classFileBuffer[currentOffset++];
            if ((currentByte & 0x80) == 0) {
                charBuffer[strLength++] = (char) (currentByte & 0x7F);
            } else if ((currentByte & 0xE0) == 0xC0) {
                charBuffer[strLength++] =
                        (char) (((currentByte & 0x1F) << 6) + (classFileBuffer[currentOffset++] & 0x3F));
            } else {
                charBuffer[strLength++] =
                        (char)
                                (((currentByte & 0xF) << 12)
                                        + ((classFileBuffer[currentOffset++] & 0x3F) << 6)
                                        + (classFileBuffer[currentOffset++] & 0x3F));
            }
        }
        return new String(charBuffer, 0, strLength);
    }

    private short readUnsignedShort(int offset) {
        byte[] classFileBuffer = b;
        return (short)(((classFileBuffer[offset]&0xFF) << 8) | (classFileBuffer[offset + 1]&0xFF));
    }

}
