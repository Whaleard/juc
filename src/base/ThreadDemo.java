package base;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 线程创建的四种方式
 *      1、继承Thread类，调用run()方法，无返回值
 *      2、实现Runnable接口，调用start()方法，无返回值
 *      3、实现Callable接口，有返回值
 *      4、使用线程池
 *
 * 注意：1、2、3三种方式最后都是通过Thread的start()方法来开启线程调用
 *      调用start()方法会新建一个线程，而调用run()方法只有主线程执行
 *
 * @author Mr.MC
 */
public class ThreadDemo {

    public static void main(String[] args) {
        // 继承Thread类
        MyThread t = new MyThread();
        t.start();

        // 实现Runnable接口
        MyRunnable mr = new MyRunnable();
        Thread t2 = new Thread(mr);
        t2.start();

        // 实现Callable接口
        MyCallable mc = new MyCallable();
        FutureTask<String> futureTask = new FutureTask<>(mc);
        Thread t3 = new Thread(futureTask);
        t3.start();
    }
}

class MyThread extends Thread {

    @Override
    public void run() {
        super.run();
        System.out.println("MyThread...");
    }
}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("MyRunnable...");
    }
}

class MyCallable implements Callable {

    @Override
    public Object call() throws Exception {
        return "MyCallable...";
    }
}

