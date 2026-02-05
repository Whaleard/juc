package future;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 尽量不通过构造方法创建CompletableFuture对象
 * 而是通过CompletableFuture的四个静态方法：
 *      runAsync(Runnable runnable)
 *      runAsync(Runnable runnable, Executor executor)
 *      supplyAsync()
 *      supplyAsync()
 */
public class CompletableFutureBuildDemo {

    /**
     * runAsync(Runnable runnable)方法：
     *     无返回值
     *     没有指定Executor，使用默认的ForkJoinPool.commonPool()作为线程池执行异步代码
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println(completableFuture.get());
    }

    /**
     * runAsync(Runnable runnable, Executor executor)方法：
     *      无返回值
     *      指定了Executor，使用指定的线程池执行异步代码
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool);

        System.out.println(completableFuture.get());

        threadPool.shutdown();
    }

    /**
     * supplyAsync(Callable callable)方法：
     *      有返回值
     *      没有指定Executor，使用默认的ForkJoinPool.commonPool()作为线程池执行异步代码
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test03() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello supplyAsync";
        });

        System.out.println(completableFuture.get());
    }

    /**
     * supplyAsync(Callable callable, Executor executor)方法：
     *      有返回值
     *      指定了Executor，使用指定的线程池执行异步代码
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test04() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello supplyAsync";
        }, threadPool);

        System.out.println(completableFuture.get());
    }
}
