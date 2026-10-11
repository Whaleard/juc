package interrupt;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 如何中断运行中的线程
 */
public class InterruptDemo {

    static volatile boolean isStop = false;

    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    /**
     * 通过volatile变量实现线程中断案例
     */
    @Test
    public void test01() {
        new Thread(() -> {
            while (true) {
                if (isStop) {
                    System.out.println(Thread.currentThread().getName() + " isStop被修改为true，程序停止");
                    break;
                }
                System.out.println("============hello volatile");
            }
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            isStop = true;
        }, "t2").start();
    }

    /**
     * 通过AtomicBoolean实现线程中断案例
     */
    @Test
    public void test02() {
        new Thread(() -> {
            while (true) {
                if (atomicBoolean.get()) {
                    System.out.println(Thread.currentThread().getName() + " atomicBoolean被修改为true，程序停止");
                    break;
                }
                System.out.println("============hello atomicBoolean");
            }
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            atomicBoolean.set(true);
        }, "t2").start();
    }

    /**
     * 通过isInterrupted()实现线程中断案例
     */
    @Test
    public void test03() {
        Thread t1 = new Thread(() -> {
            while (true) {
                // public boolean isInterrupted()实例方法判断当前线程是否被中断（通过检查中断标志位）
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " isInterrupted()被修改为true，程序停止");
                    break;
                }
                System.out.println("============hello isInterrupted");
            }
        }, "t1");
        t1.start();

        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            // public void interrupt()实例方法仅仅是设置线程的中断状态为true，发起一个协商而不会立刻停止线程
            t1.interrupt();
        }, "t2").start();
    }
}
