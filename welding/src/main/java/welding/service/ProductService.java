package welding.service;

import welding.entity.Product;
import welding.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findByAvailableTrue();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndAvailableTrue(category);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            product.setAvailable(false);
            productRepository.save(product);
        }
    }
    
    @PostConstruct
    public void initSampleData() {
        if (productRepository.count() == 0) {
            // Gates
            productRepository.save(new Product("Classic Gate", "Solid iron gate with modern design", "GETI", 350000.0));
            productRepository.save(new Product("Security Gate", "Heavy duty security gate with strong locks", "GETI", 450000.0));
            productRepository.save(new Product("Decorative Gate", "Beautiful design for homes", "GETI", 380000.0));
            
            // Windows
            productRepository.save(new Product("Standard Window", "Regular sliding window", "DIRISHA", 120000.0));
            productRepository.save(new Product("French Window", "Elegant French style window", "DIRISHA", 250000.0));
            
            // Doors
            productRepository.save(new Product("Main Door", "Strong steel door for main entrance", "MLANGO", 280000.0));
            productRepository.save(new Product("Back Door", "Simple but strong back door", "MLANGO", 180000.0));
        }
    }
}