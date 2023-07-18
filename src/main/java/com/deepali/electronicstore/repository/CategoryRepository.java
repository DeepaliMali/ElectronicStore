package com.deepali.electronicstore.repository;

import com.deepali.electronicstore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {
}
