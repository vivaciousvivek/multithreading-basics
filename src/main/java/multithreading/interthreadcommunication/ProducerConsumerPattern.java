package multithreading.interthreadcommunication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation using BlockingQueue(blocking queue allows elements to be accessed in FIFO)
 *
 * @author VIVEK KUMAR SINGH
 * @since (2018 - 05 - 11 16 : 19 : 08)
 */
public class ProducerConsumerPattern {
  public static void main(String[] args) {
    // shared object
    BlockingQueue<Integer> sharedQ = new LinkedBlockingQueue<>();

    // creating producer and consumer
    Thread producer = new Thread(new Producer(sharedQ));
    Thread consumer = new Thread(new Consumer(sharedQ));

    // starting producer and consumer
    producer.start();
    consumer.start();
  }
}

class MyProducer implements Runnable {
  private static Logger logger = Logger.getLogger(MyProducer.class.getName());
  private final BlockingQueue sharedQ;

  MyProducer(BlockingQueue sharedQ) {
    this.sharedQ = sharedQ;
    System.out.println("Hash Code: " + this.sharedQ.hashCode());
  }

  @Override
  public void run() {
    for (int i = 1; i <= 4; i++) {
      // waiting condition - wait until Queue is not empty
      logger.log(Level.INFO, "Queue is full, waiting");

      try {
        logger.log(Level.INFO, "Producing : " + i);
        /** To insert the job in queue and will notified automatically */
        sharedQ.put(i);
        /** sleep is just use for user to see the sequential flow */
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
      }
    }
  }
}

class MyConsumer implements Runnable {
  private static Logger logger = Logger.getLogger(MyConsumer.class.getName());
  private final BlockingQueue sharedQ;

  MyConsumer(BlockingQueue sharedQ) {
    this.sharedQ = sharedQ;
    System.out.println("Hash Code: " + this.sharedQ.hashCode());
  }

  @Override
  public void run() {
    while (true) {
      // waiting condition - wait until Queue is not empty
      logger.log(Level.INFO, "Queue is empty, waiting");

      try {
        logger.log(Level.INFO, "Consuming : " + sharedQ.take());
        /** sleep is just use for user to see the sequential flow */
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
      }
    }
  }
}
