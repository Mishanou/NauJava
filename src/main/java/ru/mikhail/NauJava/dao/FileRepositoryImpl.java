package ru.mikhail.NauJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mikhail.NauJava.entities.File;
import ru.mikhail.NauJava.entities.User;

import java.util.List;

@Repository
public class FileRepositoryImpl implements FileRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public FileRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<File> findByFileNameAndFileSizeGreaterThan(String fileName, Long fileSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<File> cq = cb.createQuery(File.class);
        Root<File> root = cq.from(File.class);

        Predicate namePredicate = cb.equal(root.get("fileName"), fileName);
        Predicate sizePredicate = cb.greaterThan(root.get("fileSize"), fileSize);

        cq.select(root).where(cb.and(namePredicate, sizePredicate));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<File> findByUploaderUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<File> cq = cb.createQuery(File.class);
        Root<File> root = cq.from(File.class);

        Join<File, User> uploaderJoin = root.join("uploader", JoinType.INNER);

        Predicate usernamePredicate = cb.equal(uploaderJoin.get("username"), username);

        cq.select(root).where(usernamePredicate);

        return entityManager.createQuery(cq).getResultList();
    }
}