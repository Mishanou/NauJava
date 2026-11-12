package ru.mikhail.NauJava.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mikhail.NauJava.entities.Role;
import ru.mikhail.NauJava.entities.User;
import ru.mikhail.NauJava.repository.RoleRepository;
import ru.mikhail.NauJava.repository.UserRepository;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String username;
    private String password;
    private String sessionCookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        username = "test_" + UUID.randomUUID();
        password = "TestPassword123";

        Role role = roleRepository.findByCode("USER")
                .orElseThrow(() -> new IllegalStateException("Роль USER не найдена"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);

        sessionCookie = login();
    }

    @AfterEach
    void tearDown() {
        if (username != null) {
            userRepository.findByUsername(username)
                    .ifPresent(user -> userRepository.deleteById(user.getId()));
        }
    }

    private String login() {
        Response loginPage = RestAssured
                .given()
                .redirects().follow(false)
                .when()
                .get("/login");

        String html = loginPage.asString();

        Matcher matcher = Pattern
                .compile("name=\"_csrf\"[^>]*value=\"([^\"]+)\"")
                .matcher(html);

        if (!matcher.find()) {
            throw new IllegalStateException("Не удалось получить CSRF-токен со страницы входа");
        }

        String csrfToken = matcher.group(1);
        String initialSession = loginPage.getCookie("JSESSIONID");

        assertNotNull(initialSession, "Не получена сессия JSESSIONID");

        Response loginResponse = RestAssured
                .given()
                .redirects().follow(false)
                .cookie("JSESSIONID", initialSession)
                .contentType(ContentType.URLENC)
                .formParam("username", username)
                .formParam("password", password)
                .formParam("_csrf", csrfToken)
                .when()
                .post("/login");

        assertEquals(302, loginResponse.statusCode(),
                "Не удалось войти в тестовую учётную запись");

        String authenticatedSession = loginResponse.getCookie("JSESSIONID");

        return authenticatedSession != null
                ? authenticatedSession
                : initialSession;
    }

    @Test
    void findByNameAndSize_returnsEmptyList_whenNoMatchingFileExists() {
        RestAssured
                .given()
                .cookie("JSESSIONID", sessionCookie)
                .queryParam("name", "nonexistent_" + UUID.randomUUID() + ".pdf")
                .queryParam("size", 1000L)
                .when()
                .get("/files/findByNameAndSize")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    void findByUploader_returnsEmptyList_whenUserDoesNotExist() {
        RestAssured
                .given()
                .cookie("JSESSIONID", sessionCookie)
                .queryParam("username", "nonexistent_" + UUID.randomUUID())
                .when()
                .get("/files/findByUploader")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    void findByNameAndSize_redirectsToLogin_whenNotAuthenticated() {
        RestAssured
                .given()
                .redirects().follow(false)
                .queryParam("name", "document.pdf")
                .queryParam("size", 1000L)
                .when()
                .get("/files/findByNameAndSize")
                .then()
                .statusCode(302);
    }
}
