import java.util.concurrent.Semaphore;

public class Offer implements Comparable<Offer>{
    private int traderId;
    private String company;
    private int noOfShares;
    private double pricePerShare;
    private OfferType type;
    private Semaphore semaphore;

    public Offer(int tradeId,String company, int noOfShares,double pricePerShare,OfferType type){
        this.traderId=tradeId;
        this.company=company;
        this.noOfShares=noOfShares;
        this.pricePerShare=pricePerShare;
        this.type=type;
        this.semaphore=new Semaphore(noOfShares,true);
    }

    public int getTraderId(){
        return this.traderId;
    }
    public String getCompany(){
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


    @Override
    public int compareTo(Offer other) {
        return Double.compare(this.pricePerShare,other.pricePerShare);
    }
}
