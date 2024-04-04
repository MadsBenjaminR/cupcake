package app.persistence;


import app.entities.Bottom;
import app.entities.Top;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateACupcakeMapper {

    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
            List<Bottom> bottom = new ArrayList<>();

            String sql = "SELECT * FROM public.bottom ORDER BY bottom_id ASC ";

            try (Connection connection = connectionPool.getConnection())
            {
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    ResultSet rs = ps.executeQuery();

                    while (rs.next())
                    {
                        int id = rs.getInt("bottom_id");
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        bottom.add(new Bottom(id, name, price));
                    }
                }

            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new DatabaseException("Fejl!!!!");
            }
        return bottom;
        }

    public static List<Top> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        List<Top> tops = new ArrayList<>();

        String sql = "SELECT * FROM public.top ORDER BY top_id ASC ";

        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();

                while (rs.next())
                {
                    int id = rs.getInt("top_id");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    tops.add(new Top(id, name, price));
                }
            }

        }
        catch (SQLException e) {
            throw new DatabaseException("Fejl!!!!");
        }
        return tops;
    }

    public static Top getToppingById(int toppingId, ConnectionPool connectionPool) {
        Top top = null;

        String sql="SELECT * FROM public.top where top_id=?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {

                ps.setInt(1,toppingId);

                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int id = rs.getInt("top_id");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    top =new Top(id, name, price);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return top;
    }
    public static Bottom getBaseById(int baseId, ConnectionPool connectionPool) {
        Bottom bottom =null;


        String sql="SELECT * FROM public.bottom where bottom_id=?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {

                ps.setInt(1,baseId);

                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    int id = rs.getInt("bottom_id");
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    bottom =new Bottom(id, name, price);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bottom;
    }
}

