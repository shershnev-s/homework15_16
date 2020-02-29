package by.tut.shershnev_s.service;

import by.tut.shershnev_s.service.model.ItemDTO;

import java.util.List;


public interface ItemService {

    ItemDTO add(ItemDTO itemDTO);

    void deleteItemInShop(Long itemsInShopID);

    List<Long> findItemsInShops();

}
