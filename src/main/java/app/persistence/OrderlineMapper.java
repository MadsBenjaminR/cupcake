package app.persistence;

import app.entities.Bottom;
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
    public static void deductFromCart(int price, User user, int sum,ConnectionPool connectionPool) {

        String sql = "SELECT * from users where user_id=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int currentPrice = rs.getInt("price");
                int newTotalPrice = currentPrice - price;
                if (newTotalPrice < sum) {
                    System.out.println("Insufficient funds!");

                }
                  String sql02 = "update users set balance=? where user_id=?";
                PreparedStatement ps02 = connection.prepareStatement(sql02);
                    ps02.setInt(1, newTotalPrice);
                    ps02.setInt(2, user.getUserId());
                    ps02.executeQuery();
                    System.out.println("Balance has been updated");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public static void inSertOrderHistory(int pricePrUnit, User user, int sum, ConnectionPool connectionPool) {



    }
}
