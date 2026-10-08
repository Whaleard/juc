package future;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * @author Mr.MC
 */
public class CompletableFutureUseDemo {

    protected static Logger logger;

    static {
        logger = Logger.getLogger(CompletableFutureUseDemo.class.getName());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * ForkJoinPool.commonPool()是JVM全局共享的线程池实例，CompletableFuture中未显示指定线程池的方法runAsync(Runnable runnable)与supplyAsync(Supplier<U> supplier)均默认使用该池
         * ForkJoinPool产生的线程是守护线程‌，导致虽然主线程结束但是CompletableFuture并没有完成任务就也跟着结束了
         * 因此选择自定义线程池而不使用默认线程池ForkJoinPool
         */
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "======come in");
                int result = ThreadLocalRandom.current().nextInt(10);
                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("======1秒钟后出结果：" + result);
                return result;
            }, threadPool).whenComplete((v, e) -> {
                if (e == null) {
                    System.out.println("======计算完成，更新结果值：" + v);
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                System.out.println("异常情况：" + e.getCause() + "：" + e.getMessage());
                return null;
            });
            // 获取cpu核心数
            logger.info("当前服务器共有cpu核心数：" + Runtime.getRuntime().availableProcessors());

            System.out.println(Thread.currentThread().getName() + "线程先去忙其他任务");

            // 由于CompletableFuture默认的线程池ForkJoinPool中的线程为守护线程，故暂停主线程等待守护线程任务完成后再关闭
            TimeUnit.SECONDS.sleep(5L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
