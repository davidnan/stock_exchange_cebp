import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OfferGenerator {
    private static final Random random = new Random();

    public static List<Offer> generateOffers(int numberOfOffers, int traderId, OfferType type) {
        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < numberOfOffers; i++) {
            Ticker company = Ticker.values()[random.nextInt(Ticker.values().length)];
            int shares = generateRandomShares();
            double price = generateRandomPrice(company);

            Offer offer = new Offer(traderId, company, shares, (int) price, type);
            offers.add(offer);
        }

        return offers;
    }

    public static List<Offer> createDeterministicOffers(int traderId, OfferType type) {
        List<Offer> offers = new ArrayList<>();
        if (type == OfferType.Buying) {
            offers.add(new Offer(traderId, Ticker.AMZN, 150, 132, type));
            offers.add(new Offer(traderId, Ticker.AMZN, 100, 130, type));
            offers.add(new Offer(traderId, Ticker.AMZN, 200, 128, type));
            return offers;
        }
        offers.add(new Offer(traderId, Ticker.AMZN, 100, 125, type));
        offers.add(new Offer(traderId, Ticker.AMZN, 200, 130, type));
        offers.add(new Offer(traderId, Ticker.AMZN, 150, 128, type));
        return offers;
    }

    public static List<Offer> createHighVolumeOffers(int traderId, OfferType type) {
        List<Offer> offers = new ArrayList<>();
        if (type == OfferType.Buying) {
            offers.add(new Offer(traderId, Ticker.GOOGL, 500, 118, type));
            offers.add(new Offer(traderId, Ticker.GOOGL, 600, 115, type));
            offers.add(new Offer(traderId, Ticker.GOOGL, 700, 112, type));
            return offers;
        }
        offers.add(new Offer(traderId, Ticker.GOOGL, 500, 120, type));
        offers.add(new Offer(traderId, Ticker.GOOGL, 600, 123, type));
        offers.add(new Offer(traderId, Ticker.GOOGL, 700, 125, type));
        return offers;
    }

    private static int generateRandomShares() {
        // Generate shares between 10 and 1000 in multiples of 10
        return (random.nextInt(100) + 1) * 10;
    }

    private static double generateRandomPrice(Ticker company) {
        double basePrice = switch (company) {
            case AAPL -> 150.0;
            case GOOGL -> 120.0;
            case MSFT -> 300.0;
            case AMZN -> 130.0;
        };

        // Add random variation ±20%
        double variation = (random.nextDouble() * 0.4) - 0.2; // -0.2 to +0.2
        return basePrice * (1 + variation);
    }
}