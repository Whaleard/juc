package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantAndNonReentrantLock {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "：外层");

                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "：中层");

                        lock.lock();
                        try {
                            System.out.println(Thread.currentThread().getName() + "：内层");
                        } finally {
                            lock.unlock();
                        }
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }, "t").start();
    }
}
