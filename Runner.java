import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {

    private static class TotalPrice{
        private int totalPrice;

        public TotalPrice(int startPrice){
            totalPrice = startPrice;
        }

        public int getTotalPrice(){
            return totalPrice;
        }

        public void addTotalPrice(int value){
            totalPrice += value;
        }
    }

    private static class OrderProduct {
        private String productId;
        private int quantity;

        public OrderProduct(String productId, int quantity){
            this.productId = productId;
            this.quantity = quantity;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductId() {
            return productId;
        }

        public void setQuantity(int val){
            quantity = val;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    private static class Product{
        private String name;
        private int pricePerUnit;

        public Product(String name, int pricePerUnit) {
            this.name = name;
            this.pricePerUnit = pricePerUnit;
        }

        public int getPricePerUnit() {
            return pricePerUnit;
        }

        public void setPricePerUnit(int pricePerUnit) {
            this.pricePerUnit = pricePerUnit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) throws IOException {

        Map<String, LocalDateTime> orders = new HashMap<>();
        Map<String, Product> products = new HashMap<>();
        Map<String, OrderProduct> orderProducts = new HashMap<>();
        Map<String, TotalPrice> productValue = new HashMap<>();
        BufferedReader readerOrders = new BufferedReader(new FileReader("D://orders.csv"));
        BufferedReader readerProducts = new BufferedReader(new FileReader("D://products.csv"));
        BufferedReader readerOrderProducts = new BufferedReader(new FileReader("D://order_items.csv"));

        String buffer;
        //skip initial string
        readerOrders.readLine();
        while ((buffer = readerOrders.readLine()) != null){
            String[] arr = buffer.split(",");
            orders.put(arr[0], LocalDateTime.parse(arr[1]));
        }

        //skip initial string
        readerProducts.readLine();
        while ((buffer = readerProducts.readLine()) != null){
            String[] arr = buffer.split(",");
            products.put(arr[0], new Product(arr[1], Integer.parseInt(arr[2])));
        }

        //skip initial string
        readerOrderProducts.readLine();
        while ((buffer = readerOrderProducts.readLine()) != null){
            String[] arr = buffer.split(",");
            orderProducts.put(arr[0], new OrderProduct(arr[1], Integer.parseInt(arr[2])));
        }

        int neededDay = 21;
        List<String> ordersList = orders.entrySet().stream().filter(ord -> ord.getValue().getDayOfMonth() == neededDay)
                .map(Map.Entry::getKey).collect(Collectors.toList());

        for(int i = 0; i < ordersList.size(); i++){
            OrderProduct orderProduct = orderProducts.get(ordersList.get(i));
            Product product = products.get(orderProduct.productId);

            if(!productValue.containsKey(orderProduct.productId)){
                productValue.put(orderProduct.productId,
                        new TotalPrice(orderProduct.quantity * product.pricePerUnit));
            } else {
                productValue.get(orderProduct.productId).addTotalPrice(orderProduct.quantity * product.pricePerUnit);
            }
        }

        productValue.entrySet().forEach(item -> System.out.println(item.getKey() + " " + item.getValue().getTotalPrice()));

        String maxKey = productValue.entrySet().stream().findFirst().get().getKey();
        for(var entry : productValue.entrySet()){
            if(entry.getValue().getTotalPrice() > productValue.get(maxKey).getTotalPrice()){
                maxKey = entry.getKey();
            }
        }

        System.out.println(productValue.get(maxKey).totalPrice);
        System.out.println(products.get(maxKey).getName());
    }
}
