package kitchenpos.ui.dto;

public class MenuProductCreateRequest {

    private Long productId;
    private long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}