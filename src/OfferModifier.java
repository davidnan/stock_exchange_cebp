import java.util.*;
import java.util.Random;

public class OfferModifier {
    private static final Random random = new Random();
    private int offerCounter = 0;
    private final Map<Integer, List<UUID>> traderOffers = new HashMap<>(); // Track offers by trader

    public void processOffer(Offer newOffer) {
        offerCounter++;

        // Store the new offer UUID
        traderOffers.computeIfAbsent(newOffer.getTraderId(), k -> new ArrayList<>()).add(newOffer.getId());

        // Modify first offer every 3 offers if it still exists
        if (offerCounter % 3 == 0 && !traderOffers.isEmpty()) {
            modifyFirstActiveOffer(newOffer.getTraderId());
        }
    }

    private void modifyFirstActiveOffer(int traderId) {
        List<UUID> activeOffers = Market.getInstance().getActiveOfferIds(traderId);

        if (!activeOffers.isEmpty()) {
            // Get the first active offer
            UUID firstOfferId = activeOffers.get(0);
            modifySpecificOffer(firstOfferId);
        } else {
            System.out.println(" [modifier] No active offers found for Trader " + traderId + " to modify");
        }
    }

    private void modifySpecificOffer(UUID offerId) {
        // We need a way to get the current offer's price to calculate the new price
        // Since we don't have direct access, we'll use a more reasonable modification approach

        // Option 1: Use percentage-based modification on a reasonable base price
        double basePrice = getBasePriceForModification();
        double variation = (random.nextDouble() * 0.2) - 0.1; // -10% to +10%
        double newPrice = basePrice * (1 + variation);
        newPrice = Math.max(0.01, newPrice); // Ensure positive price
        newPrice = Math.round(newPrice * 100.0) / 100.0; // Round to 2 decimal places

        boolean modified = Market.getInstance().modifyExistingOffer(offerId, newPrice);

        if (modified) {
            System.out.println(" [modifier] Successfully modified offer " + offerId.toString().substring(0, 8) + " to $" + newPrice);
        } else {
            System.out.println(" [modifier] Failed to modify offer " + offerId.toString().substring(0, 8) + " - may have been executed");
            removeOfferFromTracking(offerId);
        }
    }

    private double getBasePriceForModification() {
        // Use reasonable base prices for each company
        // You could make this more sophisticated by tracking actual market prices
        return switch (random.nextInt(4)) {
            case 0 -> 150.0; // AAPL
            case 1 -> 120.0; // GOOGL
            case 2 -> 300.0; // MSFT
            case 3 -> 130.0; // AMZN
            default -> 100.0;
        };
    }

    private void removeOfferFromTracking(UUID offerId) {
        for (List<UUID> offerList : traderOffers.values()) {
            offerList.remove(offerId);
        }
    }
}