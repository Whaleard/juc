package wait_notify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用JUC包中Condition的await()和signal()方法实现线程等待唤醒机制
 */
public class LockSupportDemo2 {

    /**
     * await()和signal()必须持有创建该Condition的同一把Lock锁，否则会直接抛出IllegalMonitorStateException异常
     *
     * @param args
     */
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "线程 come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "线程被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "线程发送唤醒");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
