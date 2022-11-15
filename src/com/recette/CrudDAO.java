package com.recette;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<E> {
    List<E> findAll();
    Optional<E> findById(int id);
    List<E> findByKeyword(String keyword);
    E create(E element) throws SQLException;
    void cookedAt(int id, LocalDate date) throws SQLException;
}
