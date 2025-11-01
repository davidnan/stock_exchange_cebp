import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OfferGenerator {
    private static final String[] COMPANIES = {"AAPL", "GOOGL", "MSFT", "AMZN"};
    private static final Random random = new Random();

    public static List<Offer> generateOffers(int numberOfOffers, int traderId, OfferType type) {
        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < numberOfOffers; i++) {
            String company = COMPANIES[random.nextInt(COMPANIES.length)];
            int shares = generateRandomShares();
            double price = generateRandomPrice(company);

            Offer offer = new Offer(traderId, company, shares, (int) price, type);
            offers.add(offer);
        }

        return offers;
    }

    private static int generateRandomShares() {
        // Generate shares between 10 and 1000 in multiples of 10
        return (random.nextInt(100) + 1) * 10;
    }

    private static double generateRandomPrice(String company) {
        double basePrice = switch (company) {
            case "AAPL" -> 150.0;
            case "GOOGL" -> 120.0;
            case "MSFT" -> 300.0;
            case "AMZN" -> 130.0;
            default -> 100.0;
        };

        // Add random variation ±20%
        double variation = (random.nextDouble() * 0.4) - 0.2; // -0.2 to +0.2
        return basePrice * (1 + variation);
    }
}