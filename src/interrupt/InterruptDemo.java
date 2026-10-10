package interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 通过volatile变量实现线程中断案例
 */
public class InterruptDemo {

    static volatile boolean isStop = false;

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (isStop) {
                    System.out.println(Thread.currentThread().getName() + " isStop被修改为true，程序停止");
                    break;
                }
                System.out.println("============hello volatile");
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            isStop = true;
        }, "t2").start();
    }
}
