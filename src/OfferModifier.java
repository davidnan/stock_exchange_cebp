import java.util.List;
import java.util.Random;

public class OfferModifier {
    private static final Random random = new Random();
    private int offerCounter = 0;

    public void processOffer(List<Offer> offers, Offer currentOffer) {
        offerCounter++;

        // Modify first offer every 3 offers if it still exists
        if (offerCounter % 3 == 0 && !offers.isEmpty()) {
            Offer firstOffer = offers.get(0);
            if (firstOffer.getSemaphore().availablePermits() > 0) {
                modifyOffer(firstOffer);
            }
        }
    }

    private void modifyOffer(Offer offer) {
        // Create a modified version of the offer with different price
        double newPrice = calculateModifiedPrice(offer.getPricePerShare());
        int traderId = offer.getTraderId();
        Ticker company = offer.getCompany();
        int shares = offer.getNoOfShares();
        OfferType type = offer.getType();

        // Create new modified offer
        Offer modifiedOffer = new Offer(traderId, company, shares, newPrice, type);

        System.out.println(" [modified] Modified offer from Trader " + traderId +
                ": " + offer.getPricePerShare() + " -> " + newPrice +
                " for " + company + " (" + type + ")");

        // Place the modified order in the market
        Market.getInstance().placeOrder(modifiedOffer);
    }

    private double calculateModifiedPrice(double originalPrice) {
        // Modify price by ±10%
        double variation = (random.nextDouble() * 0.2) - 0.1; // -10% to +10%
        double newPrice = originalPrice * (1 + variation);

        // Ensure price doesn't go below 0.01
        return Math.max(0.01, newPrice);
    }

    public void resetCounter() {
        offerCounter = 0;
    }
}