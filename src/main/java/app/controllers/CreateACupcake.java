package app.controllers;

import app.entities.Bottom;
import app.entities.Cart;
import app.entities.CartLine;
import app.entities.Top;
import app.exceptions.DatabaseException;
import app.persistence.CreateACupcakeMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
public class CreateACupcake {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {

       // app.post("/createcupcake",ctx ->createACupcake(ctx,connectionPool));

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



    cart.add(top, bottom, quantity);

    sum= cart.getTotal();

    ctx.sessionAttribute("cart", cart.getCartLines());
    ctx.sessionAttribute("sum",sum);
    totalitems=cart.totalItemsInCart();
    ctx.sessionAttribute("totalitems",totalitems);




    ctx.render("/cart.html");

        } catch (NumberFormatException e) {
    throw new RuntimeException(e);
}

    }

    
}
