package welding.controller;

import welding.entity.Product;
import welding.entity.Order;
import welding.service.ProductService;
import welding.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("productCount", productService.getAllProducts().size());
        model.addAttribute("orderCount", orderService.getAllOrders().size());
        
        List<Order> recentOrders = orderService.getAllOrders();
        if (recentOrders.size() > 5) {
            recentOrders = recentOrders.subList(0, 5);
        }
        model.addAttribute("recentOrders", recentOrders);
        
        return "admin/dashboard";
    }
    
    @GetMapping("/products")
    public String manageProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }
    
    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }
    
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "admin/product-form";
    }
    
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product) {
        if (product.getId() == null) {
            // New product
            productService.saveProduct(product);
        } else {
            // Update existing product
            Product existing = productService.getProductById(product.getId());
            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setCategory(product.getCategory());
            existing.setPrice(product.getPrice());
            existing.setMaterial(product.getMaterial());
            existing.setDimensions(product.getDimensions());
            existing.setEstimatedDays(product.getEstimatedDays());
            existing.setAvailable(product.getAvailable());
            productService.saveProduct(existing);
        }
        return "redirect:/admin/products";
    }
    
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
    
    @GetMapping("/orders")
    public String manageOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }
    
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, Order.OrderStatus.valueOf(status));
        return "redirect:/admin/orders";
    }
}