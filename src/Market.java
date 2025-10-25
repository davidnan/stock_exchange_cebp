public class Market {
    private static Market instance;
    private Market(){
    }
    public static synchronized Market getInstance(){
        if(instance==null){
            instance=new Market();
        }
        return instance;
    }
    public void placeOrder(OrderBook orderBook){

    }

}
