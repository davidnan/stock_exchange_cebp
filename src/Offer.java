import java.util.concurrent.Semaphore;
import java.util.UUID;

public class Offer implements Comparable<Offer>{
    private final UUID id;
    private final int traderId;
    private final Ticker company;
    private final int noOfShares;
    private final double pricePerShare;
    private final OfferType type;
    private final Semaphore semaphore;

    public Offer(int tradeId, Ticker company, int noOfShares, double pricePerShare, OfferType type){
        this.id = UUID.randomUUID();
        this.traderId = tradeId;
        this.company = company;
        this.noOfShares = noOfShares;
        this.pricePerShare = pricePerShare;
        this.type = type;
        semaphore = new Semaphore(noOfShares, true);
    }

    // Copy constructor for modification
    public Offer(Offer original, double newPrice) {
        this.id = original.id; // Keep the same UUID
        this.traderId = original.traderId;
        this.company = original.company;
        this.noOfShares = original.noOfShares;
        this.pricePerShare = newPrice;
        this.type = original.type;
        this.semaphore = original.semaphore; // Keep the same semaphore
    }

    public UUID getId() {
        return id;
    }

    public int getTraderId(){
        return this.traderId;
    }
    public Ticker getCompany(){
        return this.company;
    }
    public int getNoOfShares(){
        return this.noOfShares;
    }
    public double getPricePerShare(){
        return this.pricePerShare;
    }
    public OfferType getType(){
        return this.type;
    }
    public Semaphore getSemaphore() {
        return semaphore;
    }

    @Override
    public int compareTo(Offer other) {
        return Double.compare(this.pricePerShare, other.pricePerShare);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id.toString().substring(0, 8) + // Show first 8 chars for readability
                ", traderId=" + traderId +
                ", company=" + company +
                ", noOfShares=" + noOfShares +
                ", pricePerShare=" + pricePerShare +
                ", type=" + type +
                ", semaphore permits=" + semaphore.availablePermits() +
                '}';
    }
}