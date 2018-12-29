package com.asm.jelly;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dongxiaohong
 * @date 2018/12/29 14:12
 */
public class FastFailTest {
    private List<String> list = new ArrayList<>();

    @Before
    public void setUp(){
        list.add("123");
        list.add("124");
        list.add("126");
        list.add("127");
    }
    @Test
    public void listFastFail(){
        List<String> values = new ArrayList<>();
        values.add("111");
        values.add("112");
        values.add("113");
        values.add("114");
        //使用遍历器的时候不能使用非遍历器的方式改变数据结构
        /*Iterator<String> iterator = values.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
            values.add("115");
        }*/
        //遍历的时候不能添加,因为结构改变values.size()的值该表,造成堆栈溢出
        /*for (int i=0;i<values.size();i++){
            System.out.println(values.get(i));
            values.add("111");
        }*/
        //使用for遍历不能删除
        /*for (int i=0;i<values.size();i++){
            System.out.println(values.get(i));
            values.remove(i);
        }*/
        //迭代器的remove,不能使用values.remove但是可以使用迭代器的删除
        Iterator<String> iterator = values.iterator();
        while (iterator.hasNext()){
            //error values.remove(iterator.next());
            iterator.next();
            iterator.remove();
//            System.out.println(iterator.next());
        }
    }
    /**
     * 启动两个线程
     * 一个遍历集合数据，另一个改变集合结构
     * */
    @Test
    public void listFastFailThreadTest() throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String l:list){
                    System.out.println(l);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                list.remove(1);
            }
        });
        thread1.start();
        thread2.start();
    }
    /**
     * fast-fail机制的不安全
     * */
    @Test
    public void listFastFailThreadIteratorTest() throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String l:list){
                    System.out.println(l);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<String> iterator = list.iterator();
                while (iterator.hasNext()){
                    if (iterator.next()=="123"){
                        iterator.remove();
                    }
                }
            }
        });
        thread1.start();
        thread2.start();
    }
    /**
     * 以下是否能实现fast-safe机制,经验证是可以实现fast-safe机制
     * 但是涉及很多clone和copy操作,而且有可能和原来的数据结构不相同
     * */
    @Test
    public void listIfFastSafeThreadTest(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String l:list){
                    System.out.println(l);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> copyList = new ArrayList<>(list);
                Iterator<String> iterator = copyList.iterator();
                while (iterator.hasNext()){
                    if (iterator.next()=="123"){
                        iterator.remove();
                    }
                }
            }
        });
        thread1.start();
        thread2.start();
    }
}
