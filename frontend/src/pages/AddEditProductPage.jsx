import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const AddEditProductPage = () => {
  const { productId } = useParams("");
  const [name, setName] = useState("");
  const [sku, setSku] = useState("");
  const [price, setPrice] = useState("");
  const [stockQuantity, setStokeQuantity] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [imageUrl, setImageUrl] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [categories, setCategories] = useState([]);
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const categoriesData = await ApiService.getAllCategory();
        setCategories(categoriesData.categories);
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Lỗi khi tải danh mục: " + error,
        );
      }
    };

    const fetProductById = async () => {
      if (productId) {
        setIsEditing(true);
        try {
          const productData = await ApiService.getProductById(productId);
          if (productData.status === 200) {
            setName(productData.product.name);
            setSku(productData.product.sku);
            setPrice(productData.product.price);
            setStokeQuantity(productData.product.stockQuantity);
            setCategoryId(productData.product.categoryId);
            setDescription(productData.product.description);
            setImageUrl(productData.product.imgUrl);
          } else {
            showMessage(productData.message);
          }
        } catch (error) {
          showMessage(
            error.response?.data?.message ||
              "Lỗi khi lấy thông tin sản phẩm: " + error,
          );
        }
      }
    };

    fetchCategories();
    if (productId) fetProductById();
  }, [productId]);

  //metjhod to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file);
    const reader = new FileReader();
    reader.onloadend = () => setImageUrl(reader.result); //user imgurl to preview the image to upload
    reader.readAsDataURL(file);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("name", name);
    formData.append("sku", sku);
    formData.append("price", price);
    formData.append("stockQuantity", stockQuantity);
    formData.append("categoryId", categoryId);
    formData.append("description", description);
    if (imageFile) {
      formData.append("imageFile", imageFile);
    }

    try {
      if (isEditing) {
        formData.append("productId", productId);
        await ApiService.updateProduct(formData);
        showMessage("Cập nhật sản phẩm thành công");
      } else {
        await ApiService.addProduct(formData);
        showMessage("Thêm sản phẩm thành công 🤩");
      }
      navigate("/product");
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Lỗi khi lưu sản phẩm: " + error,
      );
    }
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}

      <div className="product-form-page">
        <h1>{isEditing ? "Sửa sản phẩm" : "Thêm sản phẩm"}</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tên sản phẩm</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Mã sản phẩm (SKU)</label>
            <input
              type="text"
              value={sku}
              onChange={(e) => setSku(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Số lượng kho</label>
            <input
              type="number"
              value={stockQuantity}
              onChange={(e) => setStokeQuantity(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Giá bán</label>
            <input
              type="number"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Mô tả</label>

            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Danh mục</label>

            <select
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
              required
            >
              <option value="">Chọn một danh mục</option>

              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Hình ảnh sản phẩm</label>
            <input type="file" onChange={handleImageChange} />

            {imageUrl && (
              <img
                src={ApiService.resolveAssetUrl(imageUrl)}
                alt="preview"
                className="image-preview"
              />
            )}
          </div>
          <button type="submit">
            {isEditing ? "Lưu thay đổi" : "Thêm sản phẩm"}
          </button>
        </form>
      </div>
    </Layout>
  );
};

export default AddEditProductPage;
