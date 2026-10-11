package interrupt;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 调用interrupt()方法不会中断线程
 */
public class InterruptDemo2 {

    @Test
    public void test01() {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 300; i++) {
                System.out.println("============：第" + i + "次");
            }
            System.out.println("第二次打印======t1线程中断后的中断标志：" + Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        System.out.println("t1线程默认的中断标志：" + t1.isInterrupted());

        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        t1.interrupt();

        System.out.println("第一次打印======t1线程中断后的中断标志：" + t1.isInterrupted());

        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // isInterrupted()调用结果为false，原因是t1已经执行完毕并进入TERMINATED（终止）状态，中断标志被清除，即使调用过interrupt()，终止状态下isInterrupted()恒为false
        System.out.println("第三次打印======t1线程中断后的中断标志：" + t1.isInterrupted());
    }
}
