package app.persistence;

import app.entities.Orderline;

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

        String sql = "SELECT * from orderline";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        )
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("price");
                int orderlineId = rs.getInt("orderline_id");

                Orderline orderline = new Orderline(orderlineId,price,quantity);
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
