package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CreateACupcakeMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartLineController {


    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> createACupcake(ctx, connectionPool));
        app.post("/cart", ctx -> CartLineController.orderLineSum(ctx, connectionPool));
        app.post("/pay", ctx -> CartLineController.insertInhistory(ctx, connectionPool));


    }


    public static void createACupcake(Context ctx, ConnectionPool connectionPool) {

        List<Top> tops = new ArrayList<>();
        List<Bottom> bottoms = new ArrayList<>();
        try {
            tops = CreateACupcakeMapper.getAllToppings(connectionPool);
            bottoms = CreateACupcakeMapper.getAllBottoms(connectionPool);
            ctx.attribute("toppings", tops);
            ctx.attribute("bottoms", bottoms);

            ctx.render("/createacupcake.html");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }


    }

    public static void orderLineSum(Context ctx, ConnectionPool connectionPool) {
        Cart cart = ctx.sessionAttribute("cart");

        ctx.sessionAttribute("currentUser");
        int sum = 0;
        int totalitems = 0;

        if (cart == null) {
            cart = new Cart();

        }
        try {

            int toppingId = Integer.parseInt(ctx.formParam("top"));
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));

            Top top = CreateACupcakeMapper.getToppingById(toppingId, connectionPool);//laver et objet af top
            Bottom bottom = CreateACupcakeMapper.getBaseById(bottomId, connectionPool);//laver et objet af base


            cart.add(top, bottom, quantity);
            sum = cart.getTotal();


            ctx.sessionAttribute("cart", cart.getCartLines());
            ctx.sessionAttribute("sum", sum);
            totalitems = cart.totalItemsInCart();
            ctx.sessionAttribute("totalitems", totalitems);


            ctx.render("/cart.html");

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    public static void insertInhistory(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            User user = ctx.sessionAttribute("currentUser");
            List<CartLine> alllines = Cart.getCartLines();
            System.out.println(alllines.size());
            int totalsum = Cart.getTotal();
            boolean result = OrderlineMapper.deductFromBalance(user, totalsum, connectionPool);
            if (result) {
                int orderId = OrderlineMapper.makeAnOrder(totalsum, user, connectionPool);
                int lastIndex = alllines.size() - 1;
                int quantity = alllines.get(lastIndex).getQuantity() + alllines.get(0).getQuantity();
                int pricePrUnit = alllines.get(0).getTop().getPrice() + alllines.get(lastIndex).getBottom().getPrice();
                int getTopId = alllines.get(0).getTop().getId();
                int getBottomId = alllines.get(lastIndex).getBottom().getId();
                for (int i = 0; i < alllines.size(); i++) {
                    OrderlineMapper.inSertOrderHistory(pricePrUnit, orderId, quantity, getTopId, getBottomId, connectionPool);
                }
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException("Database error.");
        }
    }
}