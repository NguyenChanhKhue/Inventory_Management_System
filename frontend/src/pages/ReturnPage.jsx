import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const ReturnPage = () => {
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [productId, setProductId] = useState("");
  const [supplierId, setSupplierId] = useState("");
  const [description, setDescription] = useState("");
  const [note, setNote] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchProductsAndSuppliers = async () => {
      try {
        const productData = await ApiService.getAllProducts();
        const supplierData = await ApiService.getAllSuppliers();
        setProducts(productData.products || []);
        setSuppliers(supplierData.suppliers || []);
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error getting products and suppliers: " + error,
        );
      }
    };

    fetchProductsAndSuppliers();
  }, []);

  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  const resetForm = () => {
    setProductId("");
    setSupplierId("");
    setDescription("");
    setNote("");
    setQuantity("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!productId || !supplierId || !quantity) {
      showMessage("Please fill in all required fields");
      return;
    }

    const body = {
      productId,
      supplierId,
      quantity: parseInt(quantity, 10),
      description,
      note,
    };

    try {
      const response = await ApiService.returnToSupplier(body);
      showMessage(response.message);
      resetForm();
    } catch (error) {
      showMessage(
        error.response?.data?.message ||
          "Error returning product to supplier: " + error,
      );
    }
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="purchase-form-page">
        <h1>Hoàn trả nhà cung cấp</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Chọn sản phẩm</label>
            <select
              value={productId}
              onChange={(e) => setProductId(e.target.value)}
              required
            >
              <option value="">Chọn sản phẩm</option>
              {products.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Chọn nhà cung cấp</label>
            <select
              value={supplierId}
              onChange={(e) => setSupplierId(e.target.value)}
              required
            >
              <option value="">Chọn nhà cung cấp</option>
              {suppliers.filter(s => s.active !== false).map((supplier) => (
                <option key={supplier.id} value={supplier.id}>
                  {supplier.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Mô tả</label>
            <input
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Ghi chú</label>
            <input
              type="text"
              value={note}
              onChange={(e) => setNote(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Số lượng trả (Tối đa: {products.find(p => p.id == productId)?.stockQuantity || 0})</label>
            <input
              type="number"
              min="1"
              max={products.find(p => p.id == productId)?.stockQuantity || ""}
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              required
            />
          </div>

          <button type="submit">Hoàn trả sản phẩm</button>
        </form>
      </div>
    </Layout>
  );
};

export default ReturnPage;
