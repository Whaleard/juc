package lockcase;

import java.util.concurrent.TimeUnit;

class Phone8 {

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
 * 问题八：一个静态同步方法，一个普通同步方法，两部手机，先打印短信还是邮件
 * ============sendEmail============
 * ============sendSMS============
 */
public class LockQuestion8 {

    public static void main(String[] args) {
        Phone8 phone = new Phone8();
        Phone8 phone2 = new Phone8();

        new Thread(() -> phone.sendSMS(), "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> phone2.sendEmail(), "B").start();
    }
}
