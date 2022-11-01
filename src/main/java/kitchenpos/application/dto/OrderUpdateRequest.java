package kitchenpos.application.dto;

public class OrderUpdateRequest {
    private String orderStatus;

    private OrderUpdateRequest() {
    }

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}