package wait_notify;

import java.util.concurrent.TimeUnit;

/**
 * 通过Object类中的wait()和notify()方法实现线程等待唤醒机制
 */
public class LockSupportDemo {

    /**
     * wait()和notify()操作的是对象的监视器（Monitor），JVM强制要求调用线程必须持有该对象的锁。
     * 若未在synchronized块中调用，线程未获取锁，直接抛出IllegalMonitorStateException异常。
     *
     * @param args
     */
    public static void main(String[] args) {
        Object objectLock = new Object();

        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "线程 come in");
                try {
                    // 线程等待
                    // wait()会让当前线程释放调用它的对象锁，并进入等待状态
                    objectLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
                // notify()不会释放锁，只是唤醒一个正在等待的线程，直到退出同步块后锁才会释放
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "线程发送唤醒");
            }
        }, "t2").start();
    }
}
