package multithreading.callablefuture;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

/**
 * A Callable is similar to Runnable except that it can return a result and throw a checked
 * exception.
 *
 * <p>Just like Runnable, we can submit a Callable to an executor service for execution. But what
 * about the Callable’s result? How do we access it?
 *
 * <p>The submit() method of executor service submits the task for execution by a thread. However,
 * it doesn’t know when the result of the submitted task will be available.
 *
 * <p>Therefore, it returns a special type of value called a <b>Future</b> which can be used to
 * fetch the result of the task when it is available.
 *
 * <p>The concept of Future is similar to <b>Promise</b> in other languages like <b>Javascript</b>.
 * It represents the result of a computation that will be completed at a later point of time in
 * future.
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 17 : 29 : 08)
 */
public class CallableAndFuture {
  public static void main(String[] args) {
    System.out.println("****Future from Callable****\n");
    futureFromCallable();

    System.out.println("\n****Return Future from Callable within time limit****\n");
    futureFromCallableInTimeLimit();

    /** Cancelling a Future */
    System.out.println("\n****Cancelling a Future****\n");
    cancellingFuture();
  }

  /**
   * The future.get() method blocks and waits for the task to complete. If you call an API from a
   * remote service in the callable task and the remote service is down, then future.get() will
   * block forever, which will make the application unresponsive.
   *
   * <p>To guard against this fact, we can add a timeout in the get(timeout, TimeUnit)
   *
   * <p>The future.get(timeout, TimeUnit) method will throw a TimeoutException if the task is not
   * completed within the specified time.
   */
  private static void futureFromCallableInTimeLimit() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Callable callable =
        () -> {
          System.out.println("Enter into Callable");
          TimeUnit.SECONDS.sleep(2);
          return "(Returned from lambda Callable)";
        };

    System.out.println("Submitting Callable to Executor");
    Future<String> future = executorService.submit(callable);

    // Retrieving the result of the future and will wait for given second, if won't get the result
    // within time throws TimeoutException
    String result = null;
    try {
      // change timeout 3 to 2 get the TimeoutException
      result = future.get(3, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }

    System.out.println("Result from Callable : " + result);

    executorService.shutdown();
  }

  /** Cancel the future and to get the value safely without getting the Exception */
  private static void cancellingFuture() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    LocalTime statTime = LocalTime.now();
    Future<String> future = executorService.submit(new MyCallable());

    while (!future.isDone()) {
      System.out.println("Task is still not done...");
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (ChronoUnit.SECONDS.between(statTime, LocalTime.now()) >= 1) {
        future.cancel(true);
      }
    }

    if (!future.isCancelled()) {
      System.out.println("Task completed ! retrieving the result");
      try {
        System.out.println(future.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Task was cancelled !");
    }

    executorService.shutdown();
  }

  /** Get Future object from Callable */
  private static void futureFromCallable() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Callable callable =
        () -> {
          System.out.println("Enter into Callable");
          TimeUnit.SECONDS.sleep(2);
          return "(Returned from lambda Callable)";
        };

    System.out.println("Submitting Callable to Executor");
    Future<String> future = executorService.submit(callable);

    System.out.println(
        "This line will be execute asynchronously i.e. won't wait for future object is resolved or not");

    // Retrieving the result of the future
    String result = null;
    try {
      result = future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    System.out.println("Result from Callable : " + result);

    executorService.shutdown();
  }
}

class MyCallable implements Callable {

  @Override
  public Object call() throws Exception {
    System.out.println("Enter into My Callable");
    TimeUnit.SECONDS.sleep(2);
    return "(Returned from lambda Callable)";
  }
}
