package concurrency.java2.chapter1;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShopperChipsCyclicBarrierAndCountDownLatch extends Thread {
    private static Lock pencil = new ReentrantLock();
    public static int bagOfChips = 1;
    //    private static CyclicBarrier fistBump = new CyclicBarrier(10);
    private static CountDownLatch fistBump = new CountDownLatch(5); //attention - number of latch must be equeals as threads

    public ShopperChipsCyclicBarrierAndCountDownLatch(String name) {
        this.setName(name);
    }

    public void run() {
        if (this.getName().contains("Olivia")) {
            fistBump.countDown();
            pencil.lock();
            try {
                bagOfChips += 3;
                System.out.println(this.getName() + " added three bags of chips");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pencil.unlock();
            }
        } else { //is Baron thread
            try {
                fistBump.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pencil.lock();
            try {
                bagOfChips *= 2;
                System.out.println(this.getName() + " doubled bags of ships");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pencil.unlock();
            }
        }
    }
}


public class RaceConditionDemo {
    public static void main(String[] args) throws InterruptedException {
        ShopperChipsCyclicBarrierAndCountDownLatch[] shopperChips = new ShopperChipsCyclicBarrierAndCountDownLatch[10];

        for (int i = 0; i < shopperChips.length / 2; i++) {
            shopperChips[2 * i] = new ShopperChipsCyclicBarrierAndCountDownLatch("Barton-" + i);
            shopperChips[2 * i + 1] = new ShopperChipsCyclicBarrierAndCountDownLatch("Olivia-" + i);
        }

        for (ShopperChipsCyclicBarrierAndCountDownLatch shopperChip : shopperChips) {
            shopperChip.start();
        }

        for (ShopperChipsCyclicBarrierAndCountDownLatch shopperChip : shopperChips) {
            shopperChip.join();
        }

        System.out.println("added bags of chips + " + ShopperChipsCyclicBarrierAndCountDownLatch.bagOfChips);
    }


}
