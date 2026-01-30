package future;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * Future对于结果的获取不是很友好，只能通过阻塞或轮询的方式得到任务的结果
 */
public class FutureAPIDemo {

    /**
     * get()方法：
     *      获取任务的执行结果。如果任务还未完成，该方法会阻塞当前线程，直到任务完成并返回结果。
     *      如果任务已经取消，则会抛出CancellationException异常；
     *      如果任务执行过程中抛出了异常，则会抛出ExecutionException异常，并将原始异常作为ExecutionException的cause
     *
     * 注意：get()方法容易导致阻塞，一般建议放在代码最后
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "======come in");
            // 暂停几秒钟
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task over";
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        System.out.println(futureTask.get());

        System.out.println(Thread.currentThread().getName() + "======忙其他任务了");
    }

    /**
     * get(long timeout, TimeUnit unit)方法：
     *      获取任务的执行结果，但在指定的超时时间内等待。
     *      如果任务在指定时间内完成，则返回结果；
     *      如果超时仍未完成，则抛出TimeoutException异常。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "======come in");
            // 暂停几秒钟
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task over";
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        System.out.println(Thread.currentThread().getName() + "======忙其他任务了");

        System.out.println(futureTask.get(3, TimeUnit.SECONDS));
    }

    /**
     * isDone()方法：
     *      判断任务是否已经完成。
     *      如果任务已经完成（包括正常完成、被取消或执行过程中出错），则返回true；
     *      如果任务还未完成，则返回false。
     *
     * 注意：isDone()方法可以通过轮询的方式去获取结果，避免get()方法阻塞线程，
     *      但是轮询的方式会耗费无谓的CPU资源，而且也不一定能及时地得到计算结果。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test03() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "======come in");
            // 暂停几秒钟
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task over";
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        System.out.println(Thread.currentThread().getName() + "======忙其他任务了");

        /*
            由于get()方法会阻塞线程，故采用while(true)循环中调用isDone()方法判断FutureTask任务是否执行完成，
            而由于isDone()方法容易消耗CPU资源，通过调用sleep()方法可以减少CPU的消耗
         */
        while (true) {
            if (futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            } else {
                // 暂停几毫秒
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t1线程正在处理中");
            }
        }
    }
}
