# eshop
OTUS: Software architect - eshop

Online education: https://otus.ru/

Course: Software architect

Appliction name: e-shop (online shop)

version: 1.0.1

port: 8090

Main app.

1. Registration of users
2. CRUD categories/users/orders
3. When crate/update/delete orders then create http POST request to eshop-notify application.

Example of request (create order):

`
POST http://localhost:8091/api/v1/notifyEmail?emailTo=a_subscriber@mail.ru&action=order_create&orderId=10
`
