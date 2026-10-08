package sync;

/**
 * 隐式可重入锁synchronized
 *
 * @author Mr.MC
 */
public class ReentrantAndNonReentrantLock {

    /**
     * Synchronized的重入实现机理：
     *  1、每个锁对象拥有一个锁计数器和一个指向持有该锁的线程的指针。
     *  2、当执行monitorenter指令时，如果目标锁对象的计数器为零，那么说明它没有被其他线程所持有，Java虚拟机会将该锁对象的持有线程设置为当前线程，并且将其计数器加1。
     *  3、在目标锁对象的计数器不为零的情况下，如果锁对象的持有线程是当前线程，那么Java虚拟机可以将其计数器加1，否则需要等待，直至持有线程释放该锁。
     *  4、当执行monitorexit指令时，Java虚拟机则需将锁对象的计数器减1。计数器为零代表锁已被释放。
     *
     * @param args
     */
    public static void main(String[] args) {
        Object o = new Object();
        new Thread(() -> {
            synchronized (o) {
                System.out.println(Thread.currentThread().getName() + "：外层");

                synchronized (o) {
                    System.out.println(Thread.currentThread().getName() + "：中层");

                    synchronized (o) {
                        System.out.println(Thread.currentThread().getName() + "：内层");
                    }
                }
            }
        }, "t").start();
    }
}
