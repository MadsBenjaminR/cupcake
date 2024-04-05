package app.controllers;

import app.Main;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.UserMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){

        app.post("/index", ctx -> createUser(ctx,connectionPool));
        app.get("/index", ctx -> ctx.render("index.html"));

        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));

        app.get("logout", ctx -> logout(ctx));



    }


    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        //render() ændrer ikke på routing, ændrer ikke URL'en, det gør redirect().
        //Men så skal man sørge for at ramme roden /
        ctx.redirect("/");
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool)
    {
        //hent formparametre
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        System.out.println("WOW");

        if(password1.equals(password2))
        {
            try
            {
                UserMapper.createUser(email,password1,connectionPool);
                ctx.attribute("message", "Hermed oprettet med email: " + email + ". Log på.");
                ctx.render("login.html");
            }
            catch (DatabaseException e)
            {
                ctx.attribute("message", "Brugernavn findes allerede - prøv igen eller log ind.");
                ctx.render("login.html");
            }
        } else
        {
            ctx.attribute("message", "passwords matcher ikke - prøv igen.");
            ctx.render("login.html");
        }

    }

    public static void login(Context ctx, ConnectionPool connectionPool){

        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try{
            User user = UserMapper.login(email,password,connectionPool);
            //Hvis login-operationen lykkes, gemmes brugeren som den aktuelle bruger i sessionsattributterne i kontekstobjektet.
            ctx.sessionAttribute("currentUser", user);
            //En meddelelse om, at brugeren er logget ind, tilføjes til attributterne i kontekstobjektet.
            ctx.attribute("message", "du er nu logget ind");
            //Til sidst renderes en HTML-side, som brugeren vil blive videresendt til efter vellykket login.
          if (user.getRole().equalsIgnoreCase("admin")) {
              ctx.redirect("admin.html");
          } else {
              ctx.render("index");
          }

        }
        catch (DatabaseException e){
            //Fejlmeddelelsen fra DatabaseException tilføjes som en attribut til kontekstobjektet.
            ctx.attribute("message", e.getMessage());
            // HTML-siden, sandsynligvis startsiden, rendres, og brugeren præsenteres for en fejlmeddelelse.
            ctx.render("login.html");
        }
    }
}
