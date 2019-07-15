package com.revature;

import java.util.List;

//DAO
public interface Dao<E> {
    // CRUD - Create, Read, Update, Delete
    void insert(E e);

    List<E> getAll();

    void update();

    void delete();
}