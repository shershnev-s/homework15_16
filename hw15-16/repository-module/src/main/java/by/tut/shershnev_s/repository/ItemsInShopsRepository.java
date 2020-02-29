package by.tut.shershnev_s.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface ItemsInShopsRepository {

    void addToItemsInShopsTable(Connection connection, Long itemID, Long shopID) throws SQLException;

    List<Long> findItemsInShops(Connection connection) throws SQLException;

}
