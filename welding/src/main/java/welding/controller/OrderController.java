package welding.controller;

import welding.entity.Order;
import welding.entity.Product;
import welding.service.OrderService;
import welding.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/new/{productId}")
    public String showOrderForm(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("order", new Order());
        return "order-form";
    }
    
    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order, 
                              @RequestParam Long productId,
                              Model model) {
        Order savedOrder = orderService.createOrder(order, productId);
        if (savedOrder != null) {
            model.addAttribute("success", "Order submitted successfully!");
            return "order-success";
        }
        model.addAttribute("error", "Failed to submit order");
        return "order-form";
    }
    
    @GetMapping("/list")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "order-list";
    }
    
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order-detail";
    }
    
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, 
                               @RequestParam Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders/" + id;
    }
}