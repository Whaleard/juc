package lockcase;

import java.util.concurrent.TimeUnit;

class Phone5 {

    public static synchronized void sendEmail() {
        System.out.println("============sendEmail============");
    }

    public static synchronized void sendSMS() {
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("============sendSMS============");
    }
}

/**
 * 问题五：两个静态同步方法，一部手机，先打印短信还是邮件
 * ============sendSMS============
 * ============sendEmail============
 *
 * 普通同步方法中synchronized锁的范围是当前实例对象，通常指this
 * 静态同步方法中synchronized锁的范围是当前类对象，例如Phone5.Class
 * 同步方法块中synchronized的锁是括号里配置的对象
 */
public class LockQuestion5 {

    public static void main(String[] args) {
        Phone5 phone = new Phone5();

        new Thread(() -> phone.sendSMS(), "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> phone.sendEmail(), "B").start();
    }
}
