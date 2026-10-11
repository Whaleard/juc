package wait_notify;

import java.util.concurrent.TimeUnit;

/**
 * 通过Object类中的wait()和notify()方法实现线程等待唤醒机制
 */
public class LockSupportDemo {

    public static void main(String[] args) {
        Object objectLock = new Object();

        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "线程 come in");
                try {
                    // 线程等待
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "线程被唤醒");
            }
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            synchronized (objectLock) {
                // 唤醒线程
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "线程发送唤醒");
            }
        }, "t2").start();
    }
}
