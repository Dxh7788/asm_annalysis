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
    @Test
    public void defaultHashMapTest(){
        Map<Long,String> map = new HashMap<>();
        map.put(23L,"test1");
        map.put(46L,"test2");
        map.put(2L,"test3");
        for (Map.Entry<Long,String> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    @Test
    public void conflictMapTest(){
        Map<Long,String> map = new HashMap<>();
        map.put(1L,"test1");
        map.put(17L,"test2");
        map.put(33L,"test3");
        map.put(49L,"test3");
        map.put(65L,"test3");
        map.put(81L,"test3");
        map.put(97L,"test3");
        map.put(113L,"test3");
        map.put(129L,"test3");
        map.put(145L,"test3");
        map.put(161L,"test3");
        map.put(177L,"test3");
        for (Map.Entry<Long,String> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
    /**
     * a%b相当于a&(b-1)如果是3%2=1,相当于3&1
     * 也就是说 15%4 = 3相当于15%3
     * 测试一下14&4的效果
     * 以下相当于 n%16
     * */
    @Test
    public void modelTest(){
        System.out.println(15&0);
        System.out.println(15&1);
        System.out.println(15&2);
        System.out.println(15&3);
        System.out.println(15&4);
        System.out.println(15&5);
        System.out.println(15&6);
        System.out.println(15&7);
        System.out.println(15&8);
        System.out.println(15&9);
        System.out.println(15&10);
        System.out.println(15&11);
        System.out.println(15&12);
        System.out.println(15&13);
        System.out.println(15&14);
        System.out.println(15&15);
        System.out.println(15&16);
        System.out.println(15&17);
        System.out.println(15&18);
    }
    @Test
    public void resizeTest(){
        System.out.println(15&16);
        System.out.println(31&16);
        System.out.println(15&17);
        System.out.println(31&17);
        System.out.println(15&18);
        System.out.println(31&18);
    }
    @Test
    public void ifElseIfTest(){
        //if else为互斥执行,其中一个执行另一个便不执行
        if (true){
            System.out.println("branch 1 test....");
        }else if (true){
            System.out.println("branch 2 test....");
        }else {
            System.out.println("branch 3 test....");
        }

        /**
         * 遍历旧的数据,放置在新的table中.
         * (1)找到不是空的桶,
         * (2)桶内数据可能不止一个,这时还要遍历链表.
         * (3)遍历是把链表分成两部分,一部分是继续保存在旧桶中,一部分挂在新桶部分.
         * (4)最后在新的桶上将上面两部分挂在上边。
         * */

    }
    @Test
    public void mathTest(){
        /**表明2&3=3&2运算的自反性质*/
        System.out.println(2&3);
        System.out.println(3&2);
    }
}
