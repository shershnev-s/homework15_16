package by.tut.shershnev_s.service.impl;

import by.tut.shershnev_s.repository.ConnectionRepository;
import by.tut.shershnev_s.repository.ItemRepository;
import by.tut.shershnev_s.repository.model.Item;
import by.tut.shershnev_s.service.ItemService;
import by.tut.shershnev_s.service.ItemsInShopsService;
import by.tut.shershnev_s.service.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ItemRepository itemRepository;
    private final ItemsInShopsService itemsInShopsService;


    public ItemServiceImpl(ConnectionRepository connectionRepository, ItemRepository itemRepository, ItemsInShopsService itemsInShopsService) {
        this.connectionRepository = connectionRepository;
        this.itemRepository = itemRepository;
        this.itemsInShopsService = itemsInShopsService;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Item item = getObjectFromDTO(itemDTO);
                item = itemRepository.add(connection, item);
                connection.commit();
                logger.info("Item " + itemDTO.getName() + " added");
                return convertToDTO(item);
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Can't add Item");
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Can't create connection");
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void deleteItemInShop(Long itemsInShopID) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                itemRepository.deleteByID(connection, itemsInShopID);
                connection.commit();
                logger.info("Items in shops deleted");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Can't delete Item");
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Can't create connection");
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Long> findItemsInShops() {
        List<Long> itemsInShopID = itemsInShopsService.findItemsInShops();
        return itemsInShopID;
    }

    private ItemDTO convertToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setDescription(item.getDescription());
        return itemDTO;
    }

    private Item getObjectFromDTO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        return item;
    }
}
