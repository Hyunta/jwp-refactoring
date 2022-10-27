package kitchenpos.domain;

import static kitchenpos.support.Fixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Order의")
class OrderTest {

    @Nested
    @DisplayName("checkUpdatable 메서드는")
    class CheckUpdatable {

        @Test
        @DisplayName("상태를 변경할 수 있다.")
        void success() {
            Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                    Arrays.asList(createOrderLineItem(), createOrderLineItem()));
            assertDoesNotThrow(order::checkUpdatable);
        }

        @Test
        @DisplayName("주문이 완료 상태이면, 예외를 던진다.")
        void fail_completed() {
            Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                    Arrays.asList(createOrderLineItem(), createOrderLineItem()));
            assertThatThrownBy(order::checkUpdatable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}