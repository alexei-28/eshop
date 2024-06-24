# eshop
OTUS: Software architect - eshop

Online education: https://otus.ru/

Course: Software architect

Appliction name: e-shop (online shop)

version: 1.0.2

Example to run container from image:
1. Create image (in folder /eshop/eshop/) from Dockerfile
   
    docker build .

2. Run container from image

   docker run -p 8091:8090 --name container_name image_id

3. Open browser on page:

   http://127.0.0.1:8091

Main app.

1. Registration of users
2. CRUD categories/users/orders
3. When crate/update/delete orders then create http POST request to eshop-notify application.

Example of request (create order):

`
POST http://localhost:8091/api/v1/notifyEmail?emailTo=a_subscriber@mail.ru&action=order_create&orderId=10
`
