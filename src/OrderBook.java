import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class OrderBook {
    PriorityBlockingQueue<Offer> buyOffers;
    PriorityBlockingQueue<Offer> sellOffers;
    List<Transaction> transactions;

    public OrderBook(){
        buyOffers = new PriorityBlockingQueue<>(100, Collections.reverseOrder()); // ca sa avem buy offer-ul cu cel mai mare pret mai intai
        sellOffers = new PriorityBlockingQueue<>(100);
        transactions = Collections.synchronizedList(new ArrayList<>());
    }

    public void placeBuyerOffer(Offer offer){
        buyOffers.add(offer);
    }

    public void placeSellerOffer(Offer offer){
        sellOffers.add(offer);
    }

    public void viewHistory(){
        transactions.forEach(System.out::println);
    }
}
