package lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 第一步：创建资源类
class ShareResource {
    // 定义标志位
    private int flag = 1;

    // 创建Lock锁
    private Lock lock = new ReentrantLock();

    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void printA(int loop) throws InterruptedException {
        // 上锁
        lock.lock();
        try {
            while (flag != 1) {
                c1.await();
            }
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "：轮数：" + loop);
            }
            flag = 2;
            c2.signal();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public void printB(int loop) throws InterruptedException {
        // 上锁
        lock.lock();
        try {
            while (flag != 2) {
                c2.await();
            }
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "：轮数：" + loop);
            }
            flag = 3;
            c3.signal();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public void printC(int loop) throws InterruptedException {
        // 上锁
        lock.lock();
        try {
            while (flag != 3) {
                c3.await();
            }
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "：轮数：" + loop);
            }
            flag = 1;
            c1.signal();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}

public class CustomizedCommunication {

    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    try {
                        shareResource.printA(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    try {
                        shareResource.printB(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "B").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    try {
                        shareResource.printC(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "C").start();
    }
}
