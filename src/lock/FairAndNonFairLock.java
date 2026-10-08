package lock;

import java.util.concurrent.locks.ReentrantLock;

class Ticket {
    /**
     * 票数
     */
    private int number = 30;

    /**
     * 创建可重入锁
     *
     * public ReentrantLock()默认创建非公平锁，
     * public ReentrantLock(boolean fair)若入参为true则创建公平锁，入参为false创建非公平锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 卖票方法
     */
    public void sale() {
        // 上锁
        lock.lock();
        try {
            // 判断：是否有票
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "：卖出第" + (number--) + "张票，剩下：" + number + "张票");
            }
        } finally {
            lock.unlock();
        }

    }
}

/**
 * 公平锁与非公平锁案例
 *
 * @author Mr.MC
 */
public class FairAndNonFairLock {

    public static void main(String[] args) {
        // 创建Ticket对象
        Ticket ticket = new Ticket();
        // 创建三个线程
        new Thread(() -> {
            // 调用卖票方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "窗口1线程").start();

        new Thread(() -> {
            // 调用卖票方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "窗口2线程").start();

        new Thread(() -> {
            // 调用卖票方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "窗口3线程").start();
    }
}
