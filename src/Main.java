import java.util.List;

public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            List<Offer> buyOffers = OfferGenerator.generateOffers(50, i, OfferType.Buying);
            Buyer buyer = new Buyer(buyOffers, 100 + i * 50);
            //de creat thread cu buyer-ul

            List<Offer> sellOffers = OfferGenerator.generateOffers(50, i + 3, OfferType.Selling);
            Seller seller = new Seller(sellOffers, 150 + i*50);
            //de creat thread cu seller-ul
        }

        // Start market processing in a separate thread
        Market market = Market.getInstance();
        //de creat thread cu market-ul
    }
}