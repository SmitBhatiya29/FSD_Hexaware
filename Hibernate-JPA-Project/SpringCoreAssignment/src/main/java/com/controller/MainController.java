package com.controller;

import com.config.ProjConfig;
import com.enums.UserMembership;
import com.exception.CartIdNotFoundException;
import com.model.CartItem;
import com.model.User;
import com.service.CartItemService;
import com.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainController {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjConfig.class);

        UserService userService = context.getBean(UserService.class);
        CartItemService cartItemService = context.getBean(CartItemService.class);

        User user = context.getBean(User.class);
        CartItem cartItem = context.getBean(CartItem.class);

        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("1. Add New User");
            System.out.println("2. Add new Item");
            System.out.println("3. Fetch All Item");
            System.out.println("4. Fetch Item by  User Name");
            System.out.println("5. Delete an Item.");
            System.out.println("0. Exit");
            int choice = sc.nextInt();
            if(choice == 0){
                break;
            }
            switch (choice) {
                case 1:
                    System.out.println("Enter user name");
                    String name = sc.next().toLowerCase();
                    user.setName(name);
                    System.out.println("Which memebership you belongs to?");
                    System.out.println("1. Normal");
                    System.out.println("2. Premium");
                    int membershipChoice = sc.nextInt();
                    if(membershipChoice == 1){
                        user.setMembership(UserMembership.Normal);
                    }else if(membershipChoice == 2){
                        user.setMembership(UserMembership.Primum);
                    }else{
                        user.setMembership(UserMembership.Normal);
                        System.out.println("Invalid choice : default membership selected");
                    }
                    try {
                        userService.addUser(user);
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
                    }
                    System.out.println("User added successfully");

                    break;
                case 2:
                    System.out.println("Enter item name");
                    String itemName = sc.next();
                    System.out.println("Enter item price");
                    BigDecimal itemPrice = sc.nextBigDecimal();
                    System.out.println("Enter item quantity");
                    int itemQuantity = sc.nextInt();
                    System.out.println("Enter your user Id" );
                    int user_id = sc.nextInt();

                    cartItem.setName(itemName);
                    cartItem.setPrice(itemPrice);
                    cartItem.setQuantity(itemQuantity);
                    user.setId(user_id);
                    cartItem.setUser(user);

                    cartItemService.addItem(cartItem);

                    break;
                case 3:
                    List<CartItem> list  = cartItemService.getAllItems();
                    list.forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("Enter User name");
                    String userName = sc.next();
                    List<CartItem> allItems = cartItemService.fetchItemsByUserName(userName.toLowerCase());
                    allItems.forEach(System.out::println);
                    break;
                case 5:
                    System.out.println("Enter item id");
                    int itemId = sc.nextInt();
                    try {
                        cartItemService.deleteItemById(itemId);
                    }catch (CartIdNotFoundException e){
                        System.out.println(e.getMessage());
                    }

                    break;
                default:
                    System.out.println("Invalid choice");
            }

        }

    }
}
