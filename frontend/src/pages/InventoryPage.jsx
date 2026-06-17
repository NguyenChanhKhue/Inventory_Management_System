import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import PaginationComponent from "../components/PaginationComponents";

const InventoryPage = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [message, setMessage] = useState("");
  const [filter, setFilter] = useState("");
  const [valueToSearch, setValueToSearch] = useState("");

  // Adjustment State
  const [adjustingProductId, setAdjustingProductId] = useState(null);
  const [adjustQuantity, setAdjustQuantity] = useState("");
  const [adjustReason, setAdjustReason] = useState("");

  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const itemsPerPage = 10;

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
        error.response?.data?.message || "Lỗi khi tải kho hàng: " + error,
      );
    }
  };

  useEffect(() => {
    getProducts();
  }, [currentPage, valueToSearch]);

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

  const startAdjusting = (product) => {
    setAdjustingProductId(product.id);
    setAdjustQuantity(product.stockQuantity);
    setAdjustReason("");
  };

  const cancelAdjusting = () => {
    setAdjustingProductId(null);
  };

  const submitAdjust = async (productId) => {
    if (adjustQuantity === "" || adjustQuantity < 0) {
      showMessage("Số lượng thực tế không hợp lệ!");
      return;
    }

    try {
      const payload = {
        productId: productId,
        quantity: parseInt(adjustQuantity, 10),
        description: "Kiểm kê kho",
        note: adjustReason,
      };

      await ApiService.adjustStock(payload);
      showMessage("Điều chỉnh tồn kho thành công!");
      setAdjustingProductId(null);
      getProducts(); // Refresh list
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Lỗi khi điều chỉnh: " + error,
      );
    }
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}

      <div className="product-page">
        <div className="product-header">
          <h1>Quản lý Tồn kho</h1>
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
                  <p className="sku">Mã (SKU): {product.sku}</p>
                  <p className="quantity">
                    Tồn kho hiện tại:{" "}
                    <strong
                      style={{
                        color: product.stockQuantity <= 5 ? "red" : "green",
                      }}
                    >
                      {product.stockQuantity}
                    </strong>
                  </p>
                </div>

                <div className="product-actions" style={{ flexDirection: "column", gap: "10px" }}>
                  {adjustingProductId === product.id ? (
                    <div style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
                      <input
                        type="number"
                        placeholder="Số lượng thực tế"
                        value={adjustQuantity}
                        onChange={(e) => setAdjustQuantity(e.target.value)}
                        style={{ padding: "5px" }}
                      />
                      <input
                        type="text"
                        placeholder="Lý do (vd: Hư hỏng...)"
                        value={adjustReason}
                        onChange={(e) => setAdjustReason(e.target.value)}
                        style={{ padding: "5px" }}
                      />
                      <div style={{ display: "flex", gap: "5px" }}>
                        <button
                          className="edit-btn"
                          onClick={() => submitAdjust(product.id)}
                        >
                          Lưu
                        </button>
                        <button className="delete-btn" onClick={cancelAdjusting}>
                          Hủy
                        </button>
                      </div>
                    </div>
                  ) : (
                    <div style={{ display: "flex", gap: "5px", justifyContent: "center" }}>
                      <button
                        className="edit-btn"
                        onClick={() => startAdjusting(product)}
                        style={{ backgroundColor: "#f39c12" }}
                      >
                        Kiểm kê / Điều chỉnh
                      </button>
                      <button
                        className="view-map-btn"
                        onClick={() => navigate(`/warehouse-map?location=${encodeURIComponent(product.location || '')}`)}
                        style={{ backgroundColor: '#2196f3', color: 'white', border: 'none', padding: '8px 12px', borderRadius: '4px', cursor: 'pointer' }}
                      >
                        Sơ đồ kho
                      </button>
                    </div>
                  )}
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

export default InventoryPage;
