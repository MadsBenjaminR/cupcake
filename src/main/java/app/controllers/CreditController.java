package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CreditMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class CreditController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.post("/addCredit", ctx -> addCredit(ctx, connectionPool));

    }


    public static void addCredit(Context ctx, ConnectionPool connectionPool){

        String email = ctx.formParam("email");
        int balance = Integer.parseInt(ctx.formParam("balance"));

        try{
            CreditMapper.addCredit(email, balance, connectionPool);
            ctx.attribute("kredit er overført til " + email);
            //ctx.render("");
        }catch (DatabaseException e){
            ctx.attribute("message", "kredit kunne ikke indsættes");
            //ctx.render("");
        }
    }
}
