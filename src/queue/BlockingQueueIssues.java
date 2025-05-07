package queue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueIssues {

    /**
     * 抛出异常
     */
    @Test
    public void test01() {
        // 创建阻塞队列
        BlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        /*
            add()：插入元素
                  当阻塞队列不满时，往队列里插入元素返回true
                  当阻塞队列满时，往队列里插入元素抛出异常 java.lang.IllegalStateException: Queue full
         */
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        System.out.println(blockingQueue.element());

        // System.out.println(blockingQueue.add("d"));

        /*
            remove()：移除元素
                     当阻塞队列不空时，从队列里移除元素返回元素内容
                     当阻塞队列空时，从队列里移除元素抛出异常 java.util.NoSuchElementException
         */
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        // System.out.println(blockingQueue.remove());
    }

    /**
     * 返回特殊值
     */
    @Test
    public void test02() {
        // 创建阻塞队列
        BlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        /*
            offer()：插入元素
                    成功返回true
                    失败返回false
         */
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("d"));

        /*
            poll()：移除元素
                   成功返回出队列元素
                   队列里没有元素返回null
         */
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
    }

    /*
        阻塞
     */
    @Test
    public void test03() throws InterruptedException {
        // 创建阻塞队列
        BlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        /*
            put()：插入元素
                  没有返回值
                  当阻塞队列满时，生产者线程往队列里插入元素，队列会阻塞生产者线程直到插入元素或者响应中断退出
         */
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");
        // blockingQueue.put("d");

        /*
            take()：移除元素
                   成功返回出队列元素
                   当阻塞队列空时，消费者线程从队列里移除元素，队列会一直阻塞消费者线程直到队列可用
         */
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        // System.out.println(blockingQueue.take());
    }

    /*
        超时
     */
    @Test
    public void test04() throws InterruptedException {
        // 创建阻塞队列
        BlockingQueue blockingQueue = new ArrayBlockingQueue(3);

        /*
            offer()：插入元素
                    成功返回true
                    当阻塞队列满时，队列会阻塞生产者线程一定时间，通过限时后生产者线程会退出并返回false
         */
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        // System.out.println(blockingQueue.offer("d", 3L, TimeUnit.SECONDS));

        /*
            poll()：移除元素
                   成功返回出队列元素
                   当阻塞队列空时，队列会阻塞消费者线程一定时间，通过限时后消费者线程会退出并返回null
         */
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll(3L, TimeUnit.SECONDS));
    }
}
