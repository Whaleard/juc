package unit;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier从英文单词可以看出大概就是循环阻塞的意思
 * 在使用中CyclicBarry的构造方法第一个参数是目标障碍数，每个线程执行CyclicBarrier的await()方法一次障碍数会减1并且执行该方法的线程被阻塞，
 * 直到目标障碍数被减为0，才会执行CyclicBarry构造方法第二个参数中传入的线程
 *
 * 案例：集齐7颗龙珠就可以召唤神龙
 */
public class CyclicBarrierTest {

    // 创建固定值
    private static final int NUMBER = 7;

    /**
     * 通过CyclicBarrier实现
     */
    @Test
    public void test01() {
        // 创建CyclicBarrier
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, () -> System.out.println("============集齐7颗龙珠就可以召唤神龙============"));

        for (int i = 1; i <= NUMBER; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "星龙珠已找到");

                // 等待
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, String.valueOf(i)).start();
        }
    }

    /**
     * 通过CountDownLatch实现
     *
     * @throws InterruptedException
     */
    @Test
    public void test02() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(NUMBER);

        for (int i = 1; i <= NUMBER; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "星龙珠已找到");

                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }

        countDownLatch.await();
        System.out.println("============集齐7颗龙珠就可以召唤神龙============");
    }
}
