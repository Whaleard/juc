package readwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// 资源类
class MyCache {

    // 创建map集合
    private volatile Map<String, Object> map = new HashMap<>();

    // 创建读写锁对象
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // 写
    public void write(String key, Object value) {
        // 添加写锁
        lock.writeLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + " 正在执行写操作" + key);
            TimeUnit.MICROSECONDS.sleep(300L);
            // 写数据
            map.put(key, value);

            System.out.println(Thread.currentThread().getName() + " 写操作执行完毕" + key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放写锁
            lock.writeLock().unlock();
        }

    }

    // 读
    public Object read(String key) {
        // 添加读锁
        lock.readLock().lock();

        Object result = null;
        try {
            System.out.println(Thread.currentThread().getName() + " 正在执行读操作" + key);
            TimeUnit.MICROSECONDS.sleep(300L);

            // 读数据
            result = map.get(key);

            System.out.println(Thread.currentThread().getName() + " 读操作执行完毕" + key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放读锁
            lock.readLock().unlock();
        }

        return result;
    }

}

public class ReadWriteLockIssues {

    public static void main(String[] args) throws InterruptedException {
        MyCache myCache = new MyCache();

        // 创建线程写数据
        for (int i = 1; i <= 5; i++) {
            final int num = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myCache.write(num + "", num);
                }
            }, String.valueOf(i)).start();
        }

        TimeUnit.MICROSECONDS.sleep(300L);

        // 创建线程读数据
        for (int i = 1; i <= 5; i++) {
            final int num = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myCache.read(num + "");
                }
            }, String.valueOf(i)).start();
        }
    }
}
