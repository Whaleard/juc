package base;

import java.util.concurrent.TimeUnit;

public class DaemonDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            // isDaemon()判断线程是否是守护线程
            System.out.println(Thread.currentThread().getName() + "开始运行，" + (Thread.currentThread().isDaemon() ? "守护线程" : "用户线程"));
            while (true) {

            }
        }, "t1");

        // 将t1设置为守护线程
        t1.setDaemon(true);
        t1.start();

        // 暂停几秒钟
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + "======end 主线程");
    }
}
