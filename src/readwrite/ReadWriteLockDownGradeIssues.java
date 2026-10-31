package readwrite;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 锁降级问题
 *
 * @author Mr.MC
 */
public class ReadWriteLockDownGradeIssues {

    /**
     * 写锁可以降级为读锁
     */
    @Test
    public void test01() {
        // 可重入读写锁对象
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        // 读锁
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        // 写锁
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

        // 锁降级
        // 1、获取写锁
        writeLock.lock();
        System.out.println("执行写操作");

        // 2、获取读锁
        readLock.lock();
        System.out.println("执行读操作");

        // 3、释放写锁
        writeLock.unlock();

        // 4、释放读锁
        readLock.unlock();
    }

    /**
     * 读锁不可以升级为写锁
     */
    @Test
    public void test02() {
        // 可重入读写锁对象
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        // 读锁
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        // 写锁
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

        // 锁升级
        // 1、获取读锁
        readLock.lock();
        System.out.println("执行读操作");

        // 2、获取写锁
        writeLock.lock();
        System.out.println("执行写操作");

        // 3、释放读锁
        readLock.unlock();

        // 4、释放写锁
        writeLock.unlock();
    }
}
