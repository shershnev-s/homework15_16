package by.tut.shershnev_s.service.impl;

import by.tut.shershnev_s.repository.ConnectionRepository;
import by.tut.shershnev_s.repository.ItemRepository;
import by.tut.shershnev_s.repository.ItemsInShopsRepository;
import by.tut.shershnev_s.repository.ShopRepository;
import by.tut.shershnev_s.service.ItemsInShopsService;
import by.tut.shershnev_s.service.model.ItemDTO;
import by.tut.shershnev_s.service.model.ShopDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class ItemsInShopsServiceImpl implements ItemsInShopsService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;
    private final ItemsInShopsRepository itemsInShopsRepository;

    public ItemsInShopsServiceImpl(ConnectionRepository connectionRepository, ItemRepository itemRepository, ShopRepository shopRepository, ItemsInShopsRepository itemsInShopsRepository) {
        this.connectionRepository = connectionRepository;
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
        this.itemsInShopsRepository = itemsInShopsRepository;
    }

    @Override
    public void linkItemAndShop(ItemDTO itemDTO, ShopDTO shopDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Long itemID = itemDTO.getId();
                itemID = itemRepository.findByID(connection, itemID);
                Long shopID = shopDTO.getId();
                shopID = shopRepository.findByID(connection, shopID);
                itemsInShopsRepository.addToItemsInShopsTable(connection, itemID, shopID);
                connection.commit();
                logger.info(itemDTO.getName() + " and " + shopDTO.getName() + " linked");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Can't link Item and Shop");
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Can't create connection");
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Long> findItemsInShops() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Long> result = itemsInShopsRepository.findItemsInShops(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                logger.error("Finding items in shops failed");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            logger.error("Can't create connection");
        }
        return Collections.emptyList();
    }
}
