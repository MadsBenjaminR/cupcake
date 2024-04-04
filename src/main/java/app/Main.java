package app;

import app.config.ThymeleafConfig;
import app.controllers.CreateACupcake;
import app.controllers.CreditController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

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

       app.get("/", ctx -> ctx.render("login.html"));
       app.post("/createcupcake",ctx ->CreateACupcake.createACupcake(ctx,connectionPool));
       CreditController.addRoutes(app, connectionPool);
       UserController.addRoutes(app, connectionPool);
       app.get("/calculate",ctx-> CreateACupcake.orderLineSum(ctx,connectionPool));


    }
}