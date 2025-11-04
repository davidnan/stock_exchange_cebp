import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    public final Queue<Offer> buyOffers;
    public final Queue<Offer> sellOffers;
    private final List<Transaction> transactionHistory;
    private final ReentrantLock lock = new ReentrantLock();

    public OrderBook(){
        buyOffers = new PriorityBlockingQueue<>(100, Collections.reverseOrder());
        sellOffers = new PriorityBlockingQueue<>(100);
        transactionHistory = new ArrayList<>();
    }

    public void placeBuyerOffer(Offer offer){
        matchOrders(offer, sellOffers, buyOffers);
    }

    public void placeSellerOffer(Offer offer){
        matchOrders(offer, buyOffers, sellOffers);
    }

    public void viewHistory(){
        lock.lock();
        try {
            if (transactionHistory.isEmpty()) {
                System.out.println("No transactions recorded yet.");
                return;
            }
            System.out.println("=== TRANSACTION HISTORY ===");
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
            System.out.println("Total transactions: " + transactionHistory.size());
        } finally {
            lock.unlock();
        }
    }

    public List<Transaction> getTransactionHistory() {
        lock.lock();
        try {
            return new ArrayList<>(transactionHistory);
        } finally {
            lock.unlock();
        }
    }

    private void addTransaction(int buyerId, int sellerId, Ticker company, int shares, int sharesLeftForSeller, int sharesLeftForbuyer, double price) {
        Transaction transaction = new Transaction(buyerId, sellerId, company, shares, sharesLeftForSeller, sharesLeftForbuyer, price);
        transactionHistory.add(transaction);
        System.out.println(" [transaction] " + transaction);
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
                    // Determine buyer and seller based on offer types
                    int buyerId, sellerId;
                    double transactionPrice = topOffer.getPricePerShare();
                    int sharesLeftForSeller = topOffer.getSemaphore().availablePermits();
                    int sharesLeftForBuyer = incoming.getSemaphore().availablePermits();


                    if (incoming.getType() == OfferType.Buying) {
                        buyerId = incoming.getTraderId();
                        sharesLeftForBuyer = incoming.getSemaphore().availablePermits();
                        sellerId = topOffer.getTraderId();
                        sharesLeftForSeller = topOffer.getSemaphore().availablePermits();
                    } else {
                        buyerId = topOffer.getTraderId();
                        sharesLeftForBuyer = topOffer.getSemaphore().availablePermits();
                        sellerId = incoming.getTraderId();
                        sharesLeftForSeller = incoming.getSemaphore().availablePermits();
                    }

                    // Record the transaction with shares left for seller
                    addTransaction(buyerId, sellerId, incoming.getCompany(), sharesToTrade, sharesLeftForSeller, sharesLeftForBuyer, transactionPrice);

                    System.out.println(" [info] Executed trade: " + sharesToTrade + " shares at $" + transactionPrice +
                            " between Buyer " + buyerId + " and Seller " + sellerId +
                            " (Seller has " + sharesLeftForSeller + " shares remaining)");

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