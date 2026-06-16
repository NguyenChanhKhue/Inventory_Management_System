import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const CategoryPage = () => {
  const [categories, setCategories] = useState([]);
  const [categoryName, setCategoryName] = useState("");
  const [parentId, setParentId] = useState("");
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
      showMessage("Tên danh mục không được để trống");
      return;
    }

    try {
      const payload = { name: categoryName };
      if (parentId) payload.parentId = parentId;

      const response = await ApiService.createCategory(payload);
      showMessage("Thêm danh mục thành công");
      setCategoryName(""); // clear input
      setParentId("");
      
      // Better to fetch again to get full tree or just reload page
      window.location.reload();
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error Login in a User" + error,
      );
    }
  };

  // Edit category
  const editCategory = async () => {
    try {
      const payload = { name: categoryName };
      if (parentId) payload.parentId = parentId;

      await ApiService.updateCategory(editingCategoryId, payload);
      showMessage("Cập nhật danh mục thành công");
      
      window.location.reload();
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
    setParentId(category.parentId || "");
  };

  //delete category
  const handleDeleteCategory = async (categoryId) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa danh mục này không?")) {
      try {
        await ApiService.deleteCategory(categoryId);
        showMessage("Xóa danh mục thành công");
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
          <h1>Danh mục sản phẩm</h1>
          <div className="add-cat">
            <input
              value={categoryName}
              type="text"
              placeholder="Tên danh mục"
              onChange={(e) => setCategoryName(e.target.value)}
            />
            <select
              value={parentId}
              onChange={(e) => setParentId(e.target.value)}
            >
              <option value="">-- Không có danh mục cha --</option>
              {categories.filter(c => c.id !== editingCategoryId).map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {cat.name}
                </option>
              ))}
            </select>

            {!isEditing ? (
              <button onClick={addCategory}>Thêm Danh mục</button>
            ) : (
              <button onClick={editCategory}>Sửa Danh mục</button>
            )}
          </div>
        </div>

        {categories && (
          <ul className="category-list">
            {categories.map((category) => (
              <li className="category-item" key={category.id}>
                <span>
                  {category.name} 
                  {category.parentName && <small style={{color: '#888'}}> (Thuộc: {category.parentName})</small>}
                </span>

                <div className="category-actions">
                  <button onClick={() => handleEditCategory(category)}>
                    Sửa
                  </button>
                  <button onClick={() => handleDeleteCategory(category.id)}>
                    Xóa
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
