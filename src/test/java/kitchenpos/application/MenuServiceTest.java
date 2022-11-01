package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("MenuService의")
public class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuGroupDao menuGroupDao;
    @MockBean
    private ProductDao productDao;
    @MockBean
    private MenuProductDao menuProductDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {
        private static final long PRODUCT_A_ID = 1L;
        private static final long PRODUCT_B_ID = 2L;
        private static final int PRODUCT_PRICE = 10000;
        private static final long MENU_GROUP_ID = 1L;
        private static final long MENU_PRICE = 10000;

        private Product productA;
        private Product productB;
        private MenuProductCreateRequest menuProductA;
        private MenuProductCreateRequest menuProductB;
        private MenuGroup menuGroup;
        private MenuCreateRequest createRequest;

        @BeforeEach
        void setUp() {
            productA = new Product(PRODUCT_A_ID, "상품A", BigDecimal.valueOf(PRODUCT_PRICE));
            productB = new Product(PRODUCT_B_ID, "상품B", BigDecimal.valueOf(PRODUCT_PRICE));
            productDao.saveAll(Arrays.asList(productA, productB));

            menuProductA = new MenuProductCreateRequest(PRODUCT_A_ID, 2);
            menuProductB = new MenuProductCreateRequest(PRODUCT_B_ID, 2);

            menuGroup = new MenuGroup(MENU_GROUP_ID, "메뉴 그룹 이름");
            menuGroupDao.save(menuGroup);

            createRequest = new MenuCreateRequest("메뉴 이름", BigDecimal.valueOf(MENU_PRICE), MENU_GROUP_ID,
                    Arrays.asList(menuProductA, menuProductB));

            given(menuGroupDao.existsById(any()))
                    .willReturn(true);
            given(productDao.findAllById(any()))
                    .willReturn(Arrays.asList(productA, productB));
        }

        @Test
        @DisplayName("등록할 수 있는 메뉴를 받으면, 메뉴를 저장하고 내용을 반환한다.")
        void success() {
            //when
            Menu actual = menuService.create(createRequest);

            //then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getMenuProducts().getValues()).hasSize(2)
            );
        }

        @Test
        @DisplayName("메뉴 가격이 없으면, 예외를 던진다.")
        void fail_noPrice() {
            //given
            createRequest = new MenuCreateRequest("메뉴 이름", null, MENU_GROUP_ID,
                    Arrays.asList(menuProductA, menuProductB));

            //when & then
            assertThatThrownBy(() -> menuService.create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 가격이 음수면, 예외를 던진다.")
        void fail_priceIsNegative() {
            //given
            createRequest = new MenuCreateRequest("메뉴 이름", BigDecimal.valueOf(-1), MENU_GROUP_ID,
                    Arrays.asList(menuProductA, menuProductB));

            //when & then
            assertThatThrownBy(() -> menuService.create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴가 포함될 메뉴그룹이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistMenuGroup() {
            //given
            given(menuGroupDao.existsById(createRequest.getMenuGroupId()))
                    .willReturn(false);

            //when & then
            assertThatThrownBy(() -> menuService.create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴의 가격보다 포함된 상품들의 총합이 싸면, 예외를 던진다.")
        void fail_sumIsCheaperThenPrice() {
            //given
            productA = new Product(PRODUCT_A_ID, "상품A", BigDecimal.valueOf(100));
            menuProductA = new MenuProductCreateRequest(PRODUCT_A_ID, 1);
            productB = new Product(PRODUCT_B_ID, "상품B", BigDecimal.valueOf(100));
            menuProductB = new MenuProductCreateRequest(PRODUCT_B_ID, 1);

            given(productDao.findAllById(any()))
                    .willReturn(List.of(productA, productB));

            createRequest = new MenuCreateRequest("메뉴 이름", BigDecimal.valueOf(201), MENU_GROUP_ID,
                    Arrays.asList(menuProductA, menuProductB));

            //when & then
            assertThatThrownBy(() -> menuService.create(createRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListMethod {
        private static final long PRODUCT_A_ID = 1L;
        private static final long PRODUCT_B_ID = 2L;
        private static final long MENU_ID = 1L;

        private MenuProduct menuProductA;
        private MenuProduct menuProductB;

//        @Test
//        @DisplayName("전체 메뉴를 조회할 때, 메뉴상품도 같이 조회할 수 있다.")
//        void success_getMenuProducts() {
//            //given
//            menuProductA = new MenuProduct(null, MENU_ID, PRODUCT_A_ID, 2);
//            menuProductB = new MenuProduct(null, MENU_ID, PRODUCT_B_ID, 2);
//
//            given(menuProductDao.findAllByMenuId(1L))
//                    .willReturn(Arrays.asList(menuProductA, menuProductB));
//
//            //when
//            List<Menu> actual = menuService.list();
//            Menu actualMenu = actual.iterator().next();
//
//            //then
//            assertThat(actualMenu.getMenuProducts()).hasSize(2);
//        }
    }
}
