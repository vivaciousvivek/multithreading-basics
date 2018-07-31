package multithreading;
/**
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 15 : 19 : 08)
 */
public class MyThread {
  public static void main(String[] args) {
    System.out.println("Inside : " + Thread.currentThread().getName());

    System.out.println("\n1. Creating thread - (New) State");
    /**
     * By Extending Thread class, this should not be used because here we already extended the
     * Thread class so that we can't extend any class further because of java doesn't support
     * multiple inheritance using class.
     */
    //    Thread thread = new ThreadExtend();
    /**
     * By Implementing Runnable interface, this approach should be used because it allow us to use
     * extend any super class.
     *
     * <p>And Thread class also implementing Runnable interface, so it is better to go with Runnable
     * interface directly
     */
    Thread thread = new Thread(new ThreadImplement());
    System.out.println("Thread State : " + thread.getState());

    System.out.println(
        "\n2. Executing thread - (Runnable) state and after start() method \n\n3. thread will be in (Running) state "
            + "when thread scheduler will select to run from queued threads");
    thread.start();
    System.out.println("Thread State : " + thread.getState());
  }
}

class ThreadExtend extends Thread {
  @Override
  public void run() {
    System.out.println("Inside Extended Thread : " + Thread.currentThread().getName());
  }
}

class ThreadImplement implements Runnable {

  @Override
  public void run() {
    System.out.println("Inside Implemented Runnable : " + Thread.currentThread().getName());
  }
}
