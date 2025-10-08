package demo.ecommerce.controller.mapper;

import demo.ecommerce.controller.dto.CancelOrderRequest;
import demo.ecommerce.controller.dto.CreateOrderRequest;
import demo.ecommerce.controller.dto.OrderResponse;
import demo.ecommerce.controller.dto.PayOrderRequest;
import demo.ecommerce.usecase.order.*;

import java.util.UUID;

public class OrderMapper {

    public static CreateOrderInput toInput(CreateOrderRequest request) {
        var items = request.items().stream()
                .map(i -> new OrderItemInput(i.productId(), i.quantity()))
                .toList();

        return new CreateOrderInput(request.userId(), items);
    }

    public static PayOrderInput toInput(String orderId, PayOrderRequest request) {
        return new PayOrderInput(UUID.fromString(orderId), request.paymentMethod());
    }

    public static CancelOrderInput toInput(String orderId, CancelOrderRequest request) {
        return new CancelOrderInput(UUID.fromString(orderId), request.reason());
    }

    public static OrderResponse toResponse(CreateOrderOutput output) {
        return new OrderResponse(output.orderId(), output.totalAmount(), output.status());
    }

    public static OrderResponse toResponse(PayOrderOutput output) {
        return new OrderResponse(output.orderId(), output.totalAmount(), output.status());
    }

    public static OrderResponse toResponse(CancelOrderOutput output) {
        return new OrderResponse(output.orderId(), output.totalAmount(), output.status());
    }
}
