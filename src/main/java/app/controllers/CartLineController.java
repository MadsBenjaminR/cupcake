package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CreateACupcakeMapper;
import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class CartLineController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> createACupcake(ctx, connectionPool));
        app.post("/cart",ctx-> CartLineController.orderLineSum(ctx,connectionPool));

    }


    public static void createACupcake(Context ctx, ConnectionPool connectionPool)  {

        List<Top> tops = new ArrayList<>();
        List<Bottom> bottoms=new ArrayList<>();
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
        User user = ctx.sessionAttribute("currentUser");
        int sum=0;
        int totalitems=0;

        Cart cart = ctx.sessionAttribute("cart");
        if(cart==null){
            cart =new Cart();

        }
        try {

    int toppingId = Integer.parseInt(ctx.formParam("top"));
    int bottomId = Integer.parseInt(ctx.formParam("bottom"));
    int quantity = Integer.parseInt(ctx.formParam("quantity"));

    Top top = CreateACupcakeMapper.getToppingById(toppingId, connectionPool);//laver et objet af top
    Bottom bottom = CreateACupcakeMapper.getBaseById(bottomId, connectionPool);//laver et objet af base
 List<CartLine> units=cart.getCartLines();


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

    public static void insertInhistory(Context ctx, ConnectionPool connectionPool) {

            User user = ctx.sessionAttribute("currentUser");
            int totalsum = 0;
            Cart cart = ctx.sessionAttribute("cart");
            if (cart == null) {
                cart = new Cart();

            }
            try {


                totalsum = cart.getTotal();
                List<CartLine> alllines = cart.getCartLines();

                CartLine firstCartLine = alllines.get(0);

                int pricePrUnit = firstCartLine.getTop().getPrice()+firstCartLine.getBottom().getPrice();

                for (int i = 0; i <= alllines.size(); i++) {
                    OrderlineMapper.inSertOrderHistory(pricePrUnit, user,firstCartLine.getQuantity(),firstCartLine.getTop().getId(),firstCartLine.getBottom().getId(), connectionPool);


                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
    }

    
