package lock;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合线程安全问题
 */
public class CollectionSafetyIssues {

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
    public void test01() throws InterruptedException {

        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(UUID.randomUUID().toString().substring(0, 8));
                    System.out.println(list);
                }
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
    public void test02() throws InterruptedException {
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    set.add(UUID.randomUUID().toString().substring(0, 8));
                    System.out.println(set);
                }
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }

    /**
     * HashMap线程不安全，使用ConcurrentHashMap替换HashSet
     *
     * @throws InterruptedException
     */
    @Test
    public void test03() throws InterruptedException {
        Map<String, String> set = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            String key = String.valueOf(i);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    set.put(key, UUID.randomUUID().toString().substring(0, 8));
                    System.out.println(set);
                }
            }, String.valueOf(i)).start();
        }

        Thread.sleep(3000L);
    }
}
