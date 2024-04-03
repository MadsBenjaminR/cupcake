package app.entities;

public class Orderline {

    private int orderline_id;
    private int quantity;
    private int price;
    private int order_id;
    private int bottom_id;
    private int top_id;

    public Orderline(int orderline_id, int quantity, int price, int order_id, int bottom_id, int top_id) {
        this.orderline_id = orderline_id;
        this.quantity = quantity;
        this.price = price;
        this.order_id = order_id;
        this.bottom_id = bottom_id;
        this.top_id = top_id;
    }
}