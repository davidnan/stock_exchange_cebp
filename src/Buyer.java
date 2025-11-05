import java.util.List;

public class Buyer implements Runnable {
    private List<Offer> offers;
    private Market market;
    private int timeoutTimeMS;
    private OfferModifier offerModifier;

    public Buyer(List<Offer> offer, int timeoutTimeMS) {
        this.offers = offer;
        this.market = Market.getInstance();
        this.timeoutTimeMS = timeoutTimeMS;
        this.offerModifier = new OfferModifier();
    }

    public void buy(Offer offer) {
        market.placeOrder(offer);
        // Track the offer for potential modification
        offerModifier.processOffer(offer);
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