import java.util.List;

public class Seller implements Runnable {
    private List<Offer> offers;
    private Market market;
    private int timeoutTimeMS;
    private OfferModifier offerModifier;

    public Seller(List<Offer> offer, int timeoutTimeMS) {
        this.offers = offer;
        market = Market.getInstance();
        this.timeoutTimeMS = timeoutTimeMS;
        this.offerModifier = new OfferModifier();
    }

    public void sell(Offer offer) {
        market.placeOrder(offer);
        // Check if we need to modify any existing offers
        offerModifier.processOffer(offers, offer);
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