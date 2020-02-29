package by.tut.shershnev_s.hw.runner;

import by.tut.shershnev_s.service.ItemService;
import by.tut.shershnev_s.service.ItemsInShopsService;
import by.tut.shershnev_s.service.ShopService;
import by.tut.shershnev_s.service.model.ItemDTO;
import by.tut.shershnev_s.service.model.ShopDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HWAppRuner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;
    private final ShopService shopService;
    private final ItemsInShopsService itemsInShopsService;

    public HWAppRuner(ItemService itemService, ShopService shopService, ItemsInShopsService itemsInShopsService) {
        this.itemService = itemService;
        this.shopService = shopService;
        this.itemsInShopsService = itemsInShopsService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int numberOfItems = 2;
        int numberOfShops = 2;
        List<ItemDTO> itemDTOS = generateItem(numberOfItems);
        addItems(itemDTOS);
        List<ShopDTO> shopDTOS = generateShop(numberOfShops);
        addShops(shopDTOS);
        int firstElementInList = 0;
        linkItemAndShop(itemDTOS, shopDTOS, firstElementInList);
        List<Long> itemsInShopID = itemService.findItemsInShops();
        for (Long element : itemsInShopID) {
            itemService.deleteItemInShop(element);
        }
    }

    private void addItems(List<ItemDTO> itemDTOS) {
        for (int i = 0; i < itemDTOS.size(); i++) {
            ItemDTO itemDTO = itemDTOS.get(i);
            itemDTO = itemService.add(itemDTO);
            itemDTOS.set(i, itemDTO);
            logger.info("Added items: " + itemDTO.getName());
        }
    }

    private void addShops(List<ShopDTO> shopDTOS) {
        for (int i = 0; i < shopDTOS.size(); i++) {
            ShopDTO shopDTO = shopDTOS.get(i);
            shopDTO = shopService.add(shopDTO);
            shopDTOS.set(i, shopDTO);
            logger.info("Added shops: " + shopDTO.getName());
        }
    }

    private void linkItemAndShop(List<ItemDTO> itemDTOS, List<ShopDTO> shopDTOS, int firstElementInList) {
        ItemDTO itemDTO = itemDTOS.get(firstElementInList);
        ShopDTO shopDTO = shopDTOS.get(firstElementInList);
        itemsInShopsService.linkItemAndShop(itemDTO, shopDTO);
    }

    private List<ShopDTO> generateShop(int numberOfShops) {
        List<ShopDTO> shopDTOS = new ArrayList<>();
        for (int i = 0; i < numberOfShops; i++) {
            ShopDTO shopDTO = new ShopDTO();
            shopDTO.setName("Shop" + i);
            shopDTO.setLocation("Location" + i);
            shopDTOS.add(shopDTO);
        }
        return shopDTOS;
    }

    private List<ItemDTO> generateItem(int numberOfItems) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setName("Item" + i);
            itemDTO.setDescription("Description" + i);
            itemDTOS.add(itemDTO);
        }
        return itemDTOS;
    }
}


