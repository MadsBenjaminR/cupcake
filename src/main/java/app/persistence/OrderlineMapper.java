package app.persistence;

import app.controllers.UserController;
import app.entities.Orderline;
import app.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderlineMapper {

    public static List<Orderline> getAllOrderlines(ConnectionPool connectionPool)
    {

        List<Orderline> orderlines = new ArrayList<>();

        String sql = "select orderline.orderline_id, orderline.quantity, orderline.price as orderline_price, orderline.order_id, orderline.bottom_id, orderline.top_id, users.email from orderline join orders on orderline.order_id = orders.order_id join users on orders.user_id = users.user_id";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        )
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("orderline_price");
                int orderlineId = rs.getInt("orderline_id");
                String email = rs.getString("email");
                Orderline orderline = new Orderline(orderlineId,price,quantity, email);
                orderlines.add(orderline);
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return orderlines;
    }
}
