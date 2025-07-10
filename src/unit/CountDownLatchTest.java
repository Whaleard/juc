package unit;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch类可以设置一个计数器，然后通过countDown()方法来进行减1的操作，使用await()方法等待计数器不大于0，然后继续执行await()方法之后的语句
 *  1、CountDownLatch主要有两个方法，当一个或多个线程调用await()方法时，这些线程会阻塞
 *  2、其他线程调用countDown()方法会将计数器减1（调用countDown()方法的线程不会阻塞）
 *  3、当计数器的值变为0时，因await()方法阻塞的线程会被唤醒，继续执行
 *
 * 案例一：6个同学陆续离开教室之后，班长锁门
 * 案例二：10位运动员准备完毕后，裁判发令，运动员进行比赛
 */
public class CountDownLatchTest {

    @Test
    public void test01() {
        Thread.currentThread().setName("班长");

        // 创建CountDownLatch对象，设置初始值
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "号同学离开教室");
                // 计数器-1
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        // 等待
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "锁门");
    }

    @Test
    public void test02() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "号运动员准备完毕");
                    countDownLatch.await();
                    System.out.println(Thread.currentThread().getName() + "号运动员到达终点");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, String.valueOf(i)).start();
        }

        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("裁判准备发令");
        countDownLatch.countDown();
    }
}
