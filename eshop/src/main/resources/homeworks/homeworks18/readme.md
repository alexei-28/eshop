## ДЗ#18

### Асинхронное взаимодействие

*Нужно определить, какие сервисы в вашем приложении, 
должны работать асинхронно (например, отправка уведомлений). 
Далее необходимо выбрать очередь сообщений и перевести данные 
сервисы на асинхронное взаимодействие.*

Current solution:
1. Main app "e-shop" create/update/delete order
2. Main app call API method "notifyEmail" 

POST:

http://127.0.0.1:8091/api/v1/notifyEmail?emailTo=a_subscriber0mail.ru&action=order_update&orderId=12

Response:
~~~
{"code":0,"message":"Success"}
~~~
Email send from : **otus.sa.eshop.alexei@gmail.com**

3. App "eshop-notify" receive message via API method
~~~
api/v1/notifyEmail?emailTo=email@email.com&action=order_update&orderId=10 
~~~

4.As result "eshop-notify" send notification (email, query param = "email") to user


Change to use message broker (e.g. Apache ActiveMQ)

1.Start message broker(/home/alexei/Programs/apache-activemq-5.16.0/)

Run by command in terminal:
~~~
./bin/activemq start
~~~
Open page

http://localhost:8161/admin/

Credentials: admin/amdin

2.Main app "e-shop" create/update/delete order

3.Main app send message to message broker

4.Message broker received message and send it to app "eshop-notify"

5.Start app "eshop-notify"

6.App "eshop-notify" received  message from message broker

7.As result "eshop-notify" send notification (email, query param = "email") to user
