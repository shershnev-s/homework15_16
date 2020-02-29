package by.tut.shershnev_s.repository.impl;

import by.tut.shershnev_s.repository.ItemRepository;
import by.tut.shershnev_s.repository.model.Item;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public Item add(Connection connection, Item item) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Item(name, description) VALUES(?,?)"
                        , Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getDescription());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
            return item;
        }
    }

    @Override
    public Long findByID(Connection connection, Long id) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT id FROM Item WHERE id=?;"
                )
        ) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            Long result = null;
            if (rs.next()) {
                result = rs.getLong("id");
            }
            return result;
        }
    }

    @Override
    public void deleteByID(Connection connection, Long id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM item WHERE id =?"
                )
        ) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting item failed, no rows affected.");
            }
        }
    }

}
