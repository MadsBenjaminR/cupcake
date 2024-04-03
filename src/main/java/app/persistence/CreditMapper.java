package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class CreditMapper {

    public static void addCredit(String email, int balance, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "UPDATE users set balance = balance + ? WHERE email = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, balance);
            ps.setString(2, email);


            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }

        }catch (SQLException e){
            String msg = "Der er sket en fejl ved indsættelse af kredit. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }

    }
}
