import java.util.List;

public class Seller implements Runnable{
    private List<Offer> offers;
    private Market market;
    int timeoutTimeMS;

    public Seller(List<Offer> offer, int timeoutTimeMS) {
        this.offers=offer;
        market = Market.getInstance();
        this.timeoutTimeMS = timeoutTimeMS;
    }

    public void sell(Offer offer) {
        market.placeOrder(offer);
    }

    @Override
    public void run() {
        try {
            for (Offer offer : offers) {
                sell(offer);
                Thread.sleep(timeoutTimeMS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}