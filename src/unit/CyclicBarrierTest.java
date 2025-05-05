package unit;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 集齐7颗龙珠就可以召唤神龙
 */
public class CyclicBarrierTest {

    // 创建固定值
    private static final int NUMBER = 7;

    public static void main(String[] args) {
        // 创建CyclicBarrier
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, new Runnable() {
            @Override
            public void run() {
                System.out.println("============集齐7颗龙珠就可以召唤神龙============");
            }
        });

        for (int i = 1; i <= 7; i++) {
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
}
