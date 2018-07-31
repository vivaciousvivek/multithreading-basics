package multithreading.callablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Limitation of Future object:
 *
 * <p><b>1. It cannot be manually completed</b> :- If it is waiting for any response from api and
 * due to some reason(api service is down) if it is unable to get the records, then their is no way
 * to complete it, not even its previous value
 *
 * <p><b>2. You cannot perform further action on a Future’s result without blocking</b> :- get()
 * method which blocks until the result is available. You don’t have the ability to attach a
 * callback function to the Future and have it get called automatically when the Future’s result is
 * available.
 *
 * <p><b>3. Multiple Futures cannot be chained together</b>
 *
 * <p><b>4. You can not combine multiple Futures together</b>
 *
 * <p><b>5. No Exception Handling</b>
 *
 * <p>All limitation is resolved in java 8 by introducing <b>CompletableFuture</b>.
 *
 * <p>CompletableFuture implements Future and CompletionStage interfaces and provides a huge set of
 * convenience methods for creating, chaining and combining multiple Futures. It also has a very
 * comprehensive exception handling support.
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 18 20 : 11 : 05)
 */
public class MyCompletableFuture {
  public static void main(String[] args) {
    System.out.println("****Basic CompletableFuture****\n");
    basicCompletableFuture();

    System.out.println("\n\n****Async CompletableFuture****\n");
    asyncCompletableFuture();

    System.out.println("\n\n****Async and Return CompletableFuture****\n");
    asyncReturnCompletableFuture();

    System.out.println("\n\n****Attach Callbacks CompletableFuture****\n");
    callbackCompletableFuture();

    System.out.println("\n\n****Combine two or more CompletableFuture using Callbacks****\n");
    combineCompletableFuture();
  }

  private static void basicCompletableFuture() {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    completableFuture.complete("CompletableFuture is complete!");

    try {
      // As get() method blocks until the Future is complete.
      // If we use get() method before complete() then ?
      // So, in this case it will block forever because this Future is never completed.
      System.out.println(completableFuture.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    //    completableFuture.complete("CompletableFuture is complete!");
  }

  /**
   * If you want to run some background task(in a separate thread i.e
   * ForkJoinPool.commonPool-worker) asynchronously and don’t want to return anything from the task,
   * then you can use CompletableFuture.runAsync() method. It takes a Runnable object and can also
   * take Executor and returns CompletableFuture<Void>.
   */
  private static void asyncCompletableFuture() {
    CompletableFuture<Void> completableFuture =
        CompletableFuture.runAsync(
            () -> {
              try {
                System.out.println("Entering :" + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    try {
      completableFuture.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  /**
   * If we want to return some result from our background task(in a separate thread i.e
   * ForkJoinPool.commonPool-worker) by using CompletableFuture.supplyAsync().
   *
   * <p>It takes a Supplier<T> and can also take Executor and returns CompletableFuture<T>
   */
  private static void asyncReturnCompletableFuture() {
    CompletableFuture<String> completableFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                System.out.println("Entering :" + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              return "Result of the asynchronous completion";
            });

    try {
      System.out.println(completableFuture.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  /**
   * CompletableFuture.get() method is blocking.
   *
   * <p>But For building asynchronous systems we should be able to attach a callback to the
   * CompletableFuture which should automatically get called when the Future completes.
   *
   * <p>We can attach a callback to the CompletableFuture using thenApply(), thenAccept() and
   * thenRun() methods
   */
  private static void callbackCompletableFuture() {
    /** We can use thenApply() method to process and transform the result of a CompletableFuture */
    CompletableFuture<String> completableFuture =
        CompletableFuture.supplyAsync(
                () -> {
                  try {
                    System.out.println("Entering :" + Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(1);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  return "\nCompletable Future";
                })
            .thenApply(
                s -> {
                  System.out.println(
                      "thenApply() thread name :" + Thread.currentThread().getName());
                  return s + " from thenApply()";
                })
            .thenApply(s -> s + " !!!");

    /**
     * If you don’t want to return anything from your callback function and just want to run some
     * piece of code after the completion of the Future, then you can use thenAccept() and thenRun()
     * methods. These methods are consumers and are often used as the last callback in the callback
     * chain.
     *
     * <p>﻿All callback methods are executed in the same thread where the supplyAsync() task is
     * executed, or in the main thread if the supplyAsync() task completes immediately (try removing
     * sleep() call to verify).
     */
    CompletableFuture<Void> completableFuture1 =
        CompletableFuture.supplyAsync(
                () -> {
                  try {
                    System.out.println("Entering :" + Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(1);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  return "\nCompletable Future";
                })
            .thenAccept(
                s -> {
                  System.out.println(
                      "thenAccept() Thread Name :" + Thread.currentThread().getName());
                  System.out.println(s + " from thenAccept()");
                });

    /**
     * To have more control over the thread that executes the callback task, you can use async
     * callbacks.
     *
     * <p>If you use thenApplyAsync() callback, then it will be executed in a different thread
     * obtained from ForkJoinPool.commonPool(). And also we can pass Executor to the
     * thenApplyAsync()
     */
    CompletableFuture<Void> completableFuture2 =
        CompletableFuture.supplyAsync(
                () -> {
                  try {
                    System.out.println("Entering :" + Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(1);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  return "\nCompletable Future";
                })
            .thenAcceptAsync(
                s -> {
                  System.out.println(
                      "thenAcceptAsync() Thread Name :" + Thread.currentThread().getName());
                  System.out.println(s + " from thenAcceptAsync()");
                });

    try {
      completableFuture.get();
      completableFuture1.get();
      completableFuture2.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  private static void combineCompletableFuture() {
    /** Combine two dependent futures using thenCompose() */

    /** Combine two independent futures using thenCombine() */

    /** Combining multiple CompletableFutures together using allOf(), anyOf() */
  }
}
