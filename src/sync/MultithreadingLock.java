package sync;

/**
 * 普通同步方法中synchronized锁的范围是当前实例对象（this）
 * 静态同步方法中synchronized锁的范围是当前类对象（Class）
 * 同步方法块中synchronized的锁是括号里配置的对象
 */
class Phone {

    public synchronized void sendSMS() throws InterruptedException {
        // TimeUnit.SECONDS.sleep(4L);
        System.out.println("============sendSMS============");
    }

    public synchronized void sendEmail() throws InterruptedException {
        System.out.println("============sendEmail============");
    }

    public void getHello() {
        System.out.println("============getHello============");
    }
}

public class MultithreadingLock {

    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    phone.sendSMS();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "A").start();

        Thread.sleep(100L);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    phone.sendEmail();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "B").start();
    }
}
