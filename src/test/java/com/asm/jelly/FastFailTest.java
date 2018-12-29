package com.asm.jelly;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dongxiaohong
 * @date 2018/12/29 14:12
 */
public class FastFailTest {
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
}
