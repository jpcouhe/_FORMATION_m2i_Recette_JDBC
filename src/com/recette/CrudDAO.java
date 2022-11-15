package com.recette;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<E> {
    List<E> findAll();

    Optional<E> findById(Long id);

    E create(E element);

}
