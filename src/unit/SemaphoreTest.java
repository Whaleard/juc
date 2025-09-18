package unit;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 案例：6辆汽车停3个停车位
 *
 * @author Mr.MC
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        // 创建Semaphore，设置许可数量
        Semaphore semaphore = new Semaphore(3);

        // 模拟6辆汽车
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                // 抢占
                try {
                    semaphore.acquire();

                    System.out.println(Thread.currentThread().getName() + "抢到了车位");

                    // 设置随机停车时间
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));

                    System.out.println(Thread.currentThread().getName() + "离开了车位");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
