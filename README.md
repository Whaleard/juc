# juc
JUC
    java.util.concurrent相关工具包的简称

进程、线程和管程
    进程：资源分配的最小单位
    线程：cpu调度和执行的最小单位
    管程（Monitor）
        定义：
            一种模块化的同步结构，将共享资源及其操作封装为一个独立单元，确保同一时间仅有一个线程执行管程内的代码，天然实现互斥。
        核心组成：
            1、共享变量：存储资源状态，
            2、操作过程；封装对共享数据的操作（如申请、释放资源），每次仅允许一个进程调用。
            3、条件变量：管理线程阻塞与唤醒（如wait()和signal()），解决资源不足时的同步问题。
            4、初始化代码：设置共享变量的初始值。
        作用与优势：
            互斥访问：通过内置互斥锁自动保证线程安全，避免竞态条件。
            条件同步：线程可因资源不足主动阻塞（通过条件变量），待资源可用时被唤醒，无须轮询检查。

线程状态：
    NEW（新建）：线程对象通过new Thread()创建后但未调用start()方法时的状态，此时线程尚未启动。
    RUNNABLE（可运行）：调用start()方法后，线程进入此状态，包含两种情况
        Ready：等待cpu调度
        Running：正在执行任务
    BLOCKED（阻塞）：线程因竞争同步锁（如synchronized）失败而被阻塞，直到获取锁后重新进入RUNNABLE状态。
    WAITING（等待）：线程主动调用wait()、join()等方法进入此状态，需其他线程显示唤醒（如notify()）。
    TIMED_WAITING（计时等待）：与WAITING类似，但通过sleep(time)或wait(time)等方法设置超时时间，时间结束后自动恢复。
    TERMINATED（终止）：线程执行完run()方法或异常退出后的最终状态，不可重启。

sleep()和wait()
    相同点：
        1、都可以被interrupted方法中断。
        2、方法在哪里中断，就在哪里被唤醒。
    不同点：
        1、sleep()是Thread的静态方法；wait()是Object的方法，任何对象实例都能调用。
        2、sleep()调用后不会释放持有的锁，调用前也不需要占用锁；wait()调用后会‌立即释放锁‌，但调用它的前提是当前线程占有锁（即代码要在synchronized中）。

用户线程和守护线程
    用户线程：自定义创建的线程
    守护线程：JVM（java虚拟机）自动创建的线程，一种特殊的为其他线程服务的线程，在后台默默地完成一些系统性的服务，比如垃圾回收线程。
            守护线程作为一个服务线程，没有服务对象就没有必要继续运行了。如果用户线程全部结束了，意味着程序需要完成的业务操作已经结束了，系统可以退出了。
            所以假如当系统只剩下守护线程的时候，JVM就会自动退出。
    注意：
        1、如果用户线程全部结束意味着程序需要完成的业务操作已经结束了，守护线程随着JVM一同结束工作。
        2、通过new Thread()创建的线程默认是用户线程，通过setDaemon(true)可以将用户线程变为守护线程。
        2、 setDaemon(true)方法必须在start()之前设置，否则报IllegalThreadStateException异常。

Synchronized关键字
    synchronized是Java中的关键字，是一种同步锁。它可以修饰以下几种对象
        1、修饰代码块，被修饰的代码块称为同步代码块，其作用的范围是大括号内的代码，作用的对象是调用这个方法的对象。
        2、修饰方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象。

Lock和synchronized相同点：
    1、都是可重入锁
Lock和synchronized不同点：
    1、Lock是一个接口，synchronized是关键字
    2、synchronized在执行完成或发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在执行完成或发生异常时，如果没有主动通过unlock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁
    3、Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断
    4、通过Lock可以知道有没有成功获取锁，而synchronized却无法办到
    5、Lock可以提高多个线程进行读操作的效率
    注：在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争

什么是死锁
    两个或两个以上进程在执行过程中，因为争夺资源而造成一种互相等待的现象，如果没有外力干涉，它们无法再执行下去
产生死锁原因
    1、互斥：至少有一个资源必须处于非共享模式，即一次只能被一个进程占用。若其他进程请求该资源，必须等待当前进程释放
    2、不可剥夺：进程已获取的资源在未使用完之前不能被系统或其他进程强制夺走，只能由进程主动释放
    3、请求与保持：进程在持有至少一个资源的同时，又提出新的资源请求，而该资源已被其他进程占有，此时请求进程被阻塞，但不会释放已持有的资源
    4、循环等待：存在一个进程-资源的环形等待链，即每个进程都在等待下一个进程所占用的资源
验证是否是死锁
    1、jps（类似linux ps -ef）
    2、jstack（jvm自带堆栈跟踪工具）

Runnable接口与Callable接口的不同点
    Runnable接口的run()方法没有返回值，方法上无法声明throws
    Callable接口的call()方法有返回值，方法上可以声明throws

Future接口
    Future接口定义了操作异步任务执行的一些方法，如获取异步任务的执行结果、取消任务的执行、判断任务是否被取消、判断任务执行是否完毕等。实现类为FutureTask
    Future接口可以为主线程开一个分支任务，专门为主线程处理耗时费力的复杂业务，让主线程继续处理其他任务或者先行结束，再通过Future获取计算结果

Lock和synchronized相同点：
    1、都是可重入锁
Lock和synchronized不同点：
    1、Lock是一个接口，synchronized是关键字
    2、synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unlock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁
    3、Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断
    4、通过Lock可以知道有没有成功获取锁，而synchronized却无法办到
    5、Lock可以提高多个线程进行读操作的效率
    注：在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争

乐观锁和悲观锁
    乐观锁：认为共享资源每次被访问的时候不会出现问题，线程可以不停地执行，无需加锁也无需等待，只是在提交修改的时候去验证对应的资源是否被其它线程修改了
    悲观锁：认为共享资源每次被访问的时候就会出现问题，所以在每次获取资源操作的时候都会上锁 

表锁和行锁
    表锁：锁定整张表，任何操作都会阻塞其他事务的写操作（甚至部分读操作）
    行锁：仅锁定需要操作的具体行，允许其他事务访问未锁定行

读写锁：一个资源可以被多个读线程访问，或者可以被多个写线程访问，但是不能同时存在读写线程，读写互斥，读读共享
    缺点：
        1、造成锁饥饿，一直读，没有写操作
        2、读时不能写，只有读完成之后，才可以写，写时可以读

阻塞队列：
    1、ArrayBlockingQueue
    2、LinkedBlockingQueue
    3、DelayQueue
    4、PriorityBlockingQueue
    5、SynchronousQueue
    6、LinkedTransferQueue
    7、LinkedBlockingDeque

ThreadPool线程池
线程池：一种线程使用模式。线程过多会带来调度开销，进而影响缓存局部性和整体性能。而线程池维护着多个线程，等待着监督管理者分配可并发执行的任务。
      这避免了在处理段时间任务时创建与销毁线程的代价。 线程池不仅能够保证内核的充分利用，还能防止过分调度。
线程池的优势：线程池做的工作只是要控制运行的线程数量，处理过程中将任务放入队列，然后在线程创建后启动这些任务，
           如果线程数量超过了最大数量，超出数量的线程排队等候，等其他线程执行完毕，再从队列中取出任务来执行。
主要特点：降低资源消耗，通过重复利用已创建的线程降低线程创建和销毁造成的消耗
        提高响应速度，当任务到达时，任务可以不需要等待线程创建就能立即执行
        提高线程的可管理性，线程是稀缺资源，如果无限制地创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控
        Java中的线程池是通过Executor框架实现的，该框架中用到了Executor、Executors、ExecutorService、ThreadPoolExecutor这几个类