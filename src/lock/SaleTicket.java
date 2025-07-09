package lock;

import java.util.concurrent.locks.ReentrantLock;

// 第一步：创建资源类，定义属性和操作方法
class Ticket {
    // 票数
    private int number = 30;

    /**
     * new ReentrantLock()默认创建非公平锁，若入参为true则创建公平锁，入参为false创建非公平锁
     */
    // 创建可重入锁
    private final ReentrantLock lock = new ReentrantLock();

    // 卖票方法
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

public class SaleTicket {
    // 第二步：创建多个线程，调用资源类的操作方法
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
