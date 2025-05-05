package sync;

public class ReentrantAndNonReentrantLock {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    System.out.println(Thread.currentThread().getName() + "：外层");

                    synchronized (this) {
                        System.out.println(Thread.currentThread().getName() + "：中层");

                        synchronized (this) {
                            System.out.println(Thread.currentThread().getName() + "：内层");
                        }
                    }
                }
            }
        }, "t").start();
    }
}
