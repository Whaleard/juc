package lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 第一步：创建资源类，定义属性和操作方法
 */
class Share {

    /**
     * 初始值
     */
    private int number = 0;

    /**
     * 创建Lock
     */
    private Lock lock = new ReentrantLock();
    private Condition condition = this.lock.newCondition();

    /**
     * 生产
     * @throws InterruptedException
     */
    public void produce() throws InterruptedException {
        // 上锁
        this.lock.lock();
        try {
            // 第二步：判断、业务操作、通知其他线程
            // 判断number值是否为0，如果不是0，线程等待
            // 这里如果使用if判断会产生虚假唤醒问题
            while (this.number != 0) {
                this.condition.await();
            }
            // 如果number值为0，进行+1操作
            this.number++;
            System.out.println(Thread.currentThread().getName() + "::" + this.number);
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 消费
     * @throws InterruptedException
     */
    public void consume() throws InterruptedException {
        // 上锁
        this.lock.lock();
        try {
            // 判断number值是否为1，如果不是1，线程等待
            while (this.number != 1) {
                this.condition.await();
            }
            // 如果number值为0，进行-1操作
            this.number--;
            System.out.println(Thread.currentThread().getName() + "::" + this.number);
            // 通知其他线程
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }
}

/**
 * Lock实现生产者消费者问题
 *
 * @author Mr.MC
 */
public class ProducerAndConsumer {

    public static void main(String[] args) {
        Share share = new Share();
        // 创建线程
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "生产者线程1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "生产者线程2").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "消费者线程1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "消费者线程2").start();
    }
}
