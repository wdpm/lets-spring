package io.github.wdpm.service;

import io.github.wdpm.domain.Ingredient;
import io.github.wdpm.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;

/**
 * @author evan
 * @date 2020/5/20
 */
@Service
public class OrderServiceImpl implements OrderService {

    private JdbcTemplate jdbcTemplate;

    private static final String CREATE_ORDER_LINE_ITEM =
            "insert into purchase_line_item(purchase_id,ingredient_id,units) values (?,?,?)";

    private static final String CREATE_ORDER = "insert into purchase(total_price) values(?)";

    @Autowired
    public OrderServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveOrder(Order order) {
        // create order and return orderId
        final int orderId = saveOrderInternal(order);

        // use orderId to create order items
        saveLineItem(orderId, order.getFlavor(), order.getScoops());
        order.getToppings().forEach(topping -> saveLineItem(orderId, topping, 1));
    }

    private void saveLineItem(int orderId, Ingredient ingredient, int units) {
        jdbcTemplate.update(CREATE_ORDER_LINE_ITEM, orderId, ingredient.getId(), units);
    }

    private int saveOrderInternal(Order order) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_ORDER, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, order.getTotalPrice());
            return ps;
        };
        jdbcTemplate.update(psc, keyHolder);
        return (int) keyHolder.getKeyList().get(0).get("ID");
    }
}
