import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const XuatKhoComponent = () => {
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
        error.response?.data?.message || "Lỗi khi xuất kho: " + error,
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
        <h1>Phiếu xuất kho</h1>
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
            <label>Số lượng (Tồn kho: {products.find(p => p.id == productId)?.stockQuantity || 0})</label>
            <input
              type="number"
              min="1"
              max={products.find(p => p.id == productId)?.stockQuantity || ""}
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

          <button type="submit">Tạo phiếu xuất</button>
        </form>
      </div>
    </Layout>
  );
};
export default XuatKhoComponent;
