import app.config.ThymeleafConfig;

public static void main(String[] args)
{
    // Initializing Javalin and Jetty webserver

    Javalin app = Javalin.create(config -> {
        config.staticFiles.add("/public");
        config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
    }).start(7070);

    // Routing

    app.get("/", ctx ->  ctx.render("index.html"));
}