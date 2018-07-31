package multithreading.interthreadcommunication;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inter Thread communication best example is Producer Consumer
 *
 * <p>For this java 5 introduce : BlockingQueue, also implemented this problem using BlockingQueue
 * {@link ProducerConsumerPattern}
 *
 * <p>Using this we no need to use wait and notify, notifyAll
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 16 : 19 : 08)
 */
public class ProducerConsumer {
  public static void main(String[] args) {
    /** lock will be applied for this shared queue object by producer and consumer both */
    final Queue<Integer> sharedQ = new LinkedList<>();

    Thread producer = new Thread(new Producer(sharedQ));
    Thread consumer = new Thread(new Consumer(sharedQ));

    producer.start();
    consumer.start();
  }
}

/**
 * we have an for(we can also use infinite) outer loop to insert values in the list.
 *
 * <p>Inside this loop, we have a synchronized block so that only a producer or a consumer thread
 * runs at a time. This inner loop checks if the job list is full, before adding the jobs to queue,
 * the producer thread gives up the intrinsic lock on Producer and goes on the waiting state.
 *
 * <p>If the queue is empty, the control passes to below the loop and it adds a value in the list.
 */
class Producer implements Runnable {
  private static Logger logger = Logger.getLogger(MyProducer.class.getName());
  private final Queue sharedQ;

  Producer(Queue sharedQ) {
    this.sharedQ = sharedQ;
    System.out.println("Hash Code: " + this.sharedQ.hashCode());
  }

  @Override
  public void run() {
    for (int i = 1; i <= 4; i++) {
      synchronized (sharedQ) {
        /**
         * Why we should always put wait in loop?
         *
         * <p>Answer:
         * https://stackoverflow.com/questions/1038007/why-should-wait-always-be-called-inside-a-loop
         */
        // waiting condition - wait until Queue is not empty
        while (sharedQ.size() >= 1) {
          try {
            logger.log(Level.INFO, "Queue is full, waiting");
            sharedQ.wait();
          } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
          }
        }

        logger.log(Level.INFO, "Producing : " + i);
        /** To insert the job in queue */
        sharedQ.add(i);
        /** notifies the consumer thread that, now it can start consuming */
        sharedQ.notify();

        /** sleep is just use for user to see the sequential flow */
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          logger.log(Level.SEVERE, e.getMessage(), e);
        }
      }
    }
  }
}

/**
 * We have an infinite loop to extract a value from the queue.
 *
 * <p>Inside this loop, we also have an inner loop which checks if the list is empty. If it is empty
 * then we make the consumer thread give up the lock on Consumer and passes the control to Producer
 * thread for producing more jobs.
 *
 * <p>If the list is not empty, we go round the loop and removes an item from the queue.
 */
class Consumer implements Runnable {
  private static Logger logger = Logger.getLogger(MyConsumer.class.getName());
  private final Queue sharedQ;

  Consumer(Queue sharedQ) {
    this.sharedQ = sharedQ;
    System.out.println("Hash Code: " + this.sharedQ.hashCode());
  }

  @Override
  public void run() {
    while (true) {
      synchronized (sharedQ) {
        // waiting condition - wait until Queue is not empty
        while (sharedQ.size() == 0) {
          try {
            logger.log(Level.INFO, "Queue is empty, waiting");
            sharedQ.wait();
          } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            //              e.printStackTrace();
          }
        }

        Integer num = Integer.valueOf(sharedQ.poll().toString());
        logger.log(Level.INFO, "Consuming : " + num);
        /** notifies the producer thread that, now it can start producing */
        sharedQ.notify();

        /** sleep is just use for user to see the sequential flow */
        try {
          TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
          logger.log(Level.SEVERE, e.getMessage(), e);
        }

        /** Terminate condition */
        if (num == 4) break;
      }
    }
  }
}
