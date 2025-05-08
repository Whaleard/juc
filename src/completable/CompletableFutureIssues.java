package completable;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CompletableFutureIssues {

    /**
     * 异步调用 没有返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "执行中");
            }
        });
        completableFuture.get();
    }

    /**
     * 异步调用 有返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                System.out.println(Thread.currentThread().getName() + "执行中");
                // 模拟异常
                int i =  1 / 0;
                return 1024;
            }
        });
        completableFuture.whenComplete(new BiConsumer<Integer, Throwable>() {

            /**
             * @param integer 返回值
             * @param throwable 异常
             */
            @Override
            public void accept(Integer integer, Throwable throwable) {
                System.out.println("============第一个入参：" + integer);
                System.out.println("============第二个入参：" + throwable);
            }
        }).get();
    }
}
