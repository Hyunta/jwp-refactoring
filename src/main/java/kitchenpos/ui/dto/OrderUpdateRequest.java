package kitchenpos.ui.dto;

public class OrderUpdateRequest {
    private String orderStatus;

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}