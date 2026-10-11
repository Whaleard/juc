package wait_notify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用LockSupport的park()和unpark()方法实现线程等待唤醒机制
 */
public class LockSupportDemo3 {

    /**
     * park()和unpark()方法不需要获取锁，且支持先唤醒后等待
     *
     * @param args
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "线程 come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "线程被唤醒");
        }, "t1");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "线程发送唤醒");
        }, "t2").start();
    }
}
