package ru.mikhail.NauJava.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mikhail.NauJava.entities.Role;
import ru.mikhail.NauJava.entities.User;
import ru.mikhail.NauJava.repository.RoleRepository;
import ru.mikhail.NauJava.repository.UserRepository;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginUITest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebDriver driver;
    private String username;

    private final String password = "secret123";

    @BeforeEach
    void setUp() {
        username = "autotest_" + UUID.randomUUID();
        String email = username + "@test.com";

        Role role = roleRepository.findByCode("USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            newRole.setCode("USER");
            return roleRepository.save(newRole);
        });

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        if (username != null) {
            userRepository.findByUsername(username)
                    .ifPresent(user -> userRepository.deleteById(user.getId()));
        }
    }

    @Test
    void testSuccessfulLoginAndLogout() {
        driver.get("http://localhost:" + port + "/login");

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(
                By.cssSelector("button[type='submit']")
        );

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        submitButton.click();

        Assertions.assertTrue(
                driver.getPageSource().contains("File Sharing Platform")
                        || driver.getCurrentUrl().endsWith("/"),
                "После входа должна открыться главная страница"
        );

        WebElement logoutButton = driver.findElement(
                By.cssSelector("form[action$='/logout'] button[type='submit']")
        );
        logoutButton.click();

        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/login"),
                "После выхода пользователь должен попасть на страницу входа"
        );
    }
}
