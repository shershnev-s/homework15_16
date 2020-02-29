package by.tut.shershnev_s.service.impl;

import by.tut.shershnev_s.repository.ConnectionRepository;
import by.tut.shershnev_s.repository.ShopRepository;
import by.tut.shershnev_s.repository.model.Shop;
import by.tut.shershnev_s.service.ShopService;
import by.tut.shershnev_s.service.model.ShopDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository;
    private final ShopRepository shopRepository;

    public ShopServiceImpl(ConnectionRepository connectionRepository, ShopRepository shopRepository) {
        this.connectionRepository = connectionRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public ShopDTO add(ShopDTO shopDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Shop shop = getObjectFromDTO(shopDTO);
                shop = shopRepository.add(connection, shop);
                connection.commit();
                logger.info("Shop " + shopDTO.getName() + " added");
                return convertToDTO(shop);
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Can't add shop");
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Can't create connection");
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private ShopDTO convertToDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        shopDTO.setLocation(shop.getLocation());
        return shopDTO;
    }

    private Shop getObjectFromDTO(ShopDTO shopDTO) {
        Shop shop = new Shop();
        shop.setId(shopDTO.getId());
        shop.setName(shopDTO.getName());
        shop.setLocation(shopDTO.getLocation());
        return shop;
    }
}

