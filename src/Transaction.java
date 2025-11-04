import java.time.LocalDateTime;

public class Transaction {
    private final int buyerId;
    private final int sellerId;
    private final Ticker company;
    private final int shares;
    private final int remainingSharesSeller;
    private final int remainingSharesBuyer;
    private final double pricePerShare;
    private final LocalDateTime timestamp;

    public Transaction(int buyerId, int sellerId, Ticker company, int shares, int remainingSharesSeller, int remainingSharesBuyer, double pricePerShare) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.company = company;
        this.shares = shares;
        this.remainingSharesSeller = remainingSharesSeller;
        this.remainingSharesBuyer = remainingSharesBuyer;
        this.pricePerShare = pricePerShare;
        this.timestamp = LocalDateTime.now();
    }

    public int getBuyerId() { return buyerId; }
    public int getSellerId() { return sellerId; }
    public Ticker getCompany() { return company; }
    public int getShares() { return shares; }
    public double getPricePerShare() { return pricePerShare; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Transaction{ buyer=%d, seller=%d, stock=%s, shares=%d, remaining shares SELLER =%d, remaining shares BUYER =%d, price=$%.2f, time=%s }",
                buyerId, sellerId, company, shares, remainingSharesSeller, remainingSharesBuyer, pricePerShare, timestamp);
    }
}