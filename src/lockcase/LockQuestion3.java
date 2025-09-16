package lockcase;

import java.util.concurrent.TimeUnit;

class Phone3 {

    public synchronized void sendSMS() {
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("============sendSMS============");
    }

    public void hello() {
        System.out.println("============hello============");
    }
}

/**
 * 问题三：新增普通的hello()方法，先打短信还是hello
 * ============hello============
 * ============sendSMS============
 *
 * 使用synchronized修饰的方法与不使用synchronized修饰的方法不会产生冲突
 *
 * @author Mr.MC
 */
public class LockQuestion3 {

    public static void main(String[] args) {
        Phone3 phone = new Phone3();

        new Thread(() -> {
            phone.sendSMS();
        }, "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            phone.hello();
        }, "B").start();
    }
}
