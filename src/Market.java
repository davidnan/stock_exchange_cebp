import java.util.HashMap;

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
            instance=new Market();
        }
        return instance;
    }

    public void placeOrder(Offer offer){
        if (OfferType.Selling == offer.getType()) {
            orderBook.get(offer.getCompany()).placeSellerOffer(offer);
            System.out.println("Placed sell order: " + offer.getTraderId() + " " + offer.getCompany() + " " + offer.getNoOfShares() + " shares at $" + offer.getPricePerShare() + " each.");
        } else {
            orderBook.get(offer.getCompany()).placeBuyerOffer(offer);
            System.out.println("Placed buy order: " + offer.getTraderId() + " " + offer.getCompany() + " " + offer.getNoOfShares() + " shares at $" + offer.getPricePerShare() + " each.");
        }
    }

    public void run() {
        //code to do the actual buying
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
                    Math.max(0.01, offer.getPricePerShare() - 5.0), OfferType.Selling);
            book.placeBuyerOffer(modifiedOffer);
        });

    }


}
