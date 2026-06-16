import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import { useNavigate } from "react-router-dom";
import PaginationComponent from "../components/PaginationComponents";

const ProductPage = () => {
  const [products, setProducts] = useState([]);
  const [message, setMessage] = useState("");
  const [filter, setFilter] = useState("");
  const [valueToSearch, setValueToSearch] = useState("");

  const navigate = useNavigate();

  //Pagination Set-Up
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const itemsPerPage = 10;

  useEffect(() => {
    const getProducts = async () => {
      try {
        const productData = valueToSearch
          ? await ApiService.searchProduct(valueToSearch)
          : await ApiService.getAllProducts();

        if (productData.status === 200) {
          const fetchedProducts = productData.products || [];

          setTotalPages(Math.ceil(fetchedProducts.length / itemsPerPage));

          setProducts(
            fetchedProducts.slice(
              (currentPage - 1) * itemsPerPage,
              currentPage * itemsPerPage,
            ),
          );
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Lỗi khi lấy dữ liệu sản phẩm: " + error,
        );
      }
    };

    getProducts();
  }, [currentPage, valueToSearch]);

  //Delete a product
  const handleDeleteProduct = async (productId) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) {
      try {
        await ApiService.deleteProduct(productId);
        showMessage("Đã xóa sản phẩm thành công");
        window.location.reload(); //relode page
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Lỗi khi xóa sản phẩm: " + error,
        );
      }
    }
  };

  //metjhod to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  const handleSearch = () => {
    setCurrentPage(1);
    setValueToSearch(filter.trim());
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}

      <div className="product-page">
        <div className="product-header">
          <h1>Sản phẩm</h1>
          <div className="product-header-actions">
            <div className="product-search">
              <input
                type="text"
                placeholder="Tìm kiếm sản phẩm ..."
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
              />
              <button type="button" onClick={handleSearch}>
                Tìm kiếm
              </button>
            </div>

            <button
              className="add-product-btn"
              onClick={() => navigate("/add-product")}
            >
              Thêm sản phẩm
            </button>
          </div>
        </div>

        {products && (
          <div className="product-list">
            {products.map((product) => (
              <div key={product.id} className="product-item">
                <img
                  className="product-image"
                  src={ApiService.resolveAssetUrl(product.imgUrl)}
                  alt={product.name}
                />

                <div className="product-info">
                  <h3 className="name">{product.name}</h3>
                  <p className="sku">Mã: {product.su}</p>
                  <p className="price">Giá: {product.price}</p>
                  <p className="quantity">Số lượng: {product.stockQuantity}</p>
                </div>

                <div className="product-actions">
                  <button
                    className="edit-btn"
                    onClick={() => navigate(`/edit-product/${product.id}`)}
                  >
                    Sửa
                  </button>
                  <button
                    className="delete-btn"
                    onClick={() => handleDeleteProduct(product.id)}
                  >
                    Xóa
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <PaginationComponent
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />
    </Layout>
  );
};
export default ProductPage;
