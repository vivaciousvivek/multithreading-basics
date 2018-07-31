package multithreading;

import java.util.concurrent.TimeUnit;

/**
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 15 : 19 : 08)
 */
public class PausingThread {
  public static void main(String[] args) {
    /** Implementing Runnable interface using Lambda */
    System.out.println("\n1. Creating threads - (New) State");
    // created thread1
    Thread thread1 =
        new Thread(
            () -> {
              System.out.println("Entering :" + Thread.currentThread().getName());

              try {
                TimeUnit.SECONDS.sleep(2);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("Exiting :" + Thread.currentThread().getName());
            });
    System.out.println("(Thread1 State) : " + thread1.getState());

    // created thread2
    Thread thread2 =
        new Thread(
            () -> {
              System.out.println("Entering :" + Thread.currentThread().getName());

              try {
                TimeUnit.SECONDS.sleep(4);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("Exiting :" + Thread.currentThread().getName());
            });
    System.out.println("(Thread2 State) : " + thread2.getState());

    System.out.println("\n2. Executing thread 0");
    thread1.start();
    System.out.println("(Thread1 State) : " + thread1.getState());
    System.out.println("(Thread2 State) : " + thread2.getState());
    System.out.println("Time : " + System.currentTimeMillis());

    try {
//            System.out.println("\n2. Waiting thread-0 to complete");
//            thread1.join();
      System.out.println("\n2. Waiting thread-0 for only 1 second to complete");
      thread1.join(1000);
      System.out.println("(Thread1 State) : " + thread1.getState());
      System.out.println("(Thread2 State) : " + thread2.getState());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Time : " + System.currentTimeMillis());
    System.out.println("\n2. Executing thread 1");
    thread2.start();
    System.out.println("(Thread1 State) : " + thread1.getState());
    System.out.println("(Thread2 State) : " + thread2.getState());
  }
}
