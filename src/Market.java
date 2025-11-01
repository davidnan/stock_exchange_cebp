public class Market implements Runnable {
    private static Market instance;
    private OrderBook orderBook;

    private Market(){
        orderBook = new OrderBook();
    }

    public static synchronized Market getInstance(){
        if(instance==null){
            instance=new Market();
        }
        return instance;
    }

    public void placeOrder(Offer offer){
        if (OfferType.Selling == offer.getType()) {
            orderBook.placeSellerOffer(offer);
        } else {
            orderBook.placeBuyerOffer(offer);
        }
    }

    public void run() {
        //code to do the actual buying
    }

}
