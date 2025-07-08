package lock;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合线程安全问题
 */
public class CollectionSafetyIssues {

    /**
     * ArrayList的add()方法没有加锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test01() throws InterruptedException {
        // 创建ArrayList集合
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                list.add(UUID.randomUUID().toString().substring(0, 8));
                // 从集合中获取内容
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * Vector的add()方法在方法声明时加上了synchronized关键字
     *
     * @throws InterruptedException
     */
    @Test
    public void test02() throws InterruptedException {
        // 创建ArrayList集合
        List<String> list = new Vector<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                list.add(UUID.randomUUID().toString().substring(0, 8));
                // 从集合中获取内容
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    @Test
    public void test03() throws InterruptedException {
        // 创建ArrayList集合
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                list.add(UUID.randomUUID().toString().substring(0, 8));
                // 从集合中获取内容
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * ArrayList线程不安全，若多个线程同时对ArrayList进行读写操作，会出现ConcurrentModificationException异常
     * 解决办法：
     *      1、使用Vector替换ArrayList
     *      2、通过Collections.synchronizedList(new ArrayList<>())将ArrayList封装为线程安全的集合
     *      3、使用CopyOnWriteArrayList替换ArrayList
     *
     * @throws InterruptedException
     */
    @Test
    public void test04() throws InterruptedException {
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * HashSet的add()方法没有加锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test05() throws InterruptedException {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * HashSet线程不安全，使用CopyOnWriteArraySet替换HashSet
     *
     * @throws InterruptedException
     */
    @Test
    public void test06() throws InterruptedException {
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * HashMap的put()方法没有加锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test07() throws InterruptedException {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            String key = String.valueOf(i);
            new Thread(() -> {
                map.put(key, UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * HashMap线程不安全，使用ConcurrentHashMap替换HashMap
     *
     * @throws InterruptedException
     */
    @Test
    public void test08() throws InterruptedException {
        Map<String, String> set = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            String key = String.valueOf(i);
            new Thread(() -> {
                set.put(key, UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }
}
