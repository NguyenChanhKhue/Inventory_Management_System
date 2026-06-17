import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const ChiTietChungTuComponent = () => {
  const { transactionId } = useParams();
  const [transaction, setTransaction] = useState(null);
  const [message, setMessage] = useState("");
  const [status, setStatus] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const getTransaction = async () => {
      try {
        const transactionData =
          await ApiService.getTransactionById(transactionId);

        if (transactionData.status === 200) {
          setTransaction(transactionData.transaction);
          setStatus(transactionData.transaction.transactionStatus);
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error Getting a transaction: " + error,
        );
      }
    };

    getTransaction();
  }, [transactionId]);

  //update transaction status
  const handleUpdateStatus = async () => {
    try {
      await ApiService.updateTransactionStatus(transactionId, status);
      navigate("/lich-su-chung-tu");
    } catch (error) {
      showMessage(
        error.response?.data?.message ||
          "Error Updating a transactions: " + error,
      );
    }
  };

  //Method to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <p className="message">{message}</p>}
      <div className="transaction-details-page">
        {transaction && (
          <>
            {/* Transaction base information */}
            <div className="section-card">
              <h2>Thông tin chứng từ</h2>
              <p>Loại giao dịch: {{ IMPORT: 'Nhập kho', EXPORT: 'Xuất kho', ADJUSTMENT: 'Điều chỉnh', RETURN_TO_SUPPLIER: 'Hoàn trả NCC' }[transaction.transactionType] || transaction.transactionType}</p>
              <p>Trạng thái: {{ COMPLETED: 'Hoàn thành', CANCELLED: 'Đã hủy', PENDING: 'Chờ xử lý', PROCESSING: 'Đang xử lý' }[transaction.transactionStatus] || transaction.transactionStatus}</p>
              <p>Mô tả: {transaction.description}</p>
              <p>Ghi chú: {transaction.note}</p>
              <p>Tổng sản phẩm: {transaction.totalProduct}</p>
              <p>Tổng tiền: {transaction.totalPrice.toFixed(2)}</p>
              <p>
                Thời gian tạo: {new Date(transaction.createAt).toLocaleString()}
              </p>

              {transaction.updateddAt && (
                <p>
                  Thời gian cập nhật: {new Date(transaction.updatedAt).toLocaleString()}
                </p>
              )}
            </div>



            {/* Product information of the transaction */}
            <div className="section-card">
              <h2>Thông tin sản phẩm</h2>
              <p>Tên SP: {transaction.product.name}</p>
              <p>SKU: {transaction.product.sku}</p>
              <p>Giá nhập: {transaction.product.importPrice?.toFixed(2)}</p>
              <p>Tồn kho: {transaction.product.stockQuantity}</p>
              <p>Mô tả: {transaction.product.description}</p>

              {transaction.product.imgUrl && (
                <img
                  src={ApiService.resolveAssetUrl(transaction.product.imgUrl)}
                  alt={transaction.product.name}
                />
              )}
            </div>
            {console.log(transaction.product.imgUrl)}
            {/* User information who made the transaction */}
            <div className="section-card">
              <h2>Thông tin người tạo</h2>
              <p>Tên: {transaction.user.name}</p>
              <p>Email: {transaction.user.email}</p>
              <p>Số điện thoại: {transaction.user.phoneNumber}</p>
              <p>Quyền: {transaction.user.role}</p>
              <p>
                Thời gian tạo: {new Date(transaction.createAt).toLocaleString()}
              </p>
            </div>

            {/* Supplier information who made the transaction */}
            {transaction.suppliers && (
              <div className="section-card">
                <h2>Thông tin nhà cung cấp</h2>
                <p>Tên: {transaction.supplier.name}</p>
                <p>Liên hệ: {transaction.supplier.contactInfo}</p>
                <p>Địa chỉ: {transaction.supplier.address}</p>
              </div>
            )}

            {/* UPDATE TRANSACTION STATUS */}
            {transaction.transactionStatus !== "CANCELLED" ? (
              <div className="section-card transaction-staus-update">
                <label>Trạng thái: </label>
                <select
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                >
                  <option value="PROCESSING">Đang xử lý</option>
                  <option value="COMPLETED">Hoàn thành</option>
                  <option value="CANCELLED">Đã hủy</option>
                </select>
                <button onClick={() => handleUpdateStatus()}>
                  Cập nhật trạng thái
                </button>
                {status === "CANCELLED" && transaction.transactionStatus !== "CANCELLED" && (
                  <p style={{ color: "red", marginTop: "10px", fontSize: "14px" }}>
                    ⚠️ Lưu ý: Việc Hủy phiếu này sẽ tự động hoàn trả/trừ lại số lượng vào Tồn kho hiện tại.
                  </p>
                )}
              </div>
            ) : (
              <div className="section-card" style={{ backgroundColor: "#ffebee", border: "1px solid #ffcdd2" }}>
                <h3 style={{ color: "#c62828", margin: 0 }}>Phiếu Giao Dịch Đã Hủy</h3>
                <p style={{ color: "#d32f2f", marginTop: "5px" }}>Không thể thay đổi trạng thái hoặc thao tác trên phiếu đã hủy.</p>
              </div>
            )}
          </>
        )}
      </div>
    </Layout>
  );
};

export default ChiTietChungTuComponent;
