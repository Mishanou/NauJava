package ru.mikhail.NauJava.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.mikhail.NauJava.entities.Role;
import ru.mikhail.NauJava.entities.User;
import ru.mikhail.NauJava.repository.RoleRepository;
import ru.mikhail.NauJava.repository.UserRepository;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Все поля должны быть заполнены");
            return "register";
        }

        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Это имя пользователя уже занято");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Этот email уже зарегистрирован");
            return "register";
        }

        Role userRole = roleRepository.findByCode("USER")
                .orElseThrow(() ->
                        new IllegalStateException("Роль USER не найдена в базе данных")
                );

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(userRole);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Регистрация прошла успешно. Теперь войдите в систему."
        );

        return "redirect:/login";
    }
}
