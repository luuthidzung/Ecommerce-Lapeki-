package com.ecommerce.service;

import com.ecommerce.model.Category;

import java.util.List;

/**
 * @overview CategoryService is a class providing a number of function to be
 * implemented on Category.
 */
public interface CategoryService {
    /**
     * Insert a new category
     *
     * @param category - the category to be inserted to database
     */
    void insertCategory(Category category);

    /**
     * Update an existing category
     *
     * @param category - the category to be updated
     */
    void updateCategory(Category category);

    /**
     * Delete an existing category
     *
     * @param categoryID - the id of the category to be deleted
     */
    void deleteCategory(int categoryID);

    /**
     * Retrieve a category by its id
     *
     * @param categoryID - the id of the desired category
     */
    Category getCategoryByID(int categoryID);

    /**
     * Retrieve all categories
     *
     * @return Either the list of all categories or null if there is none
     */
    List<Category> getAllCategories();
}