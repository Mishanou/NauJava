package ru.mikhail.NauJava.util;

import org.springframework.stereotype.Component;
import ru.mikhail.NauJava.entities.FileEntity;
import ru.mikhail.NauJava.entities.Role;
import ru.mikhail.NauJava.entities.ShareLink;
import ru.mikhail.NauJava.entities.User;
import ru.mikhail.NauJava.repository.FileRepository;
import ru.mikhail.NauJava.repository.RoleRepository;
import ru.mikhail.NauJava.repository.ShareLinkRepository;
import ru.mikhail.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TestDataFactory {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ShareLinkRepository shareLinkRepository;

    public TestDataFactory(RoleRepository roleRepository,
                           UserRepository userRepository,
                           FileRepository fileRepository,
                           ShareLinkRepository shareLinkRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.shareLinkRepository = shareLinkRepository;
    }

    public Role createRole() {
        Role role = new Role();
        role.setName("ROLE_" + UUID.randomUUID());
        role.setCode("USER");
        return roleRepository.save(role);
    }

    public User createUser(Role role) {
        User user = new User();
        user.setUsername("user_" + UUID.randomUUID());
        user.setEmail(UUID.randomUUID() + "@test.com");
        user.setPasswordHash("hash");
        user.setRole(role);
        return userRepository.save(user);
    }

    public FileEntity createFile(User uploader, String fileName, long size) {
        FileEntity file = new FileEntity();
        file.setFileName(fileName);
        file.setFileSize(size);
        file.setContentType("application/pdf");
        file.setUploader(uploader);
        return fileRepository.save(file);
    }

    public ShareLink createShareLink(FileEntity file) {
        ShareLink link = new ShareLink();
        link.setToken(UUID.randomUUID().toString());
        link.setIsActive(true);
        link.setExpirationDate(LocalDateTime.now().plusDays(1));
        link.setFile(file);
        return shareLinkRepository.save(link);
    }
}