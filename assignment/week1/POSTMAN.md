# POSTMAN 스크린샷

## 정상 요청

### 01 상품 등록 - 갈치 (POST /admin/products, 201 Created)
![01 상품 등록 - 갈치 (POST /admin/products, 201 Created)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B710%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%282%29.png)

### 02 상품 등록 - 고등어 (POST /admin/products, 201 Created)
![02 상품 등록 - 고등어 (POST /admin/products, 201 Created)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B79%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%283%29.png)

### 03 상품 등록 - 오징어 (POST /admin/products, 201 Created)
![03 상품 등록 - 오징어 (POST /admin/products, 201 Created)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B78%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%284%29.png)

### 04 상품 조회 - 갈치 (GET /products?name=갈치, 200 OK)
![04 상품 조회 - 갈치 (GET /products?name=갈치, 200 OK)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B77%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%285%29.png)

### 05 재고 추가 - 갈치 +10 (PATCH /admin/products/1/stock, 200 OK)
![05 재고 추가 - 갈치 +10 (PATCH /admin/products/1/stock, 200 OK)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B76%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%286%29.png)

### 06 상품 구매 - 갈치 2개, 고등어 1개 (POST /orders, 201 Created)
![06 상품 구매 - 갈치 2개, 고등어 1개 (POST /orders, 201 Created)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B75%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%287%29.png)

### 07 상품 삭제 - 고등어, 오징어 (DELETE /admin/products, 200 OK)
![07 상품 삭제 - 고등어, 오징어 (DELETE /admin/products, 200 OK)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B711%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%281%29.png)

## 에러 케이스

### E1 중복 상품명 등록 (POST /admin/products, 409 Conflict)
![E1 중복 상품명 등록 (POST /admin/products, 409 Conflict)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B74%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%288%29.png)

### E2 없는 상품 조회 (GET /products?name=없는상품, 404 Not Found)
![E2 없는 상품 조회 (GET /products?name=없는상품, 404 Not Found)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B73%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%289%29.png)

### E3 재고 부족 구매 (POST /orders, 409 Conflict)
![E3 재고 부족 구매 (POST /orders, 409 Conflict)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B72%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%2810%29.png)

### E4 재고 추가 수량 0 (PATCH /admin/products/1/stock, 400 Bad Request)
![E4 재고 추가 수량 0 (PATCH /admin/products/1/stock, 400 Bad Request)](Postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7/postman%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B71%20%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202026-10-29%20211318%20%2811%29.png)
