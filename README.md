# 키친포스

## 요구 사항

### Product

>  Product: 메뉴를 관리하는 기준이 되는 데이터

1. 상품을 등록할 수 있다.
   - 등록할 상품의 가격은 필수로 있어야 한다.
   - 가격은 0보다 크거나 같아야한다.
2. 등록된 상품을 모두 조회할 수 있다.



### Menu

> MenuGroup: 메뉴 묶음, 분류
>
> Menu:  메뉴 그룹에 속하는 실제 주문 가능 단위
>
> MenuProduct: 메뉴에 속하는 수량이 있는 상품

1. 메뉴 그룹을 등록할 수 있다.
   - 메뉴 그룹에 이름을 붙일 수 있다.
2. 등록된 메뉴 그룹을 모두 조회할 수 있다.
3. 메뉴를 등록할 수 있다.
   - 메뉴의 가격은 필수로 입력해야한다.
   - 가격은 0보다 크거나 같아야 한다.
   - 해당 메뉴가 포함될 메뉴그룹이 존재해야한다.
   - 메뉴에 포함될 상품들이 존재해야 한다.
   - 메뉴의 가격이 포함된 상품들의 가격 총합보다 작거나 같아야 한다.
4. 등록된 메뉴를 모두 조회할 수 있다.



### Table

> TableGroup: 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능
>
> OrderTable: 매장에서 주문이 발생하는 영역
>
> EmptyTable: 주문을 등록할 수 없는 주문 테이블

1. 주문 테이블을 등록할 수 있다.
2. 등록된 주문 테이블을 모두 조회할 수 있다.
3. 주문 테이블의 비움 상태를 변경할 수 있다.
   - 입력한 주문 테이블이 존재해야 한다.
   - 주문 테이블이 속해있는 그룹이 없어야 한다.
   - 주문 테이블이 조리중이나 식사중이면 변경할 수 있다.
4. 주문 테이블의 인원을 변경할 수 있다.
   - 변경할 인원은 0보다 커야한다.
   - 주문 테이블이 존재해야 한다.
   - 주문 테이블이 비어있으면 안된다.
5. 테이블 그룹을 만들 수 있다.
   - 입력한 주문 테이블들은 2개 이상이어야 한다.
   - 입력한 주문 테이블들은 모두 존재해야 한다.
   - 입력한 주문 테이블들은 비어있는 상태여야 한다.
   - 입력한 주문 테이블들은 테이블 그룹이 없어야 한다.
   - 입력한 주문 테이블들의 그룹 아이디를 설정한다.
   - 주문 받을 테이블를 모두 비어있지 않은 상태로 변경한다.
6. 테이블 그룹을 해체할 수 있다.
   - 그룹의 주문 테이블들 중 조리중이거나 식사중이 테이블이 존재하면 해체할 수 없다.



### Order

> Order:  매장에서 발생하는 주문
>
> OrderStatus:  주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다.
>
> OrderLineItem:  주문에 속하는 수량이 있는 메뉴

1. 주문을 생성할 수 있다.
   - 주문아이템이 비어있으면 안된다.
   - 동일한 메뉴를 주문할 수 없다.
   - 주문한 주문테이블의 상태가 empty면 안된다.
   - 주문의 상태를 조리중으로 변경한다.
2. 주문을 전부 조회할 수 있다.
3. 주문의 상태를 변경할 수 있다.
   - 주문이 존재해야 한다.
   - 주문이 완료상태이면 변경할 수 없다.





## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |



## 리팩터링 과정

1. setter를 제거하고, 생성자를 추가한다.

   - [x] Product

   - [x] MenuProduct

   - [x] MenuGroup

   - [x] Menu

   - [x] OrderTable 

     -> setter를 지금 지우는게 맞나? 구조 개선이 필요해보인다.

     - OrderTable은 초기 생성시 numberOfGuests, tableGroupId는 없다.

   - [x] TableGroup

     -> setter를 없애는게 과연 옳은 판단이었을까?

     우선 생성자를 추가하고 필요한 setter는 유지한채 구현을 해보자

   - [x] OrderLineItem

   - [x] Order



Controller에서 받아온 객체를 그대로 save하거나, 해당 객체의 값을 update하는 부분들이 있어서 setter를 올바르게 삭제하지 못한 곳이 있다. 이어서 Controller에 요청받는 dto를 만들어 개선하려고한다.



2. Controller에서 요청받을 때 사용할 RequestDto를 만든다
   - [x] TableGroupCreateRequest
   
   - [x] ProductCreateRequest
   
   - [x] MenuCreateRequest
   
     - 메뉴 관련 DTO를 분리하다 JPA를 도입.
   
       원인1. ProductDao에서 하나씩 조회해서 가격을 더한 값과 price를 비교하는 로직
   
       원인2. menuProductDao에서 하나씩 매핑해주는 로직을 처리하기 귀찮음



3. JPA 엔티티로의 전환

   - [x] Product

   - [ ] Menu

   - [ ] MenuProduct

     - MenuProduct를 직접 참조하면서 문제가 많이 발생했다.

       1. 양방향이 걸려있어서 List.of() 를 쓰면 NPE가 터진다.
       2. Menu findAll() 테스트 시, MenuProduct에 oneToMany로 LazyLoading이 걸려 LazyIninitialization 에러가 발생한다.

       결론 : 양방향 매핑을 삭제한다. 

     JPA에 시간을 너무 많이써서 롤백했다.



4. Controller에서 요청받을 때 사용할 RequestDto를 만든다

- [x] OrderUpdateRequest
- [x] OrderCreateRequest
- [x] MenuGroupCreateRequest
  - 지금 당장은 필요없지만, 컨트롤러와 도메인의 경계를 명확히 하고자 가져감.
- [x] OrderTableCreateRequest
- [x] OrderTableUpdateRequest



5. Service에 있던 기능들 도메인에게 책임부여

- [x] OrderTable.updateNumberOfGuests()
- [x] OrderTable.updateEmpty()



6. Controller에서 요청받을 때 사용할 RequestDto를 만든다.

- [x] 누락된 MenuProductCreateRequest를 만듬
  - Menu가 생성될 때 MenuProduct의 Id값도 생성된다는 것을 깨달음



7. OrderStatus에 상태 검증 메서드 추가
   1. 완료 상태인지 검증
   2. 진행중인 목록을 반환하는 기능 추가



8. TableGroupService 리팩터링

   - OrderTables 일급컬렉션 생성

     - `List<OrderTable>`  관련 로직 해당 도메인에 주입

     - 생성자

       - [x] 빈 리스트 검증

       - [x] 2개 미만 리스트 검증

   - ungroup 메서드 리팩터링



9. MenuService 리팩터링
10. OrderService 리팩터링
11. JPA 적용
    - [x] OrderTable 과 TableGroup
