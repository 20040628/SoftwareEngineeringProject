### Bank Card & Payment API

#### Update Bank Card

- **URL**: `/api/users/updateBankCard/{userId}`
- **Method**: `POST`
- **Headers**:
  ```
  Authorization: Bearer {token}
  Content-Type: application/json
  ```
- **Path Parameters**:
  - `userId`: User ID
- **Request Body**:
  ```json
  {
    "bankCard": "string"    // 13-19 digits bank card number
  }
  ```
- **Success Response** (200 OK):
  ```json
  {
    "message": "银行卡信息更新成功",
    "userId": "number",
    "bankCard": "string"
  }
  ```
- **Error Response** (400 Bad Request):
  ```json
  {
    "message": "Invalid bank card format"
  }
  ```

#### Check Bank Card

- **URL**: `/api/bank-payment/check-card/{userId}`
- **Method**: `GET`
- **Headers**:
  ```
  Authorization: Bearer {token}
  ```
- **Path Parameters**:
  - `userId`: User ID
- **Success Response** (200 OK):
  ```json
  {
    "hasBankCard": true,
    "maskedCard": "**** **** **** 1234",
    "bankBalance": 5000.00
  }
  ```
- **Error Response** (400 Bad Request):
  ```json
  {
    "message": "User not found"
  }
  ```

#### Bank Card Payment (with Deposit)

- **URL**: `/api/bank-payment/{orderId}`
- **Method**: `POST`
- **Headers**:
  ```
  Authorization: Bearer {token}
  Content-Type: application/json
  ```
- **Path Parameters**:
  - `orderId`: Order ID
- **Request Body**:
  ```json
  {
    "securityCode": "string"  // 3-4 digits security code
  }
  ```
- **Success Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "支付成功",
    "orderId": "number",
    "bankCardLast4": "string",
    "amount": "number",       // Total amount including deposit (50 yuan)
    "paymentTime": "datetime"
  }
  ```
- **Error Response** (400 Bad Request):
  ```json
  {
    "message": "银行卡余额不足，无法完成支付"
  }
  ```

#### Alipay Payment (without Deposit)

- **URL**: `/api/alipay-payment/{orderId}`
- **Method**: `POST`
- **Headers**:
  ```
  Authorization: Bearer {token}
  ```
- **Path Parameters**:
  - `orderId`: Order ID
- **Success Response** (200 OK):
  ```json
  {
    "success": true,
    "message": "支付宝支付成功",
    "orderId": "number",
    "amount": "number",       // Only order price, no deposit
    "paymentTime": "datetime"
  }
  ```
- **Error Response** (400 Bad Request):
  ```json
  {
    "message": "订单状态不正确，无法支付"
  }
  ```

### Return Scooter API

#### Return Scooter

- **URL**: `/api/bookings/return`
- **Method**: `POST`
- **Headers**:
  ```
  Authorization: Bearer {token}
  Content-Type: application/json
  ```
- **Request Body**:
  ```json
  {
    "orderId": "number",
    "remarks": "string"       // Optional remarks
  }
  ```
- **Success Response** (200 OK):
  ```json
  {
    "message": "电动车归还成功",
    "orderId": "number",
    "returnTime": "datetime",
    "depositRefunded": "boolean",
    "depositAmount": "number",      // Included if depositRefunded is true
    "depositMessage": "string"      // Included if depositRefunded is true
  }
  ```
- **Error Response** (400 Bad Request):
  ```json
  {
    "message": "只有活跃订单才能进行还车操作"
  }
  ``` 