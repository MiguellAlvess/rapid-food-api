package br.com.db.rapid_food_api.order.controller;

import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderResponseDto;
import br.com.db.rapid_food_api.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                                        UriComponentsBuilder builder) {
        OrderResponseDto responseDto = orderService.createOrder(orderRequestDto);
        URI uri = builder.path("/orders/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }
}
