package pool;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * todo 待完善
 */
public class ThreadPoolIssues {

    @Test
    public void test01() {
        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(() -> System.out.println(Thread.currentThread().getName() + "执行中"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // shutdown()核心作用是停止接收新任务并等待所有已提交任务执行完毕后再关闭线程池
            executorService.shutdown();
        }
    }

    @Test
    public void test02() {
        // 创建一个单线程的线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(() -> System.out.println(Thread.currentThread().getName() + "执行中"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void test03() {
        // 可缓存线程池，线程数量不固定
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(() -> System.out.println(Thread.currentThread().getName() + "执行中"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * new ThreadPoolExecutor(int corePoolSize,
     *                        int maximumPoolSize,
     *                        long keepAliveTime,
     *                        TimeUnit unit,
     *                        BlockingQueue<Runnable> workQueue,
     *                        ThreadFactory threadFactory,
     *                        RejectedExecutionHandler handler)
     *
     * corePoolSize：核心线程数量，即使线程处于空闲状态，也不会被销毁
     * maximumPoolSize：线程池中允许的最大线程数量，当工作队列已满时，会继续创建非核心线程，直到达到该值
     * keepAliveTime：非核心线程的存活时间
     * unit：线程存活时间单位
     * workQueue：用于存放待执行任务的阻塞队列
     * threadFactory：自定义线程的创建方式，通常用于设置线程名、守护属性或优先级
     * handler：拒绝策略
     */
    @Test
    public void test04() {
        // 自定义线程池
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                2,
                5,
                2L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(() -> System.out.println(Thread.currentThread().getName() + "执行中"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
