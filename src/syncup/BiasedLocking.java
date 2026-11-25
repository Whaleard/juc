package syncup;

class Ticket {
    /**
     * 票数
     */
    private int number = 30;

    /**
     * 卖票方法
     */
    public synchronized void sale() {
        // 判断：是否有票
        if (this.number > 0) {
            System.out.println(Thread.currentThread().getName() + "：卖出第" + (this.number--) + "张票，剩下：" + this.number + "张票");
        }
    }
}

/**
 * 多线程的情况下，锁不仅不存在多线程竞争，还存在锁由同一个线程多次获得的情况。偏向锁就是在这种情况下出现的，它的出现是为了解决只有在一个线程执行同步时提高性能。
 * 偏向锁是Java中synchronized锁优化机制的一种，核心思想是让锁偏向第一个获取它的线程，从而减少无竞争场景下的加锁开销。
 *  1、基本原理：线程首次获取锁时，JVM会将该线程ID记录在对象头的Mark Word中；后续该线程再次获取锁时，只需比较线程ID，无需CAS或其他同步操作，加锁成本极低。
 *  2、适用场景：适合单线程重复访问同步块、无锁竞争的情况，如初始化阶段或线程局部缓存。
 *  3、锁升级：当其他线程尝试竞争该锁时，偏向锁会撤销并升级为轻量级锁，若竞争进一步加剧则继续升级为重量级锁，升级过程不可逆。
 *  4、版本状态：偏向锁从JDK 1.6开始引入，默认启用；JDK 15起已被官方标记为废弃。
 *
 * @author Mr.MC
 */
public class BiasedLocking {

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