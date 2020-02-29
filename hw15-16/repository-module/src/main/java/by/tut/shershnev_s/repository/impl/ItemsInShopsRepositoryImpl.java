package by.tut.shershnev_s.repository.impl;

import by.tut.shershnev_s.repository.ItemsInShopsRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemsInShopsRepositoryImpl implements ItemsInShopsRepository {

    @Override
    public void addToItemsInShopsTable(Connection connection, Long itemID, Long shopID) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO ItemsInShops(item_id, shop_id) VALUES(?,?);"
                        , Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setLong(1, itemID);
            preparedStatement.setLong(2, shopID);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Linking failed, no rows affected.");
            }
        }
    }

    @Override
    public List<Long> findItemsInShops(Connection connection) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        " SELECT item_id FROM ItemsInShops;"
                )
        ) {
            ResultSet rs = statement.executeQuery();
            List<Long> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getLong("item_id"));
            }
            return result;
        }
    }
}
