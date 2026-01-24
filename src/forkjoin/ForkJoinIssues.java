package forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class MyTask extends RecursiveTask<Integer> {

    /**
     * 拆分差值不能超过10
     */
    private static final Integer DIFF_VALUE = 10;

    /**
     * 拆分开始值
     */
    private int begin;

    /**
     * 拆分结束值
     */
    private int end;

    /**
     * 结果值
     */
    private int result;

    /**
     * 有参构造
     * @param begin
     * @param end
     */
    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * 拆分和合并过程
     * @return
     */
    @Override
    protected Integer compute() {
        // 判断相加的两个数差值是否大于10
        if (end - begin <= DIFF_VALUE) {
            // 相加操作
            for (int i = begin; i <= end; i++) {
                result += i;
            }
        } else {
            // 获取中间值
            int mid = (begin + end) / 2;
            // 拆分左边
            MyTask task = new MyTask(begin, mid);
            // 拆分右边
            MyTask task2 = new MyTask(mid + 1, end);
            // 调用方法进行拆分
            task.fork();
            task2.fork();
            // 合并结果
            result = task.join() + task2.join();
        }
        return result;
    }
}

/**
 * @author Mr.MC
 */
public class ForkJoinIssues {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建MyTask对象
        MyTask myTask = new MyTask(1, 100);
        // 创建分支合并池对象
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        // 获取最终合并结果
        Integer result = forkJoinTask.get();
        System.out.println(result);
        // 关闭池对象
        forkJoinPool.shutdown();
    }
}
