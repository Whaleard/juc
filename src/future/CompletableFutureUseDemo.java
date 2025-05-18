package future;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureUseDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * ForkJoinPool产生的线程是守护线程‌，导致虽然main线程结束但是CompletableFuture并没有完成任务就也跟着结束了
         * 因此选择自定义线程池而不使用默认线程池ForkJoinPool
         */
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {
                @Override
                public Integer get() {
                    System.out.println(Thread.currentThread().getName() + "======come in");
                    int result = ThreadLocalRandom.current().nextInt(10);
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("======1秒钟后出结果：" + result);
                    return result;
                }
            }, threadPool).whenComplete(new BiConsumer<Integer, Throwable>() {
                @Override
                public void accept(Integer integer, Throwable throwable) {
                    if (throwable == null) {
                        System.out.println("======计算完成，更新结果值：" + integer);
                    }
                }
            }).exceptionally(new Function<Throwable, Integer>() {
                @Override
                public Integer apply(Throwable throwable) {
                    throwable.printStackTrace();
                    System.out.println("异常情况：" + throwable.getCause() + "：" + throwable.getMessage());
                    return null;
                }
            });

            System.out.println(Thread.currentThread().getName() + "线程先去忙其他任务");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
