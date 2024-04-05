package app.persistence;


import app.entities.Orderline;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
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


    public static void inSertOrderHistory(int pricePrUnit, int orderId, int quantity, int topId, int bottomId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO public.orderline (quantity, price, order_id, bottom_id, top_id) VALUES ( ?, ?, ?, ?, ?)";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, quantity);
            ps.setInt(2, pricePrUnit);
            ps.setInt(3,orderId);
            ps.setInt(4,topId);
            ps.setInt(5,bottomId);

            ps.executeUpdate();


        }catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static int makeAnOrder(int totalsum, User user,ConnectionPool connectionPool) throws SQLException, DatabaseException {

        int orderId = 0;

        String sql="INSERT INTO public.order(price, user_id) VALUES (?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        )

        {
            ps.setInt(1,totalsum);
            ps.setInt(2,user.getUserId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected>0){
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                orderId = rs.getInt("order_id");
                return orderId;
            }


        }catch (SQLException e){
            throw new DatabaseException("fejl i data");
        }

        return orderId;
    }

}


