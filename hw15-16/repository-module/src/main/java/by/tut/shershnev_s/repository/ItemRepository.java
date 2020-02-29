package by.tut.shershnev_s.repository;

import by.tut.shershnev_s.repository.model.Item;

import java.sql.Connection;
import java.sql.SQLException;

public interface ItemRepository {

    Item add(Connection connection, Item item) throws SQLException;

    Long findByID(Connection connection, Long id) throws SQLException;

    void deleteByID(Connection connection, Long id) throws SQLException;
}
