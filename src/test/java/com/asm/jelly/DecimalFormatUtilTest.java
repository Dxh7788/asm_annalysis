package com.asm.jelly;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author dongxiaohong
 * @date 2018/12/27 17:22
 */
public class DecimalFormatUtilTest {

    @Test
    public void test(){
        Double c = null;
        System.out.println(format(c,2));
    }
    /**
     * DecimalFormat线程安全
     * */
    /*private String format(Double money, int scale){
        if (money==null){
            money = 0d;
        }
        StringBuilder patternBuilder= new StringBuilder(",####.");
        for (int i=
             0;i!=scale;i++){
            patternBuilder.append("#");
        }
        String value = new DecimalFormat(patternBuilder.substring(0)).format(money);
        String[] split = value.split(",");
        String unit = "亿元";
        switch (split.length){
            case 1:
                unit = "元";
                break;
            case 2:
                unit = "万元";
                break;
            default:
                break;
        }
        return  new BigDecimal(money).setScale(scale, RoundingMode.HALF_UP)+unit;
    }*/
    /**
     * 默认格式化方式,###.##,1234567890.01==>1,234,567,890.01
     */
    private static String DEFAULT_TEN_THOUSAND_UNIT= "万元";
    private static String DEFAULT_A_HUNDRED_MILLION_UNIT= "亿元";
    private static String DEFAULT_UNIT= "元";
    private static String DEFAULT_DECIMAL_FORMAT_PATTERN = new String(",###.");
    private static DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat(DEFAULT_DECIMAL_FORMAT_PATTERN+"##");
    /**
     * 支持将Double类型转换为xx.xx亿元或者xx.xx万元或者xx.xx元
     * */
    public static String format(Double number, int scale){
        String pattern = ",####.";
        StringBuilder patternBuilder = new StringBuilder(pattern);
        for (int i = 0;i<scale;i++){
            patternBuilder.append("#");
        }
        DEFAULT_DECIMAL_FORMAT.applyPattern(patternBuilder.substring(0));
        if (number == null){
            number = 0d;
        }
        String[] splices = DEFAULT_DECIMAL_FORMAT.format(number).split(",");
        String unit = DEFAULT_A_HUNDRED_MILLION_UNIT;
        int pow = 0;
        switch (splices.length){
            case 1:
                unit = DEFAULT_UNIT;
                break;
            case 2:
                pow = 1;
                unit = DEFAULT_TEN_THOUSAND_UNIT;
                break;
            default:
                pow = 2;
                break;
        }
        return new BigDecimal(number/Math.pow(10000, pow)).setScale(scale, RoundingMode.HALF_UP)+unit;
    }
}
