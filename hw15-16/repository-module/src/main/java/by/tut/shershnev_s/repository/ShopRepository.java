package by.tut.shershnev_s.repository;

import by.tut.shershnev_s.repository.model.Shop;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;

@Repository
public interface ShopRepository {

    Shop add(Connection connection, Shop shop) throws SQLException;

    Long findByID(Connection connection, Long id) throws SQLException;

}
