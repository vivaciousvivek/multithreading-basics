package multithreading.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * It becomes a problem when your application requires creating 20 or 30 or more threads for running
 * tasks concurrently.
 *
 * <p>Manage threads using executor(A framework for creating and managing threads i.e pool of
 * threads and Task submission and execution i.e You can submit a task to be executed now or
 * schedule them to be executed later or make them execute periodically).
 *
 * <p>Thread Pool :- A thread pool is nothing but a bunch of worker threads that exist separately
 * from the Runnable or Callable tasks and is managed by the executor.
 *
 * <p>So, it makes sense to separate thread creation and management of the application.
 *
 * <p>Java provides three executor interfaces and one class:
 *
 * <p>1. Executor :- A simple interface that contains a method called execute() to launch a task
 * specified by a Runnable object.
 *
 * <p>2. ExecutorService :- A sub-interface of Executor that adds functionality to manage the
 * lifecycle of the tasks. It also provides a submit() method whose overloaded versions can accept a
 * Runnable as well as a Callable object.
 *
 * <p>3. ScheduledExecutorService :- A sub-interface of ExecutorService. It adds functionality to
 * schedule the execution of the tasks.
 *
 * <p>Executors class : contains factory methods to create different kind of Executor services.
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 15 : 19 : 08)
 */
public class ExecutorFramework {
  public static void main(String[] args) {
    System.out.println("Creating Executor Service...");
    /**
     * newSingleThreadExecutor() :- creates single worker thread for executing tasks. If a task is
     * submitted for execution and the thread is currently busy executing another task, then the new
     * task will wait in a queue until the thread is free to execute it.
     */
    //    ExecutorService executorService = Executors.newSingleThreadExecutor();

    System.out.println("Creating Executor Service with a thread pool of Size 2");
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    Runnable task1 =
        () -> {
          System.out.println("Inside Task1 : " + Thread.currentThread().getName());
          try {
            TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        };

    Runnable task2 =
        () -> {
          System.out.println("Inside Task2 : " + Thread.currentThread().getName());
          try {
            TimeUnit.SECONDS.sleep(4);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        };

    Runnable task3 =
        () -> {
          System.out.println("Inside Task3 : " + Thread.currentThread().getName());
          try {
            TimeUnit.SECONDS.sleep(3);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        };

    System.out.println("\nSubmit the tasks to the executor service...");
    executorService.submit(task1);
    executorService.submit(task2);
    executorService.submit(task3);

    /**
     * ExecutorService provides two methods for shutting down an executor -
     *
     * <p>shutdown() - it stops accepting new tasks, waits for previously submitted tasks to
     * execute, and then terminates the executor.
     *
     * <p>shutdownNow() - this method interrupts the running task and shuts down the executor
     * immediately.
     */
    System.out.println("\nShutting down the Executor Service...");
    executorService.shutdown();
  }
}
