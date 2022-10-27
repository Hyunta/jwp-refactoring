package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("OrderService의")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private MenuDao menuDao;
    @MockBean
    private OrderTableDao orderTableDao;
    @MockBean
    private OrderDao orderDao;
    @MockBean
    private OrderLineItemDao orderLineItemDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {
        private static final long ORDER_ID = 1L;
        private static final long MENU_A_ID = 1L;
        private static final long MENU_B_ID = 2L;
        private static final long ORDER_TABLE_ID = 1L;

        private OrderLineItem orderLineItemA;
        private OrderLineItem orderLineItemB;
        private OrderTable orderTable;
        private OrderCreateRequest request;

        @BeforeEach
        void setUp() {
            orderLineItemA = new OrderLineItem(null, ORDER_ID, MENU_A_ID, 1);
            orderLineItemB = new OrderLineItem(null, ORDER_ID, MENU_B_ID, 1);

            orderTable = new OrderTable(ORDER_TABLE_ID, null, 2, false);

            request = new OrderCreateRequest(ORDER_TABLE_ID, Arrays.asList(orderLineItemA, orderLineItemB));

            Order savedOrder = new Order(1L, ORDER_TABLE_ID, OrderStatus.COOKING.name(),
                    LocalDateTime.now(), Arrays.asList(orderLineItemA, orderLineItemB));

            given(orderDao.save(any(Order.class)))
                    .willReturn(savedOrder);
            given(orderLineItemDao.save(any(OrderLineItem.class)))
                    .willReturn(orderLineItemA)
                    .willReturn(orderLineItemB);
            given(menuDao.countByIdIn(any()))
                    .willReturn((long) request.getOrderLineItems().size());
            given(orderTableDao.findById(request.getOrderTableId()))
                    .willReturn(Optional.of(orderTable));
        }

        @Test
        @DisplayName("등록할 수 있는 주문을 받으면, 저장하고 내용을 반환한다.")
        void success() {
            //when
            Order actual = orderService.create(request);

            //then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(2),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("주문 아이템이 비어있으면, 예외를 던진다.")
        void fail_noOrderLineItem(List<OrderLineItem> orderLineItems) {
            //given
            request = new OrderCreateRequest(ORDER_TABLE_ID, null);

            //when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("동일한 주문 아이템을 입력하면, 예외를 던진다.")
        void fail_sameOrderLineItem() {
            //given
            request = new OrderCreateRequest(ORDER_TABLE_ID, Arrays.asList(orderLineItemA, orderLineItemA));
            Set<Long> distinctMenus = request.getOrderLineItems().stream()
                    .map(OrderLineItem::getMenuId)
                    .collect(Collectors.toSet());

            given(menuDao.countByIdIn(any()))
                    .willReturn((long) distinctMenus.size());

            //when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문한 테이블의 상태가 Empty이면, 예외를 던진다.")
        void fail_orderTableIsEmpty() {
            //given
            orderTable.setEmpty(true);

            //when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class changeOrderStatus {
        private static final long ORDER_ID = 1L;
        private static final long MENU_A_ID = 1L;
        private static final long MENU_B_ID = 2L;
        private static final long ORDER_TABLE_ID = 1L;

        private OrderLineItem orderLineItemA;
        private OrderLineItem orderLineItemB;
        private Order savedOrder;
        private OrderUpdateRequest request;

        @BeforeEach
        void setUp() {
            orderLineItemA = new OrderLineItem(null, ORDER_ID, MENU_A_ID, 1);
            orderLineItemB = new OrderLineItem(null, ORDER_ID, MENU_B_ID, 1);

            savedOrder = new Order(ORDER_ID, ORDER_TABLE_ID, null, null,
                    Arrays.asList(orderLineItemA, orderLineItemB));
            request = new OrderUpdateRequest(OrderStatus.MEAL.name());

            given(orderDao.findById(ORDER_ID))
                    .willReturn(Optional.of(savedOrder));
        }

        @Test
        @DisplayName("주문의 상태를 변경할 수 있다.")
        void success() {
            //when
            Order actual = orderService.changeOrderStatus(ORDER_ID, request);

            //then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        @DisplayName("주문이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistOrder() {
            //given
            given(orderDao.findById(ORDER_ID))
                    .willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문이 완료상태이면, 예외를 던진다.")
        void fail_completedOrder() {
            //given
            savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
