package lockcase;

import java.util.concurrent.TimeUnit;

class Phone7 {

    public synchronized void sendEmail() {
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
 * 问题七：一个静态同步方法，一个普通同步方法，一部手机，先打印短信还是邮件
 * ============sendEmail============
 * ============sendSMS============
 *
 * 类锁和对象锁是两种不同的锁，互不影响
 */
public class LockQuestion7 {

    public static void main(String[] args) {
        Phone7 phone = new Phone7();

        new Thread(() -> phone.sendSMS(), "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> phone.sendEmail(), "B").start();
    }
}
