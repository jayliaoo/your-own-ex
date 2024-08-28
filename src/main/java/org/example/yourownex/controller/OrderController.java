package org.example.yourownex.controller;

import org.example.yourownex.dto.*;
import org.example.yourownex.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Result<Void> create(@RequestBody OrderCreationRequest request) {
        orderService.create(request);
        return Result.success();
    }
}
