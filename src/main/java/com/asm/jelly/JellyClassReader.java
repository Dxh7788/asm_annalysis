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

    /**
     * 获取类名
     * */
    public String getClassName(){
        return readClass(header+2,new char[maxStringLength]);
    }

    /**
     * 获取父类名
     * */
    public String getSuperClassName(){
        return readClass(header+4, new char[maxStringLength]);
    }

    /**
     * 获取接口header+2+2+2+2,最后一个2代表接口的数量
     * */
    public String[] getInterfaces(){
        int interfaceCount = readUnsignedShort(header+2+2+2);
        int step =0 ;
        String[] interfaces = new String[interfaceCount];
        for (int cnt=0 ; cnt<interfaceCount ; cnt++){
            //步长是2个字节
            step += 2;
            interfaces[cnt] = readStringish(header+2+2+2+step ,new char[maxStringLength]).replace("/",".");
        }
        return interfaces;
    }

    public String[] getInterfaces2(){
        int currentOffset = header+2+2+2;
        int interfaceCount = readUnsignedShort(currentOffset);
        String[] interfaces = new String[interfaceCount];
        for (int cnt=0 ; cnt<interfaceCount ; cnt++){
            //步长是2个字节
            currentOffset += 2;
            interfaces[cnt] = readClass(currentOffset,new char[maxStringLength]);
        }
        return interfaces;
    }

    /**
     * 获取接口方式3
     * */
    public String[] getInterfaces3(){
        byte[] classFileBuffer = b;
        //首先获取接口数量
        int currentOffset = header+2+2+2;
        int interfacesCount = readUnsignedShort(currentOffset);
        String[] interfaces = new String[interfacesCount];
        int step = 0;
        for (int i=0;i!=interfacesCount;i++){
            //找出常量池索引
            step += 2;
            int constantPoolIndex = readUnsignedShort(currentOffset+step);
            //找到此常量池处引用的常量池索引
            int constantUtf8ValuesIndex = readUnsignedShort(cpInfoOffsets[constantPoolIndex]);
            String value = constantUtf8Values[constantUtf8ValuesIndex];
            if (value != null){
                interfaces[i] = value;
                continue;
            }
            //生成字符串,找到常量池索引constantUtf8Values`Index的位置
            int utf8Index = cpInfoOffsets[constantUtf8ValuesIndex];
            //找到字节长度
            int utf8Length = readUnsignedShort(utf8Index);
            int currentOffsetIndex = utf8Index+2;
            int charsEndIndex = currentOffsetIndex + utf8Length;
            char[] chars = new char[utf8Length];
            int srcLength = 0;
            while (currentOffsetIndex<charsEndIndex){
                int currentByte = classFileBuffer[currentOffsetIndex++] ;
                //128位以内
                if ((currentByte & 0x80) == 0){
                    chars[srcLength++] = (char)(currentByte & 0x7F);
                }else  if ((currentByte & 0xE0) == 0xC0){//utf-8三字节
                    chars[srcLength++] =
                            (char) (((currentByte & 0x1F) << 6) + (classFileBuffer[currentOffsetIndex++] & 0x3F));
                }else {//unicode,中文4字节问题
                    chars[srcLength++] =
                            (char)
                                    (((currentByte & 0xF) << 12)
                                            + ((classFileBuffer[currentOffsetIndex++] & 0x3F) << 6)
                                            + (classFileBuffer[currentOffsetIndex++] & 0x3F));
                }
            }
            interfaces[i] = new String(chars, 0, srcLength);
        }
        return interfaces;
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


    public static void main(String[] args) throws Exception {
        JellyClassReader reader = new JellyClassReader("World");
        System.out.println("类名为:"+reader.getClassName().replace("/","."));
        System.out.println("父类是:"+reader.getSuperClassName().replace("/","."));
        String[] interfaces = reader.getInterfaces3();
        for (String inf:interfaces){
            System.out.println(inf);
        }
    }

}
