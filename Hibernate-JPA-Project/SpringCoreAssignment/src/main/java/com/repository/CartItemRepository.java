package com.repository;

import com.model.CartItem;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CartItemRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private User user;

    public void addItem(CartItem cartItem) {
        String sql = "INSERT INTO cart_items (name, price, quantity, user_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,cartItem.getName(),cartItem.getPrice(),cartItem.getQuantity(),cartItem.getUser().getId());
    }

    public List<CartItem> fetchItemsByUserName(String userName) {
        String sql = "SELECT c.id, c.name, c.price, c.quantity, c.user_id FROM cart_items c JOIN users u ON c.user_id = u.id WHERE u.name = ?";
         return jdbcTemplate.query(sql,getAllRow(),userName);
    }

    public int deleteItemById(int itemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        return  jdbcTemplate.update(sql,itemId);
    }

    public List<CartItem> getAllItems() {
        String sql = "SELECT * FROM cart_items";
        return  jdbcTemplate.query(sql,getAllRow());

    }

    private RowMapper<CartItem> getAllRow(){
        return new RowMapper<CartItem>() {
            @Override
            public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {

                user.setId(rs.getInt("user_id"));
                return new CartItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("quantity"),
                        user
                );
            }
        };
    }
}
