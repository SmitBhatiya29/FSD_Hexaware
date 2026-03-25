package com.service;

import com.exception.CartIdNotFoundException;
import com.model.CartItem;
import com.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    public void addItem(CartItem cartItem) {
        cartItemRepository.addItem(cartItem);
    }

    public List<CartItem> fetchItemsByUserName(String userName) {
        return  cartItemRepository.fetchItemsByUserName(userName);
    }

    public void deleteItemById(int itemId) throws CartIdNotFoundException {
         int check = cartItemRepository.deleteItemById(itemId);
         if(check == 0){
            throw new CartIdNotFoundException("Item Id is Invalid ....");
         }else {
             System.out.println("Item Deleted Successfully....");
         }

    }

    public List<CartItem> getAllItems() {
        return  cartItemRepository.getAllItems();
    }
}
