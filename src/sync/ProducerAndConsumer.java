package sync;

/**
 * 第一步：创建资源类，定义属性和操作方法
 */
class Share {

    /**
     * 初始值
     */
    private int number = 0;

    /**
     * 生产（虚假唤醒问题）
     * @throws InterruptedException
     */
    public synchronized void produce() throws InterruptedException {
        // 这里使用if判断会产生虚假唤醒问题
        if (number != 0) {
            // 由于wait()方法特点是在哪里中断，就在哪里被唤醒，唤醒之后不再进行if判断，故引发虚假唤醒问题
            // 例如：生产者线程1获取到cpu，此时number值为0，number++；
            //      生产者线程2获取到cpu，此时number值为1，调用wait()方法
            //      生产者线程1获取到cpu，此时number值为1，调用wait()方法
            //      消费者线程1获取到cpu，此时number值为1，number--，调用notifyALL()方法
            //      生产者线程1、生产者线程2都被唤醒，都不再进行if判断，而是直接进行number++，产生虚假唤醒问题（即不满足被唤醒的条件）
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        this.notifyAll();
    }

    /**
     * 消费（虚假唤醒问题）
     * @throws InterruptedException
     */
    public synchronized void consume() throws InterruptedException {
        if (number != 1) {
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        this.notifyAll();
    }

    /**
     * 生产
     * @throws InterruptedException
     */
    public synchronized void produce2() throws InterruptedException {
        // 第二步：判断、业务操作、通知其他线程
        // 判断number值是否为0，如果不是0，线程等待
        // 这里如果使用if判断会产生虚假唤醒问题
        while (number != 0) {
            this.wait();
        }
        // 如果number值为0，进行+1操作
        number++;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        // 通知其他线程
        this.notifyAll();
    }

    /**
     * 消费
     * @throws InterruptedException
     */
    public synchronized void consume2() throws InterruptedException {
        // 判断number值是否为1，如果不是1，线程等待
        while (number != 1) {
            this.wait();
        }
        // 如果number值为0，进行-1操作
        number--;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        // 通知其他线程
        this.notifyAll();
    }
}

/**
 * synchronized关键字实现生产者消费者问题
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
