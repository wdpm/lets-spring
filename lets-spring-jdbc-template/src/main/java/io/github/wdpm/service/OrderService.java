package io.github.wdpm.service;

import io.github.wdpm.domain.Order;

/**
 * @author evan
 * @date 2020/5/20
 */
public interface OrderService {
    void saveOrder(Order order);
}

