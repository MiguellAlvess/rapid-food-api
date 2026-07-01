package br.com.db.rapid_food_api.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.db.rapid_food_api.config.RepositoryIntegrationTestBase;
import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.factory.FactoryHelper;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class OrderRepositoryIT extends RepositoryIntegrationTestBase{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;


    private User savedUser;
    private Vendor savedVendor;

    @BeforeEach
    void cleanDb(){
        orderRepository.deleteAll();
        userRepository.deleteAll();
        vendorRepository.deleteAll();

        savedUser = userRepository.saveAndFlush(UserConstants.createUser());
        savedVendor = vendorRepository.saveAndFlush(VendorTestFactory.createVendor());
    }

    @DisplayName("Should return a list or Orders By User ordered desc")
    @Test
    public void shouldReturnAllOrdersByUsersOrderedDesc(){
        Order firstOrder = FactoryHelper.createOrderWithNullId();
        firstOrder.setUser(savedUser);
        firstOrder.setVendor(savedVendor);

        Order secondOrder = FactoryHelper.createOrderWithNullId();
        secondOrder.setUser(savedUser);
        secondOrder.setVendor(savedVendor);
        secondOrder.setCreatedAt(firstOrder.getCreatedAt().plusMinutes(5));

        orderRepository.saveAndFlush(firstOrder);
        orderRepository.saveAndFlush(secondOrder);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> result = orderRepository.findAllByUserOrderedDesc(savedUser.getId(), pageable);
        
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getCreatedAt()).isAfter((result.getContent().get(1).getCreatedAt()));
    }
}