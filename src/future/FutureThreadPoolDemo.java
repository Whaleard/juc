package future;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * FutureTask搭配线程池实现异步多线程任务配合，可以显著提高程序的执行效率
 */
public class FutureThreadPoolDemo {

    @Test
    public void test01() {
        long startTime = System.currentTimeMillis();
        // 暂停
        try {
            TimeUnit.MILLISECONDS.sleep(500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("======花费时间：" + (endTime - startTime) + "毫秒");
        System.out.println(Thread.currentThread().getName() + "======end");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        FutureTask<String> futureTask = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task one is over";
        });

        /*
            ExecutorService中的submit()方法用于提交一个Runnable或Callable任务到线程池中执行。
            该方法会返回一个Future对象，Future对象可以用来获取任务执行结果或取消任务执行。
         */
        threadPool.submit(futureTask);

        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task two is over";
        });
        threadPool.submit(futureTask2);

        FutureTask<String> futureTask3 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task three is over";
        });
        threadPool.submit(futureTask3);

        System.out.println("futureTask结果：" + futureTask.get());
        System.out.println("futureTask2结果：" + futureTask2.get());
        System.out.println("futureTask3结果：" + futureTask3.get());

        long endTime = System.currentTimeMillis();
        System.out.println("======花费时间：" + (endTime - startTime) + "毫秒");

        // 平缓关闭线程池，停止接受新任务，但会等待正在执行的任务和队列中的任务完成‌
        threadPool.shutdown();

        System.out.println(Thread.currentThread().getName() + "======end");
    }
}
