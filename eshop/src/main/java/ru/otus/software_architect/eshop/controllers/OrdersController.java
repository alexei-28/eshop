package ru.otus.software_architect.eshop.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.Response;
import ru.otus.software_architect.eshop.api.DefaultRestClientCallback;
import ru.otus.software_architect.eshop.api.ErrorResponse;
import ru.otus.software_architect.eshop.model.NotifyActionEnum;
import ru.otus.software_architect.eshop.model.Orders;
import ru.otus.software_architect.eshop.model.User;
import ru.otus.software_architect.eshop.model.ViewMode;
import ru.otus.software_architect.eshop.repo.CategoryRepository;
import ru.otus.software_architect.eshop.repo.OrderRepository;
import ru.otus.software_architect.eshop.repo.UserRepository;
import ru.otus.software_architect.eshop.service.TransportService;
import ru.otus.software_architect.eshop.service.UserService;
import ru.otus.software_architect.eshop.util.DateUtil;
import ru.otus.software_architect.eshop.util.StringUtll;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class OrdersController {
    @Autowired
    private ApplicationContext context;
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private User currentUser;
    @Autowired
    private JmsTemplate jmsTemplate;
    private static final String ESHOP_QUEUE = "eshop_queue";

    private static Logger logger = LogManager.getLogger(OrdersController.class);

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        currentUser = userRepository.findByUsername(UserService.getCurrentUserName());
        List<Orders> orders = orderRepository.findByUser(currentUser);
        model.addAttribute("ordersList", orders);
        model.addAttribute("appName", appName);
        return "orders";
    }

    @RequestMapping("order/add")
    public String addOrder(Model model) {
        logger.info("addOrder,categoriesList = " + categoryRepository.findAll());
        model.addAttribute("isAdd", true);
        model.addAttribute("order", new Orders());
        model.addAttribute("title", "Add Order");
        model.addAttribute("viewMode", ViewMode.ADD);
        model.addAttribute("categoriesList", categoryRepository.findAll());
        return "order";
    }

    @RequestMapping("order/view/{id}")
    public String viewOrder(@PathVariable("id") int id, Model model) {
        Optional<Orders> order = orderRepository.findById(id);
        logger.info("find_order = " + order);
        model.addAttribute("isView", true);
        model.addAttribute("order", order);
        model.addAttribute("title", "View Order");
        model.addAttribute("categoriesList", categoryRepository.findAll());
        return "order";
    }

    @RequestMapping("order/edit/{id}")
    public String editOrder(@PathVariable("id") int id, Model model) {
        Optional<Orders> order = orderRepository.findById(id);
        logger.info("find_order = " + order);
        model.addAttribute("order", order);
        model.addAttribute("title", "Edit Order");
        model.addAttribute("viewMode", ViewMode.EDIT);
        model.addAttribute("categoriesList", categoryRepository.findAll());
        return "order";
    }

    @RequestMapping("order/delete/{id}")
    public String deleteOrder(@PathVariable("id") int orderId) {
        orderRepository.deleteById(orderId);
        logger.info("success_delete_orderId = " + orderId);
        //notifyByEmail(NotifyActionEnum.ORDER_DELETE, orderId);
        publishToMessageBroker(NotifyActionEnum.ORDER_DELETE, orderId);
        return "redirect:/orders";
    }

    @PostMapping(value = "/order")
    public String submitOrder(Orders order, Model model) {
        logger.info("submitOrder = " + order);
        NotifyActionEnum notifyActionEnum = NotifyActionEnum.ORDER_CREATE;
        if (order.getId() == 0) { // add order
            order.setCreated(new Date());
        } else { // update order
            order.setUpdated(new Date());
            notifyActionEnum = NotifyActionEnum.ORDER_UPDATE;
        }
        order.setUser(currentUser);
        orderRepository.save(order);
        //notifyByEmail(notifyActionEnum, order.getId());
        publishToMessageBroker(notifyActionEnum, order.getId());
        return "redirect:/orders";
    }

    private void notifyByEmail(NotifyActionEnum action, int orderId) {
        logger.info("notifyByEmail:");
        String currentUserName = UserService.getCurrentUserName();
        TransportService.notifyByEmail(currentUserName, action, orderId, new DefaultRestClientCallback<JsonElement>() {
            @Override
            public void onSuccess(Response<JsonElement> response) {
                //super.onSuccess(response);
                JsonElement responseJson = response.body();
                logger.info("notifyByEmail: onSuccess: responseJson = " + responseJson);
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                //super.onError(errorResponse);
                logger.error("notifyByEmail: error = " + errorResponse);
            }
        });
    }

    // Send message(json) to message broker (e.g. Apache ActiveMQ, http://127.0.0.1:8161/admin/)
    private String publishToMessageBroker(NotifyActionEnum action, int orderId) {
        logger.info("publishToMessageBroker:");
        JsonObject json = new JsonObject();
        json.addProperty("email", UserService.getCurrentUserName());
        json.addProperty("action", action.name().toLowerCase());
        json.addProperty("orderId", orderId);
        json.addProperty("createdAt", DateUtil.date2String(new Date(), DateUtil.JSON_DATE_FORMAT));
        String message = json.toString();
        String urlEncodeMessage = StringUtll.urlEncode(message);
        jmsTemplate.convertAndSend(ESHOP_QUEUE, urlEncodeMessage);
        logger.info("publishToMessageBroker: success_sent_message_to_MB: " + message);
        return "Message was success published on Message Broker!"; // can use as response
    }
}