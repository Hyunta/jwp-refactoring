package kitchenpos.ui.dto;

public class OrderTableUpdateRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableUpdateRequest() {
    }

    public OrderTableUpdateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}