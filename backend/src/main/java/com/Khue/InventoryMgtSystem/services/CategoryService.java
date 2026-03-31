package com.Khue.InventoryMgtSystem.services;

import com.Khue.InventoryMgtSystem.dto.CategoryDTO;
import com.Khue.InventoryMgtSystem.dto.Response;

public interface CategoryService {
    Response createCategory (CategoryDTO categoryDTO);

    Response getAllCategory();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}
