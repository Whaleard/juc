package sync;

/**
 * 第一步：创建资源类，定义属性和操作方法
 */
class Ticket {
    /**
     * 票数
     */
    private int number = 30;

    /**
     * 卖票方法
     */
    public synchronized void sale() {
        // 判断：是否有票
        if (this.number > 0) {
            System.out.println(Thread.currentThread().getName() + "：卖出第" + (this.number--) + "张票，剩下：" + this.number + "张票");
        }
    }
}

/**
 * 第二步：创建多个线程，调用资源类的操作方法
 *
 * @author Mr.MC
 */
public class SaleTicket {

    public static void main(String[] args) {
        // 创建Ticket对象
        Ticket ticket = new Ticket();
        // 创建三个线程
        new Thread(() -> {
            // 调用卖票方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "窗口1线程").start();

        new Thread(() -> {
            // 调用卖票方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "窗口2线程").start();

        new Thread(() -> {
            // 调用卖票方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "窗口3线程").start();
    }
}