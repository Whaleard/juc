package lockcase;

import java.util.concurrent.TimeUnit;

class Phone4 {

    public synchronized void sendEmail() {
        System.out.println("============sendEmail============");
    }

    public synchronized void sendSMS() {
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("============sendSMS============");
    }
}

/**
 * 现在有两部手机，先打印短信还是邮件
 * ============sendEmail============
 * ============sendSMS============
 *
 * 锁对象不一致不会产生冲突
 */
public class LockQuestion4 {

    public static void main(String[] args) {
        Phone4 phone = new Phone4();
        Phone4 phone2 = new Phone4();

        new Thread(() -> {
            phone.sendSMS();
        }, "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            phone2.sendEmail();
        }, "B").start();
    }
}
