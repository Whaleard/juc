package test;

import org.junit.Test;

public class test1 {

    @Test
    public void test() {
        System.out.println(Thread.currentThread().getName() + "::" + Thread.currentThread().isDaemon());
    }
}
