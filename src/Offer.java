import java.util.concurrent.Semaphore;

public class Offer implements Comparable<Offer>{
    private final int traderId;
    private final Ticker company;
    private final int noOfShares;
    private final double pricePerShare;
    private final OfferType type;
    private final Semaphore semaphore;

    public Offer(int tradeId, Ticker company, int noOfShares,double pricePerShare,OfferType type){
        this.traderId=tradeId;
        this.company=company;
        this.noOfShares=noOfShares;
        this.pricePerShare=pricePerShare;
        this.type=type;
        semaphore = new Semaphore(noOfShares, true);
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
        return Double.compare(this.pricePerShare,other.pricePerShare);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "traderId=" + traderId +
                ", company=" + company +
                ", noOfShares=" + noOfShares +
                ", pricePerShare=" + pricePerShare +
                ", type=" + type +
                ", semaphore permits=" + semaphore.availablePermits() +
                '}';
    }
}
