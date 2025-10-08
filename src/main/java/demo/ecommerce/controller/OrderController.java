package demo.ecommerce.controller;

import demo.ecommerce.controller.dto.CancelOrderRequest;
import demo.ecommerce.controller.dto.CreateOrderRequest;
import demo.ecommerce.controller.dto.OrderResponse;
import demo.ecommerce.controller.dto.PayOrderRequest;
import demo.ecommerce.controller.mapper.OrderMapper;
import demo.ecommerce.usecase.order.CancelOrderUseCase;
import demo.ecommerce.usecase.order.CreateOrderUseCase;
import demo.ecommerce.usecase.order.PayOrderUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           PayOrderUseCase payOrderUseCase,
                           CancelOrderUseCase cancelOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.payOrderUseCase = payOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        var input = OrderMapper.toInput(request);
        var output = createOrderUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderMapper.toResponse(output));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> pay(@PathVariable("id") String id,
                                             @Valid @RequestBody PayOrderRequest request) {
        var input = OrderMapper.toInput(id, request);
        var output = payOrderUseCase.execute(input);
        return ResponseEntity.ok(OrderMapper.toResponse(output));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable("id") String id,
                                                @RequestBody CancelOrderRequest request) {
        var input = OrderMapper.toInput(id, request);
        var output = cancelOrderUseCase.execute(input);
        return ResponseEntity.ok(OrderMapper.toResponse(output));
    }
}
