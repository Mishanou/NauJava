package ru.mikhail.NauJava.repository;

public interface CrudRepository<T, ID>
{
    void create(T entity);
    T read(ID id);
    void update(T entity);
    boolean delete(ID id);
}

