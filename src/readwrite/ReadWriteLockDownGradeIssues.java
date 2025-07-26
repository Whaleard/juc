package readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDownGradeIssues {

    public static void main(String[] args) {
        // 可重入读写锁对象
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
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
}
