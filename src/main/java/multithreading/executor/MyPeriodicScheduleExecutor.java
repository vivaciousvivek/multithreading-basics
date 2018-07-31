package multithreading.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 15 : 19 : 08)
 */
public class MyPeriodicScheduleExecutor {
  public static void main(String[] args) {
    System.out.println("\n\n******Periodic Scheduler Executor******");

    System.out.println("\nCreating Scheduler Executor Service of pool size 1...");
    MyPeriodicScheduleExecutor myPeriodicScheduleExecutor = new MyPeriodicScheduleExecutor();
    myPeriodicScheduleExecutor.scheduleMe();
  }

  public void scheduleMe() {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    Runnable periodicTask =
        () -> {
          System.out.println(
              "Inside Periodic Scheduler Task at : "
                  + System.currentTimeMillis()
                  + " : "
                  + Thread.currentThread().getName());
        };

    System.out.println(
        "Scheduling task to be executed every 2 seconds with an initial delay of 0 seconds : "
            + System.currentTimeMillis());
    scheduledExecutorService.scheduleAtFixedRate(periodicTask, 0, 2, TimeUnit.SECONDS);
  }
}
