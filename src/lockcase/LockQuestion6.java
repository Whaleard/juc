package lockcase;

import java.util.concurrent.TimeUnit;

class Phone6 {

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
 * 问题六：两个静态同步方法，两部手机，先打印短信还是邮件
 * ============sendSMS============
 * ============sendEmail============
 *
 * @author Mr.MC
 */
public class LockQuestion6 {

    public static void main(String[] args) {
        Phone6 phone = new Phone6();
        Phone6 phone2 = new Phone6();

        new Thread(() -> phone.sendSMS(), "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> phone2.sendEmail(), "B").start();
    }
}
