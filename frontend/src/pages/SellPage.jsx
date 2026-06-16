import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const SellPage = () => {
  const [products, setProducts] = useState([]);
  const [productId, setProductId] = useState("");
  const [description, setDescription] = useState("");
  const [note, setNote] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const productData = await ApiService.getAllProducts();
        setProducts(productData.products);
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Lỗi khi tải dữ liệu sản phẩm: " + error,
        );
      }
    };

    fetchProducts();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!productId || !quantity) {
      showMessage("Vui lòng điền đầy đủ các trường bắt buộc");
      return;
    }
    const body = {
      productId,
      quantity: parseInt(quantity),
      description,
      note,
    };

    try {
      const respone = await ApiService.sellProduct(body);
      showMessage(respone.message);
      resetForm();
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Lỗi khi xuất bán sản phẩm: " + error,
      );
    }
  };

  const resetForm = () => {
    setProductId("");
    setDescription("");
    setNote("");
    setQuantity("");
  };

  //metjhod to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="purchase-form-page">
        <h1>Xuất bán sản phẩm</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Chọn sản phẩm</label>

            <select
              value={productId}
              onChange={(e) => setProductId(e.target.value)}
              required
            >
              <option value="">Chọn một sản phẩm</option>
              {products.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Số lượng</label>
            <input
              type="number"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              required
            />
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

          <button type="submit">Xuất bán</button>
        </form>
      </div>
    </Layout>
  );
};
export default SellPage;
