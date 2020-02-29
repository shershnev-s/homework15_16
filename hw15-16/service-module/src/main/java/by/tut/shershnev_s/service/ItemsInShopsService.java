package by.tut.shershnev_s.service;

import by.tut.shershnev_s.service.model.ItemDTO;
import by.tut.shershnev_s.service.model.ShopDTO;

import java.util.List;


public interface ItemsInShopsService {

    void linkItemAndShop(ItemDTO itemDTO, ShopDTO shopDTO);

    List<Long> findItemsInShops();
}
