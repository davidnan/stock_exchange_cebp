import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        for (int i = 1; i <= 3; i++) {
            List<Offer> buyOffers = OfferGenerator.generateOffers(10, i, OfferType.Buying);
            //List<Offer> buyOffers = OfferGenerator.createHighVolumeOffers(i, OfferType.Buying);
            //List<Offer> buyOffers = OfferGenerator.createDeterministicOffers(i, OfferType.Buying);
            Buyer buyer = new Buyer(buyOffers, 100 + i * 50);
            executor.submit(buyer);

            List<Offer> sellOffers = OfferGenerator.generateOffers(10, i + 3, OfferType.Selling);
            //List<Offer> sellOffers = OfferGenerator.createHighVolumeOffers(i, OfferType.Selling);
            //List<Offer> sellOffers = OfferGenerator.createDeterministicOffers(i + 3, OfferType.Selling);
            Seller seller = new Seller(sellOffers, 150 + i*50);

            executor.submit(seller);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        new Thread(() -> {
            Market.getInstance().printLeftoverOrders();
            Market.getInstance().printTransactionHistory();
//            Market.getInstance().tryToBuyLeftoverOrders();
//            Market.getInstance().printLeftoverOrders();
        }).start();

    }
}