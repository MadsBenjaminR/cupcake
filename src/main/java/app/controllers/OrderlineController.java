package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import app.entities.Orderline;

import java.util.List;
import java.util.Map;

public class OrderlineController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.get("/admin.html", ctx -> displayAllOrderLines(ctx,connectionPool));
    }

    public static void displayAllOrderLines(Context ctx, ConnectionPool connectionPool) {

                List<Orderline> orderlines = OrderlineMapper.getAllOrderlines(connectionPool);
                ctx.sessionAttribute("orderlines", orderlines);
                ctx.render("admin.html");

    }

}
