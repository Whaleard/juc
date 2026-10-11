package interrupt;

/**
 * 静态方法Thread.interrupted()：判断线程是否被中断并清除当前中断状态
 * 这个方法做了两件事：
 *  1、返回当前线程的中断状态，测试当前线程是否已被中断
 *  2、将当前线程的中断状态清零并重新设置为false，清除线程的中断状态
 */
public class InterruptDemo4 {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "线程中断状态：" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "线程中断状态：" + Thread.interrupted());
        System.out.println("============1");
        Thread.currentThread().interrupt();
        System.out.println("============2");
        System.out.println(Thread.currentThread().getName() + "线程中断状态：" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "线程中断状态：" + Thread.interrupted());
    }
}
