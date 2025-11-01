import java.util.List;

public class Buyer implements Runnable {
    private List<Offer> offers;
    private Market market;
    int timeoutTimeMS;

    public Buyer(List<Offer> offer, int timeoutTimeMS) {
        this.offers=offer;
        this.market = Market.getInstance();
        this.timeoutTimeMS = timeoutTimeMS;
    }

    public void buy(Offer offer) {
        market.placeOrder(offer);
    }

    @Override
    public void run() {
        try {
            for (Offer offer : offers) {
                buy(offer);
                Thread.sleep(timeoutTimeMS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
