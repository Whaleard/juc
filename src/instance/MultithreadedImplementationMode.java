package instance;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 实现Runnable接口
 */
class MyThread implements Runnable {

    @Override
    public void run() {
        System.out.println("============通过实现Runnable接口创建线程============");
    }
}

/**
 * 实现Callable接口
 */
class MyThread2 implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("============通过实现Callable接口创建线程============");
        return 200;
    }
}

public class MultithreadedImplementationMode {

    @Test
    public void testRunnable() {
        new Thread(new MyThread(), "AA").start();
    }

    @Test
    public void testCallable() throws ExecutionException, InterruptedException {
        // FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread2());
        // 创建一个线程
        new Thread(futureTask, "BB").start();
        while (!futureTask.isDone()) {
            System.out.println("wait......");
        }
        // 调用FutureTask的get()方法
        System.out.println(futureTask.get());

        while (!futureTask.isDone()) {
            System.out.println("wait......");
        }
        System.out.println(futureTask.get());
    }
}
