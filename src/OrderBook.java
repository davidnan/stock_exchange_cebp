import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    private final Queue<Offer> buyOffers;
    private final Queue<Offer> sellOffers;
    private final ReentrantLock lock = new ReentrantLock();

    public OrderBook(){
        buyOffers = new PriorityBlockingQueue<>(100, Collections.reverseOrder());
        sellOffers = new PriorityBlockingQueue<>(100);
    }

    public void placeBuyerOffer(Offer offer){
        matchOrders(offer, sellOffers, buyOffers);
    }

    public void placeSellerOffer(Offer offer){
        matchOrders(offer, buyOffers, sellOffers);
    }

    public void viewHistory(){
        System.out.println("Transaction history is not being recorded.");
    }

    private void matchOrders(Offer incoming, Queue<Offer> oppositeOffers, Queue<Offer> sameSideOffers) {
        System.out.println(" [info] Matching incoming offer: " + incoming);

        while (incoming.getSemaphore().availablePermits() > 0 && !oppositeOffers.isEmpty()) {
            Offer topOffer;

            lock.lock();
            try {
                topOffer = oppositeOffers.peek();
                if (topOffer == null) {
                    break;
                }

                boolean isPriceMismatch =
                        (incoming.getType() == OfferType.Buying && incoming.getPricePerShare() < topOffer.getPricePerShare()) ||
                        (incoming.getType() == OfferType.Selling && incoming.getPricePerShare() > topOffer.getPricePerShare());

                if (isPriceMismatch) {
                    System.out.println(" [debug] Price mismatch for incoming "
                            + incoming + " and top " + topOffer + ". Stopping match.");
                    break;
                }
            } finally {
                lock.unlock();
            }

            int sharesToTrade = Math.min(incoming.getSemaphore().availablePermits(), topOffer.getSemaphore().availablePermits());
            if (sharesToTrade <= 0) {
                continue;
            }

            System.out.println(" [debug] Attempting to trade " + sharesToTrade + " shares with " + topOffer);

            if (topOffer.getSemaphore().tryAcquire(sharesToTrade)) {
                if (incoming.getSemaphore().tryAcquire(sharesToTrade)) {
                    System.out.println(" [info] Executed trade: " + sharesToTrade + " shares at $" + topOffer.getPricePerShare() +
                                       " between Trader " + incoming.getTraderId() + " and Trader " + topOffer.getTraderId());

                    if (topOffer.getSemaphore().availablePermits() == 0) {
                        lock.lock();
                        try {
                            if (topOffer == oppositeOffers.peek()) {
                                oppositeOffers.poll();
                                System.out.println(" [info] Top offer fully matched and removed: " + topOffer);
                            }
                        } finally {
                            lock.unlock();
                        }
                    }
                } else {
                    topOffer.getSemaphore().release(sharesToTrade);
                    System.out.println(" [warn] Could not acquire from incoming offer; trade aborted. Releasing permits from top offer.");
                    break;
                }
            }
        }

        if (incoming.getSemaphore().availablePermits() > 0) {
            sameSideOffers.offer(incoming);
            System.out.println(" [info] Added remaining offer to order book: " + incoming);
        }
    }
}
