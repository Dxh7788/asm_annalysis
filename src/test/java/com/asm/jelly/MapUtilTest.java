package com.asm.jelly;

import org.junit.Test;

import java.util.*;

/**
 * @author dongxiaohong
 * @date 2018/12/26 10:00
 */
public class MapUtilTest {
    /**按照key来排序*/
    @Test
    public void orderByKeyTest(){
        Map<Long,String> map = new HashMap<>(4,0.1f);
        /*Map<Long,String> map5 = new HashMap<>(5,0.1f);
        Map<Long,String> map6 = new HashMap<>(6,0.1f);
        Map<Long,String> map7 = new HashMap<>(7,0.1f);
        Map<Long,String> map8 = new HashMap<>(8,0.1f);
        Map<Long,String> map9 = new HashMap<>(9,0.1f);
        Map<Long,String> map10 = new HashMap<>(10,0.1f);
        Map<Long,String> map11 = new HashMap<>(11,0.1f);
        Map<Long,String> map12 = new HashMap<>(12,0.1f);
        Map<Long,String> map13 = new HashMap<>(13,0.1f);
        Map<Long,String> map14 = new HashMap<>(14,0.1f);
        Map<Long,String> map15 = new HashMap<>(15,0.1f);
        Map<Long,String> map16 = new HashMap<>(16,0.1f);*/

        map.put(3L,"ddd");
        map.put(2L,"ddd");
        map.put(4L,"ddd");
        map.put(34L,"ddd");
        /*map.put(16L,"ddd");
        map.put(18L,"ddd");
        map.put(17L,"ddd");
        map.put(19L,"ddd");*/
        /*map.put(10L,"ddd");
        map.put(13L,"ddd");
        map.put(12L,"ddd");*/
        /*map.put(11L,"ddd");
        map.put(9L,"ddd");
        map.put(6L,"ddd");
        map.put(8L,"ddd");
        map.put(5L,"ddd");
        map.put(7L,"ddd");
        map.put(14L,"ddd");
        map.put(15L,"ddd");
        map.put(17L,"ddd");*/
        /*Collections.sort(new ArrayList(map.entrySet()), new Comparator<Map.Entry<Long,String>>() {
            @Override
            public int compare(Map.Entry<Long, String> o1, Map.Entry<Long, String> o2) {
                if (o1.getKey().intValue()-o2.getKey().intValue()>0){
                    return 1;
                }else if (o1.getKey().intValue()-o2.getKey().intValue()<0){
                    return -1;
                }
                return 0;
            }
        });*/
        for (Map.Entry<Long,String> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    @Test
    public void orderByKeyTestString(){
        Map<String,String> map = new HashMap<>(3);
        map.put("3","ddd");
        map.put("2","ddd");
        map.put("4","ddd");
        map.put("1","ddd");
        map.put("16","ddd");
        map.put("10","ddd");
        map.put("13","ddd");
        map.put("12","ddd");
        map.put("11","ddd");
        map.put("9","ddd");
        map.put("6","ddd");
        map.put("8","ddd");
        map.put("5","ddd");
        map.put("7","ddd");
        map.put("14","ddd");
        map.put("15","ddd");
        map.put("17","ddd");

        /*Collections.sort(new ArrayList<>(map.entrySet()), new Comparator<Map.Entry<Long,String>>() {
            @Override
            public int compare(Map.Entry<Long,String> o1, Map.Entry<Long,String> o2) {
                if (o2.getKey().intValue()-o1.getKey().intValue()<0){
                    return -1;
                }else if (o2.getKey().intValue()-o1.getKey().intValue()>0){
                    return 1;
                }
                return 0;
            }
        });*/
        for (Map.Entry<String,String> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }


    /**
     * Long和Integer类型的hash求值为它自身.
     *
     * */
    @Test
    public void longHashTest(){
        Object key = 10010;
        int h;
        System.out.println((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16));
    }

    /**
     * 计算loadFactor
     * */
    @Test
    public void loadFactorTest(){
        int count = 7;
        int oldCnt = count;
        float loadFactor=0;
        count = count-1;
        count |= count>>>1;
        count |= count>>>2;
        count |= count>>>4;
        count |= count>>>8;
        count |= count>>>16;
        count = count+1;
        loadFactor =  (float) oldCnt/count;
        System.out.println(count+1);
        System.out.println(loadFactor);
        Map<Long,String> map = new HashMap<>(count, 0.1f);
        map.put(3L,"ddd");
        map.put(2L,"ddd");
        map.put(4L,"ddd");
        map.put(34L,"ddd");
        map.put(16L,"ddd");
        map.put(18L,"ddd");
        map.put(17L,"ddd");
        for (Map.Entry<Long,String> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}
