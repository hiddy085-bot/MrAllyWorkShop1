package welding.service;

import welding.entity.User;
import welding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;  // 🔴 ONGEZA HII IMPORT
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    @Lazy  // 🔴 ONGEZA HII ANNOTATION - HII NDIO SULUHISHO!
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("==========================================");
        System.out.println("🔍 TRYING TO LOGIN WITH USERNAME: '" + username + "'");
        System.out.println("==========================================");
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.out.println("❌ USER NOT FOUND IN DATABASE: '" + username + "'");
                return new UsernameNotFoundException("User not found: " + username);
            });
        
        System.out.println("✅ USER FOUND IN DATABASE:");
        System.out.println("   - Username: " + user.getUsername());
        System.out.println("   - Email: " + user.getEmail());
        System.out.println("   - Role: " + user.getRole());
        System.out.println("   - Password (encoded): " + user.getPassword());
        System.out.println("   - Enabled: " + user.isEnabled());
        System.out.println("==========================================");
        
        return user;
    }
    
    public User registerNewUser(User user) {
        System.out.println("==========================================");
        System.out.println("📝 REGISTERING NEW USER: " + user.getUsername());
        System.out.println("==========================================");
        
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            System.out.println("❌ USERNAME ALREADY EXISTS: " + user.getUsername());
            throw new RuntimeException("Username already exists!");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            System.out.println("❌ EMAIL ALREADY EXISTS: " + user.getEmail());
            throw new RuntimeException("Email already registered!");
        }
        
        // Store raw password for logging
        String rawPassword = user.getPassword();
        
        // Encode password
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        
        System.out.println("   - Raw password: " + rawPassword);
        System.out.println("   - Encoded password: " + encodedPassword);
        System.out.println("   - Password matches: " + passwordEncoder.matches(rawPassword, encodedPassword));
        
        // Set default role
        user.setRole(User.Role.CUSTOMER);
        
        // Save user
        User savedUser = userRepository.save(user);
        System.out.println("✅ USER REGISTERED SUCCESSFULLY! ID: " + savedUser.getId());
        System.out.println("==========================================");
        
        return savedUser;
    }
    
    public User findByUsername(String username) {
        System.out.println("🔍 FINDING USER BY USERNAME: " + username);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            System.out.println("✅ USER FOUND: " + user.getUsername());
        } else {
            System.out.println("❌ USER NOT FOUND: " + username);
        }
        return user;
    }
    
    public User findByEmail(String email) {
        System.out.println("🔍 FINDING USER BY EMAIL: " + email);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            System.out.println("✅ USER FOUND: " + user.getEmail());
        } else {
            System.out.println("❌ USER NOT FOUND: " + email);
        }
        return user;
    }
}