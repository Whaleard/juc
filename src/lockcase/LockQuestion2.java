package lockcase;

import java.util.concurrent.TimeUnit;

class Phone2 {

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
 * 问题二：短信方法内停4秒，先打印短信还是邮件
 * ============sendSMS============
 * ============sendEmail============
 *
 * @author Mr.MC
 */
public class LockQuestion2 {

    public static void main(String[] args) {
        Phone2 phone = new Phone2();

        new Thread(() -> {
            phone.sendSMS();
        }, "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            phone.sendEmail();
        }, "B").start();
    }
}
