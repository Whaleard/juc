package unit;

import java.util.concurrent.CountDownLatch;

/**
 * 6个同学陆续离开教室之后，班长锁门
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setName("班长");

        // 创建CountDownLatch对象，设置初始值
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "号同学离开教室");
                    // 计数器-1
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();
        }
        // 等待
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "锁门");
    }
}
