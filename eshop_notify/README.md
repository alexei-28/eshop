Online education: https://otus.ru/

Course: Softwrare architect

Appliction name: eshop-notify

version: 1.0.0

port: 8091

Notification app.

API:

1.Say hello (e.g. to check is application was started)

`
GET /api/v1/hello
`

Success response:
`
{
  "date": "15.03.2020 14:15:04",
  "name": "E-shop nofification, ver.1.0.0"
}
`

2.Sending email when receive the next request (example):

`
POST /api/v1/notifyEmail?emailTo={emailTo}&action={order_create}&orderId={orderId}
`

,where:

emailTo - email to

action = order_create, order_delete, order_update

orderId = order's id

Success response:

`
{
  "code": 0,
  "message": "Success"
}
`