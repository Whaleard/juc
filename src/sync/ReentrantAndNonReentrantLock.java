package sync;

/**
 * synchronized为隐式可重入锁，可重入锁又叫递归锁
 *
 * @author Mr.MC
 */
public class ReentrantAndNonReentrantLock {

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
