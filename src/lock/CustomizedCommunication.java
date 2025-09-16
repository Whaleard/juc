package lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 第一步：创建资源类
 */
class ShareResource {
    /**
     * 定义标志位
     */
    private int flag = 1;

    /**
     * 创建Lock锁
     */
    private Lock lock = new ReentrantLock();

    private Condition c1 = this.lock.newCondition();
    private Condition c2 = this.lock.newCondition();
    private Condition c3 = this.lock.newCondition();

    public void printA(int loop) throws InterruptedException {
        // 上锁
        this.lock.lock();
        try {
            while (this.flag != 1) {
                // 等待
                this.c1.await();
            }
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "：轮数：" + loop);
            }
            this.flag = 2;
            // 唤醒
            this.c2.signal();
        } finally {
            // 释放锁
            this.lock.unlock();
        }
    }

    public void printB(int loop) throws InterruptedException {
        // 上锁
        this.lock.lock();
        try {
            while (this.flag != 2) {
                // 等待
                this.c2.await();
            }
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "：轮数：" + loop);
            }
            this.flag = 3;
            // 唤醒
            this.c3.signal();
        } finally {
            // 释放锁
            this.lock.unlock();
        }
    }

    public void printC(int loop) throws InterruptedException {
        // 上锁
        this.lock.lock();
        try {
            while (this.flag != 3) {
                // 等待
                this.c3.await();
            }
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "：轮数：" + loop);
            }
            this.flag = 1;
            // 唤醒
            this.c1.signal();
        } finally {
            // 释放锁
            this.lock.unlock();
        }
    }
}

/**
 * 多线程定制化通信
 *
 * @author Mr.MC
 */
public class CustomizedCommunication {

    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareResource.printA(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareResource.printB(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    shareResource.printC(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "C").start();
    }
}
