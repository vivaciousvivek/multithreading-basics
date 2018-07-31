package multithreading.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Pool size of N*(1+ W/S) for maximum efficiency.
 *
 * <p>where :
 *
 * <p>N processors, waiting time(W) and service time(S) for a request.
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 15 : 19 : 08)
 */
public class MyScheduleExecutor {
  public static void main(String[] args) {
    System.out.println("\n\n******Scheduler Executor******");

    System.out.println("\nCreating Scheduler Executor Service of pool size 1...");
    MyScheduleExecutor myScheduleExecutor = new MyScheduleExecutor();
    myScheduleExecutor.scheduleMe();
  }

  public void scheduleMe() {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    Runnable task1 =
        () -> {
          System.out.println(
              "Inside Scheduler Task1 at : "
                  + System.currentTimeMillis()
                  + " : "
                  + Thread.currentThread().getName());
          try {
            TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        };

    System.out.println(
        "Submitting task at " + System.currentTimeMillis() + " , to be executed after 5 seconds.");
    scheduledExecutorService.schedule(task1, 5, TimeUnit.SECONDS);

    scheduledExecutorService.shutdown();
  }
}
