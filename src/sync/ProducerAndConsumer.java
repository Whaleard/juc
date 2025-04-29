package sync;

// 第一步：创建资源类，定义属性和操作方法
class Share {

    // 初始值
    private int number = 0;

    // 生产
    public synchronized void produce() throws InterruptedException {
        // 第二步：判断、业务操作、通知其他线程
        // 判断number值是否为0，如果不是0，线程等待
        while (number != 0) {   // 这里如果使用if判断会产生虚假唤醒问题
            this.wait();
        }
        // 如果number值为0，进行+1操作
        number++;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        // 通知其他线程
        this.notifyAll();
    }

    // 消费
    public synchronized void consume() throws InterruptedException {
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

public class ProducerAndConsumer {

    public static void main(String[] args) {
        Share share = new Share();
        // 创建线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.produce();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "生产者线程1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.produce();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "生产者线程2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.consume();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "消费者线程1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.consume();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "消费者线程2").start();
    }
}
