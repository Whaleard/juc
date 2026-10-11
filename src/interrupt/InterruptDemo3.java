package interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 中断只是一种协商机制，修改中断标识位仅此而已，不是立刻打断线程
 */
public class InterruptDemo3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "线程中断标志位：" + Thread.currentThread().isInterrupted() + "，程序停止");
                    break;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    // 如果线程处于阻塞状态（例如处于sleep、wait、join等状态），在别的线程中调用当前线程对象的interrupt()方法，线程将立即退出被阻塞状态，中断标志位被清除（重置为false），并抛出InterruptedException异常
                    // 由于中断标志位被清除，导致无限循环，需重新设置中断标志位
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }

                System.out.println("============hello InterruptDemo3");
            }
        }, "t1");
        t1.start();

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            t1.interrupt();
        }, "t2").start();
    }
}
