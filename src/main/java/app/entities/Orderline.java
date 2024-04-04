package app.entities;

public class Orderline {

    private int orderlineId;
    private int quantity;
    private int price;
    private int orderId;
    private int bottomId;
    private int topId;

    public Orderline(int orderlineId, int quantity, int price, int orderId, int bottomId, int topId) {
        this.orderlineId = orderlineId;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.bottomId = bottomId;
        this.topId = topId;
    }

    public Orderline(int orderlineId, int quantity, int price) {
        this.orderlineId = orderlineId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderlineId() {
        return orderlineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public int getTopId() {
        return topId;
    }
}