package com.asm.jelly;

import org.junit.Test;

import java.io.*;

/**
 * @author dongxiaohong
 * @date 2018/12/14 16:47
 */
public class JellyAsmBootTest {
    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;

    /**
     * class文件的魔数、主版本和小版本的大小分别为4、2、2字节。因此，
     * (1)第一步是求出常量池的数据以及大小。
     *
     * */
    @Test
    public void readClass() throws IOException {
        byte[] data = new byte[INPUT_STREAM_DATA_CHUNK_SIZE];
        String name = "java.lang.Runnable";
        InputStream in = ClassLoader.getSystemResourceAsStream(name.replace(".", "/") + ".class");
        try {
            ByteArrayOutputStream ous = new ByteArrayOutputStream();
            int byteRead;
            while ((byteRead = in.read(data, 0, data.length)) != -1) {
                ous.write(data, 0, byteRead);
            }
            ous.flush();
            //读取出来的bytes
            byte[] bytes = ous.toByteArray();
            int offset = 8;
            //开始解析bytes数据,魔数、major_version和minor_version不用解析了
            //首先解析的是常量池大小,class文件的第9、10位为常量池大小记录值，第9位为高位，第10位为低位
            int constantPoolSize = ((bytes[offset]&0xFF)<<8) | (bytes[offset+1]&0xFF);
            //解析常量池,从class文件的第10字节开始解析,解析constantPoolSize次
            //cpInfoOffsets用来记录constantPoolSize个常量池的偏移量.第一个偏移量为size(magic)+size(major_version)+size(minor_version)+size(constantPoolSize)+1=4+2+2+2+1=11,也就是说从第11字节首先开始的是常量池
            int[] cpInfoOffsets = new int[constantPoolSize];
            int currentCpInfoOffset = 0 + 10;
            int currentCpInfoIndex = 1;
            int currentMaxStringLength = 0;
            while (currentCpInfoIndex < constantPoolSize){
                cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset+1;
                int cpInfoSize;
                switch (bytes[currentCpInfoOffset]){
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
                        cpInfoSize = 3 + (((bytes[currentCpInfoOffset+1]&0xFF)<<8) | (bytes[currentCpInfoOffset+1+1]&0xFF));
                        if (cpInfoSize > currentMaxStringLength) {
                            // The size in bytes of this CONSTANT_Utf8 structure provides a conservative estimate
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
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("异常");
        }finally {
            in.close();
        }
    }
}
