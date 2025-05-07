package pool;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * todo 待完善
 */
public class ThreadPoolIssues {

    @Test
    public void test01() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + "执行中");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void test02() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + "执行中");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void test03() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            for (int i = 1; i <= 10; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + "执行中");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    public void test04() {
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
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + "执行中");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
