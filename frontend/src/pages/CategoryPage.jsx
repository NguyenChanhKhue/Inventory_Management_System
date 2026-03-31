import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const CategoryPage = () => {
  const [categories, setCategories] = useState([]);
  const [categoryName, setCategoryName] = useState("");
  const [message, setMessage] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editingCategoryId, setEditingCategoryId] = useState(null);

  // fetch the categories is received from backend

  useEffect(() => {
    const getCategories = async () => {
      try {
        const response = await ApiService.getAllCategory();
        if (response.status === 200) {
          setCategories(response.categories);
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error Loggin in a User: " + error,
        );
      }
    };

    getCategories();
  }, []);

  //add category
  const addCategory = async () => {
    if (!categoryName) {
      showMessage("Category name can not empty");
      return;
    }

    try {
      const response = await ApiService.createCategory({ name: categoryName });
      showMessage("Category successfully added");
      setCategoryName(""); // clear input
      setCategories((prevCategories) => [
        ...prevCategories,
        response.category ?? { id: Date.now(), name: categoryName },
      ]);
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error Login in a User" + error,
      );
    }
  };

  // Edit category
  const editCategory = async () => {
    try {
      await ApiService.updateCategory(editingCategoryId, {
        name: categoryName,
      });
      showMessage("Category successfully Updated");
      setCategories((prevCategories) =>
        prevCategories.map((category) =>
          category.id === editingCategoryId
            ? { ...category, name: categoryName }
            : category,
        ),
      );
      setIsEditing(false);
      setEditingCategoryId(null);
      setCategoryName("");
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error Loggin in a User: " + error,
      );
    }
  };

  //populate the edit category data
  const handleEditCategory = (category) => {
    setIsEditing(true);
    setEditingCategoryId(category.id);
    setCategoryName(category.name);
  };

  //delete category
  const handleDeleteCategory = async (categoryId) => {
    if (window.confirm("Are you sure you want to delete this category?")) {
      try {
        await ApiService.deleteCategory(categoryId);
        showMessage("Category sucessfully Deleted");
        setCategories((prevCategories) =>
          prevCategories.filter((category) => category.id !== categoryId),
        );
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error Deleting in a Category: " + error,
        );
      }
    }
  };

  //show mess
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="category-page">
        <div className="category-header">
          <h1>Categories</h1>
          <div className="add-cat">
            <input
              value={categoryName}
              type="text"
              placeholder="Category Name"
              onChange={(e) => setCategoryName(e.target.value)}
            />

            {!isEditing ? (
              <button onClick={addCategory}>Add Category</button>
            ) : (
              <button onClick={editCategory}>Edit Category</button>
            )}
          </div>
        </div>

        {categories && (
          <ul className="category-list">
            {categories.map((category) => (
              <li className="category-item" key={category.id}>
                <span>{category.name}</span>

                <div className="category-actions">
                  <button onClick={() => handleEditCategory(category)}>
                    Edit
                  </button>
                  <button onClick={() => handleDeleteCategory(category.id)}>
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </Layout>
  );
};

export default CategoryPage;
