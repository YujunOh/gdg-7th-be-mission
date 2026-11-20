# 쇼핑몰 API 명세서

모든 API 앞에 공통으로 붙는 `http://localhost:8080`은 생략

요청과 응답 본문은 모두 JSON

## 한눈에 보기

| 역할 | 기능 | Method | URI | 성공 코드 |
|---|---|---|---|---|
| 소비자 | 상품 조회 | GET | `/products?name={name}` | 200 |
| 소비자 | 상품 구매 | POST | `/orders` | 201 |
| 관리자 | 상품 등록 | POST | `/admin/products` | 201 |
| 관리자 | 재고 추가 | PATCH | `/admin/products/{productId}/stock` | 200 |
| 관리자 | 상품 삭제 | DELETE | `/admin/products` | 200 |

## 공통 에러 응답

| 상태 코드 | 의미 | 발생 상황 |
|---|---|---|
| 400 Bad Request | 요청 값이 잘못됨 | 필수 값 누락, 수량 0 이하, 가격 음수 |
| 404 Not Found | 대상이 없음 | 존재하지 않는 상품명 또는 상품 ID |
| 409 Conflict | 현재 상태와 충돌 | 중복 상품명, 재고 부족 |

---

## 소비자 (User)

### 1. 상품 조회

상품명으로 상품 한 개를 조회

상품명은 중복되지 않음.

- Method: `GET`
- URI: `/products`
- Query Parameter

| 이름 | 타입 | 필수 | 설명 |
|---|---|---|---|
| name | String | O | 조회할 상품명 |

- Request Body: 없음

- Response `200 OK`

```json
{
  "productId": 1,
  "name": "갈치",
  "price": 15000,
  "stock": 30
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| productId | Long | 상품 ID. 구매 요청에 사용 |
| name | String | 상품명 |
| price | Integer | 개당 가격 |
| stock | Integer | 재고 수량 |

- 에러: 해당 이름의 상품이 없으면 `404`, `name`이 비어 있으면 `400`

### 2. 상품 구매

하나 이상의 상품을 한 번에 구매

- Method: `POST`
- URI: `/orders`
- Request Body

```json
{
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ]
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| items | Array | O | 구매할 상품 목록. 1개 이상 |
| items[].productId | Long | O | 구매할 상품 ID |
| items[].quantity | Integer | O | 구매 수량. 1 이상 |

- Response `201 Created`

```json
{
  "totalPrice": 45000,
  "items": [
    { "name": "갈치", "quantity": 2, "amount": 30000 },
    { "name": "고등어", "quantity": 1, "amount": 15000 }
  ]
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| totalPrice | Integer | 총 구매 금액 |
| items[].name | String | 상품명 |
| items[].quantity | Integer | 구매 수량 |
| items[].amount | Integer | 해당 상품 소비 금액 (개당 가격 x 수량) |

- 에러
  - `400`: `items`가 비어 있거나 수량이 1 미만
  - `404`: 존재하지 않는 상품 ID가 하나라도 포함됨
  - `409`: 재고가 부족한 상품이 하나라도 포함됨. 이 경우 어떤 상품도 구매되지 않음

---

## 관리자 (Admin)

### 1. 상품 등록

새 상품을 등록

같은 이름의 상품이 이미 있으면 에러를 반환

- Method: `POST`
- URI: `/admin/products`
- Request Body

```json
{
  "name": "갈치",
  "price": 15000,
  "stock": 30
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| name | String | O | 상품명 |
| price | Integer | O | 개당 가격. 0 이상 |
| stock | Integer | O | 초기 재고 수량. 0 이상 |

- Response `201 Created`

```json
{
  "productId": 1,
  "name": "갈치",
  "price": 15000,
  "stock": 30
}
```

- 에러
  - `400`: 필수 값 누락, 가격 또는 재고가 음수
  - `409`: 같은 이름의 상품이 이미 존재함

### 2. 재고 추가

기존 상품의 재고를 늘림

상품 ID는 경로로, 추가할 수량은 본문으로 보냄

- Method: `PATCH`
- URI: `/admin/products/{productId}/stock`
- Path Variable

| 이름 | 타입 | 설명 |
|---|---|---|
| productId | Long | 재고를 추가할 상품 ID |

- Request Body

```json
{
  "quantity": 10
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| quantity | Integer | O | 추가할 수량. 1 이상 |

- Response `200 OK`

```json
{
  "name": "갈치",
  "stock": 40
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| name | String | 상품명 |
| stock | Integer | 추가 후 최종 재고 수량 |

- 에러
  - `400`: 수량이 1 미만
  - `404`: 존재하지 않는 상품 ID

### 3. 상품 삭제

상품을 삭제

ID 목록을 보내 여러 개를 한 번에 삭제할 수 있음

- Method: `DELETE`
- URI: `/admin/products`
- Request Body

```json
{
  "ids": [2, 3]
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| ids | List&lt;Long&gt; | O | 삭제할 상품 ID 목록. 1개 이상 |

- Response `200 OK`: 삭제 후 남아 있는 상품 목록

```json
{
  "products": [
    { "name": "고등어", "stock": 12 },
    { "name": "오징어", "stock": 7 }
  ]
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| products[].name | String | 상품명 |
| products[].stock | Integer | 재고 수량 |

- 에러
  - `400`: `ids`가 비어 있음
  - `404`: 존재하지 않는 상품 ID가 하나라도 포함됨
    - 이 경우 어떤 상품도 삭제되지 않음

---

## 메모

- 상품 조회는 상품명이 리소스의 식별자가 아니라 검색 조건이라고 보고 경로가 아닌 쿼리 파라미터로 받음

- 상품의 식별자는 `productId`

- 구매는 "주문"이라는 새 리소스를 만드는 행위이기 때문에 `POST /orders`로 설계
- 삭제시에는 응답이 남은 상품 목록이라서 `204`가 아닌 `200`을 사용
- DELETE의 요청 본문은 HTTP 표준(RFC 9110)에서 권장하지 않지만 여러 상품의 ID 목록을 한 번에 받기 위해 편의상 본문으로 받음

- 일부 클라이언트와 프록시가 DELETE의 본문을 무시할 수 있어서 Postman과 Swagger에서 동작을 확인함
- 구매와 삭제는 요청에 하나라도 문제가 있으면 전체를 처리하지 않음

- 일부만 처리되는 상태를 만들지 않기 위해서..
