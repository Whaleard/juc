package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock为显示可重入锁
 */
public class ReentrantAndNonReentrantLock {

    public static void main(String[] args) {
        // ReentrantLock可重入锁
        Lock lock = new ReentrantLock();
        // 创建线程
        new Thread(() -> {
            // 上锁
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "：外层");
                // 上锁
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "：中层");
                    // 上锁
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "：内层");
                    } finally {
                        // 释放锁
                        lock.unlock();
                    }
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            } finally {
                // 释放锁
                lock.unlock();
            }
        }, "t").start();
    }
}
