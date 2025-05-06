package unit;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 集齐7颗龙珠就可以召唤神龙
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
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, new Runnable() {
            @Override
            public void run() {
                System.out.println("============集齐7颗龙珠就可以召唤神龙============");
            }
        });

        for (int i = 1; i <= NUMBER; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "星龙珠已找到");

                    // 等待
                    try {
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "星龙珠已找到");

                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();
        }

        countDownLatch.await();
        System.out.println("============集齐7颗龙珠就可以召唤神龙============");
    }
}
