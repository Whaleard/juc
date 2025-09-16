package lockcase;

import java.util.concurrent.TimeUnit;

class Phone {

    public synchronized void sendEmail() {
        System.out.println("============sendEmail============");
    }

    public synchronized void sendSMS() {
        System.out.println("============sendSMS============");
    }
}

/**
 * 问题一：标准访问，先打印短信还是邮件
 * ============sendSMS============
 * ============sendEmail============
 *
 * 当synchronized作用于实例方法上，锁实际上是由该实例对象持有的。
 * 当一个线程访问该对象的同步方法时，它会锁定该对象，其他线程如果尝试访问该对象的同步方法将会被阻塞，直到锁被释放。
 *
 * 在同一时刻内，通过同一个对象去调用对应类中被synchronized修饰的实例方法，只能有一个线程访问成功，其他线程只能等待
 * 此时synchronized的锁对象为当前对象this
 *
 * @author Mr.MC
 */
public class LockQuestion1 {

    public static void main(String[] args) {
        Phone phone = new Phone();

        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();
    }
}
