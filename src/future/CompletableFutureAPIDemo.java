package future;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * CompletableFuture类API
 *  1、获取结果
 *      ① public T get()
 *      ② public T get(long timeout, TimeUnit unit)
 *      ③ public T join()
 *      ④ public T getNow(T valueIfAbsent)
 *  2、主动触发计算
 *      ① public boolean complete(T value)
 *  3、对计算结果进行处理
 *      ① public CompletableFuture<T> thenApply(Function<? super T,? extends U> fn)
 */
public class CompletableFutureAPIDemo {

    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    /**
     * CompletableFuture的get()方法会阻塞当前线程，等待异步任务完成并返回结果‌
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abc";
        });

        System.out.println("返回结果：" + completableFuture.get());
    }

    /**
     * CompletableFuture的get(long timeout, TimeUnit unit)方法会阻塞当前线程，等待指定时间，超时会抛出TimeoutException
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abc";
        });

        System.out.println("返回结果：" + completableFuture.get(2L, TimeUnit.SECONDS));
    }

    /**
     * CompletableFuture的join()方法会阻塞当前线程，等待异步任务完成并返回结果‌
     *
     * get()方法与join()方法核心区别
     *  1、异常处理方式
     *      get()方法抛出受检异常：ExecutionException、InterruptedException，必须抛出或捕获。
     *      join()方法抛出非受检异常：CompletionException、CancellationException，编译器不要求必须捕获。
     *  2、是否支持超时
     *      get()的重载方法get(long timeout, TimeUnit unit)可以指定等待时间，超时会抛出TimeoutException。
     *      join()方法只能无限等待。
     */
    @Test
    public void test03() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abc";
        });

        System.out.println("返回结果：" + completableFuture.join());
    }

    /**
     * CompletableFuture的getNow(T valueIfAbsent)方法立即获取结果不阻塞
     *  1、计算完成，返回计算结果。
     *  2、计算未完成，返回valueIfAbsent作为替代结果。
     */
    @Test
    public void test04() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abc";
        });

        System.out.println("返回结果：" + completableFuture.getNow("xxx"));
    }

    /**
     * CompletableFuture的complete(T value)方法用来手动完成任务并设置结果
     * 核心作用是当你已经拿到结果，或者想主动结束一个还没完成的异步任务时，直接调用complete(value)给它喂一个结果，让后续依赖它的逻辑立刻继续跑，不用再等内部异步逻辑执行完。
     *
     * get()和join()的本质是阻塞当前线程，底层逻辑是检查CompletableFuture的result字段
     *  1、如果result为null（未完成），线程会通过LockSupport.park()挂起。
     *  2、如果result非null（已完成），则直接返回结果。
     */
    @Test
    public void test05() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abc";
        });

        System.out.println("任务是否被解除阻塞并返回：" + completableFuture.complete("xxx") + "，返回结果：" + completableFuture.join());
    }

    /**
     * CompletableFuture的thenApply方法是一个‌异步回调方法‌，用于在上一个异步任务正常完成后，对其结果进行‌转换‌，并返回一个新的CompletableFuture。
     * 核心作用是“拿到上一个任务的结果 → 加工处理 → 返回新结果”，适合链式调用和数据处理流水线。
     * 上游任务异常结束，thenApply的函数不会执行。
     */
    @Test
    public void test06() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                System.out.println("第一个任务完成");
                return 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool).thenApply(f -> {
            System.out.println("第二个任务完成");
            return f + 2;
        }).thenApply(f -> {
            System.out.println("第三个任务完成");
            return f + 3;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("任务完成，结果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + "线程任务完成，等待分支任务");
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
    }

    /**
     * CompletableFuture的handle方法是‌同时处理成功结果和异常‌的统一入口，无论任务正常完成还是抛出异常，它都会执行，并返回一个新的CompletableFuture。
     */
    @Test
    public void test07() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                System.out.println("第一个任务完成");
                return 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool).handle((f, e) -> {
            System.out.println("第二个任务完成");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println("第三个任务完成");
            return f + 3;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("任务完成，结果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + "线程任务完成，等待分支任务");
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
    }

    /**
     * CompletableFuture的thenAccept方法用来“消费”前一个任务结果、但不返回新结果的方法。
     * 适合做打印日志、更新状态这类收尾操作。
     */
    @Test
    public void test08() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                System.out.println("第一个任务完成");
                return 1;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool).thenApply(f -> {
            System.out.println("第二个任务完成");
            return f + 2;
        }).thenAccept(r -> {
            System.out.println("第三个任务完成");
            System.out.println("分支任务计算结果：" + r);
        });

        System.out.println(Thread.currentThread().getName() + "线程任务完成，等待分支任务");
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
    }

    /**
     * CompletableFuture.thenRun方法用于在异步任务‌正常完成后‌执行一个‌无参数、无返回值‌的后续动作，适合打印日志、清理资源、发送通知这类“不关心结果”的收尾操作。
     */
    @Test
    public void test09() {
        CompletableFuture.supplyAsync(() -> "result").thenRun(() -> {}).join();
    }

    /**
     *
     */
    @Test
    public void test10() {
        try {
            CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("1号任务：" + Thread.currentThread().getName());
                return "";
            }, threadPool).thenRunAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("2号任务：" + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("3号任务：" + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("4号任务：" + Thread.currentThread().getName());
            });

            completableFuture.get(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * CompletableFuture的applyToEither方法，核心作用是‌在两个异步任务中，哪个先完成就立刻使用哪个的结果‌。
     */
    @Test
    public void test11() {
        CompletableFuture<String> playA = CompletableFuture.supplyAsync(() -> {
            System.out.println("============A come in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "playA";
        });

        CompletableFuture<String> playB = CompletableFuture.supplyAsync(() -> {
            System.out.println("============B come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "playB";
        });

        CompletableFuture<String> result = playA.applyToEither(playB, f -> f + " is winner");

        System.out.println(result.join());
    }

    /**
     *
     */
    @Test
    public void test12() {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "线程启动");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "线程启动");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 20;
        });

        CompletableFuture<Integer> completableFuture = completableFuture1.thenCombine(completableFuture2, (r1, r2) -> {
            System.out.println("============开始两个结果合并");
            System.out.println("结果1：" + r1);
            System.out.println("结果2：" + r2);
            return r1 + r2;
        });

        System.out.println("最终结果：" + completableFuture.join());
    }

    private static final int SIZE = 1000000;

    public static void testCompletableFuture() {
        long startTime = System.currentTimeMillis();
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        Queue<CompletableFuture<Integer>> futureList = new ConcurrentLinkedQueue<>();
        IntStream.range(0, SIZE).forEach(i -> {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                Random random = new Random();
                return random.nextInt(Integer.MAX_VALUE);
            });
            futureList.add(future);
        });
        futureList.forEach(future -> list.add(future.join()));
        long endTime = System.currentTimeMillis();
        System.out.println("testCompletableFuture - size：" + list.size());
        System.out.println("总用时：" + (endTime - startTime) + "毫秒");
        System.out.println();
    }

    @Test
    public void test() {
        testCompletableFuture();
    }
}
