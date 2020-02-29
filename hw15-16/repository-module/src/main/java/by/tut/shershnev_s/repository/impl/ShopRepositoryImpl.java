package by.tut.shershnev_s.repository.impl;

import by.tut.shershnev_s.repository.ShopRepository;
import by.tut.shershnev_s.repository.model.Shop;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ShopRepositoryImpl implements ShopRepository {


    @Override
    public Shop add(Connection connection, Shop shop) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Shop(name, location) VALUES(?,?)"
                        , Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, shop.getName());
            preparedStatement.setString(2, shop.getLocation());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating shop failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shop.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating shop failed, no ID obtained.");
                }
            }
            return shop;
        }
    }

    @Override
    public Long findByID(Connection connection, Long id) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id FROM Shop WHERE id=?;"
                )
        ) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            Long result = null;
            while (rs.next()) {
                result = rs.getLong("id");
            }
            return result;
        }
    }
}
