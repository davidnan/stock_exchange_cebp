import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Market implements Runnable {
    private static Market instance;
    private static HashMap<Ticker, OrderBook> orderBook;

    private Market(){
        orderBook = generateOrderBooks();
    }

    private HashMap<Ticker, OrderBook> generateOrderBooks(){
        HashMap<Ticker, OrderBook> books = new HashMap<>();
        for (Ticker ticker : Ticker.values()) {
            books.put(ticker, new OrderBook());
        }
        return books;
    }

    public static synchronized Market getInstance(){
        if (instance == null) {
            instance = new Market();
        }
        return instance;
    }

    public void placeOrder(Offer offer){
        if (OfferType.Selling == offer.getType()) {
            orderBook.get(offer.getCompany()).placeSellerOffer(offer);
            System.out.println("Placed sell order: " + offer.getTraderId() + " " + offer.getCompany() + " " + offer.getNoOfShares() + " shares at $" + offer.getPricePerShare() + " each. [ID: " + offer.getId().toString().substring(0, 8) + "]");
        } else {
            orderBook.get(offer.getCompany()).placeBuyerOffer(offer);
            System.out.println("Placed buy order: " + offer.getTraderId() + " " + offer.getCompany() + " " + offer.getNoOfShares() + " shares at $" + offer.getPricePerShare() + " each. [ID: " + offer.getId().toString().substring(0, 8) + "]");
        }
    }

    // New method to modify existing offer by UUID
    public boolean modifyExistingOffer(UUID offerId, double newPrice) {
        // We need to search all order books for this offer
        for (Ticker ticker : Ticker.values()) {
            boolean modified = orderBook.get(ticker).modifyOfferPrice(offerId, newPrice);
            if (modified) {
                return true;
            }
        }
        return false;
    }

    // Method to get active offer IDs for a trader
    public List<UUID> getActiveOfferIds(int traderId) {
        List<UUID> activeOffers = new ArrayList<>();
        for (Ticker ticker : Ticker.values()) {
            activeOffers.addAll(orderBook.get(ticker).getActiveOfferIds(traderId));
        }
        return activeOffers;
    }

    public void run() {
        // Code to do the actual buying
    }

    public void printTransactionHistory() {
        System.out.println("\n=== GLOBAL TRANSACTION HISTORY ===");
        boolean hasTransactions = false;

        for (Ticker ticker : Ticker.values()) {
            List<Transaction> tickerTransactions = orderBook.get(ticker).getTransactionHistory();
            if (!tickerTransactions.isEmpty()) {
                hasTransactions = true;
                System.out.println("\n--- " + ticker + " Transactions ---");
                for (Transaction transaction : tickerTransactions) {
                    System.out.println(transaction);
                }
            }
        }

        if (!hasTransactions) {
            System.out.println("No transactions have been executed yet.");
        }
    }

    public void printLeftoverOrders() {
        for (Ticker ticker : Ticker.values()) {
            if (orderBook.get(ticker).buyOffers.isEmpty() && orderBook.get(ticker).sellOffers.isEmpty()) {
                continue; // Skip if there are no leftover orders
            }
            System.out.println("Order Book for " + ticker + ":");
            OrderBook book = orderBook.get(ticker);
            System.out.println("Buy Offers: " + book.buyOffers);
            System.out.println("Sell Offers: " + book.sellOffers);
        }
    }

    public void tryToBuyLeftoverOrders() {
        OrderBook book = orderBook.get(Ticker.AMZN);
        book.buyOffers.forEach(offer -> {
            Offer modifiedOffer = new Offer(offer.getTraderId(), offer.getCompany(), offer.getNoOfShares(),
                    offer.getPricePerShare() + 5.0, OfferType.Buying);
            book.placeSellerOffer(modifiedOffer);
        });
        book.sellOffers.forEach(offer -> {
            Offer modifiedOffer = new Offer(offer.getTraderId(), offer.getCompany(), offer.getNoOfShares(),
                    offer.getPricePerShare() - 5, OfferType.Selling);
            book.placeBuyerOffer(modifiedOffer);
        });
    }
}