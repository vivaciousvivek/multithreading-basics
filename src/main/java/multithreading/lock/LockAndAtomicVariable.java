package multithreading.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * In multithreaded programs, access to shared variables must be synchronized in order to prevent
 * race conditions.
 *
 * <p>We use synchronized methods and synchronized blocks to protect concurrent access to shared
 * variables and avoid race conditions.
 *
 * <p>Java’s synchronized keyword internally uses the intrinsic lock associated with an object to
 * gain exclusive access to the object’s member fields.
 *
 * <p>Instead of using an intrinsic lock, we can also use various Locking classes provided by Java’s
 * Concurrency API to have more fine-grained control over the locking mechanism.
 *
 * <p>And modern way of thread synchronization via various Atomic classes provided by Java
 * concurrency API.
 *
 * <p>We should use these Atomic classes instead of synchronized keyword and locks whenever
 * possible because they are faster, easier to use, readable and scalable.
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 21 20 : 15 : 06)
 */
public class LockAndAtomicVariable {
  private static int count = 0;

  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    System.out.println("****ReentrantLock****\n");
    executorService.submit(
        () -> {
          System.out.println("First Reentrant :" + Thread.currentThread().getName());
          System.out.println("\nCount value from First Thread : " + getReentrantLock());
        });

    executorService.submit(
        () -> {
          System.out.println("Second Reentrant :" + Thread.currentThread().getName());
          System.out.println("\nCount value from Second Thread : " + getReentrantLock());
        });

    executorService.shutdown();

    /**
     * ﻿If any thread acquires the write-lock, then all the reader threads will pause their
     * execution and wait for the writer thread to return.
     */
    ExecutorService executorService1 = Executors.newFixedThreadPool(3);

    System.out.println("\n\n****ReadWriteLock****\n");
    MyReadWriteLock myReadWriteLock = new MyReadWriteLock();

    Runnable readTask =
        (() -> {
          System.out.println("First ReadWrite :" + Thread.currentThread().getName());
          System.out.println("\nCount value from First Thread : " + myReadWriteLock.getCount());
        });

    Runnable writeTask =
        (() -> {
          System.out.println("Second ReadWrite :" + Thread.currentThread().getName());
          System.out.println(
              "\nCount value from Second Thread : " + myReadWriteLock.getIncrementAndCount());
        });

    executorService1.submit(readTask);
    executorService1.submit(readTask);

    executorService1.submit(writeTask);

    executorService1.submit(readTask);
    executorService1.submit(readTask);

    executorService1.shutdown();
    try {
      executorService1.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    /** Atomic Variables */
    ExecutorService executorService2 = Executors.newFixedThreadPool(2);

    System.out.println("\n\n****Atomic Variable****\n");
    MyAtomicVariable myAtomicVariable = new MyAtomicVariable();

    for (int i = 0; i < 10; i++) {
      executorService2.submit(() -> myAtomicVariable.incrementAndGetAtomicCount());
    }

    executorService2.shutdown();
    try {
      executorService2.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Count : " + myAtomicVariable.getAtomicCount());
  }

  /**
   * ReentrantLock is a mutually exclusive lock with the same behavior as the intrinsic/implicit
   * lock but with extra feature, a thread that currently owns the lock can acquire it more than
   * once without any problem.
   */
  private static int getReentrantLock() {
    final ReentrantLock reentrantLock = new ReentrantLock();

    // acquire the lock and start processing
    reentrantLock.lock();
    try {
      return ++count;
    } finally {
      // release the lock, so that other threads is waiting for the lock can acquire it, best
      // practice to use it inside the finally block so that it will release in any case
      reentrantLock.unlock();
    }
  }
}

/**
 * ReadWriteLock consists of a pair of locks - one for read access and one for write access.
 *
 * <p>The read lock may be held by multiple threads simultaneously as long as the write lock is not
 * held by any thread.
 *
 * <p>ReadWriteLock allows for an increased level of concurrency. It performs better compared to
 * other locks in applications where there are fewer writes than reads.
 */
class MyReadWriteLock {
  private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private int count = 0;

  public int getIncrementAndCount() {
    readWriteLock.writeLock().lock();
    try {
      return ++count;
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  public int getCount() {
    readWriteLock.readLock().lock();
    try {
      return count;
    } finally {
      readWriteLock.readLock().unlock();
    }
  }
}

/**
 * Java’s concurrency api defines several classes in java.util.concurrent.atomic package that
 * support Atomic operations on single variables.
 *
 * <p>Atomic classes internally use <b>compare-and-swap</b> instructions supported by modern CPUs to
 * achieve synchronization.
 *
 * <p>These instructions are generally much faster than locks.
 */
class MyAtomicVariable {
  private AtomicInteger atomicCount = new AtomicInteger(0);

  public int incrementAndGetAtomicCount() {
    return atomicCount.incrementAndGet();
  }

  public int getAtomicCount() {
    return atomicCount.get();
  }
}
