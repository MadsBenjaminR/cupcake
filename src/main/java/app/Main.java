package app;

import app.config.ThymeleafConfig;
import app.controllers.CreateACupcake;
import app.controllers.CreditController;
import app.controllers.OrderlineController;
import app.controllers.UserController;
import app.entities.Bottom;
import app.entities.Top;
import app.persistence.ConnectionPool;
import app.persistence.OrderlineMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import kotlin.Pair;

public class Main
{
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing

        app.get("/", ctx -> ctx.render("index.html"));
        UserController.addRoutes(app,connectionPool);
        OrderlineController.addRoutes(app,connectionPool);
        CreditController.addRoutes(app,connectionPool);
       app.post("/createcupcake",ctx ->CreateACupcake.createACupcake(ctx,connectionPool));
       app.get("/calculate",ctx-> CreateACupcake.orderLineSum(ctx,connectionPool));





    }
}