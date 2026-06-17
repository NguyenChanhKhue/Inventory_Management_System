import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const AddEditSupplierPage = () => {
  const { supplierId } = useParams("");
  const [name, setName] = useState("");
  const [contactInfo, setContactInfo] = useState("");
  const [address, setAddress] = useState("");
  const [message, setMessage] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    if (supplierId) {
      setIsEditing(true);

      const fetchSupplier = async () => {
        try {
          const supplierData = await ApiService.getSupplierById(supplierId);
          if (supplierData.status === 200) {
            setName(supplierData.supplier.name);
            setContactInfo(supplierData.supplier.contactInfo);
            setAddress(supplierData.supplier.address);
          }
        } catch (error) {
          showMessage(
            error.response?.data?.message ||
              "Lỗi khi lấy thông tin nhà cung cấp: " + error,
          );
        }
      };
      fetchSupplier();
    }
  }, [supplierId]);

  //handle form submission for both add and edit supplier
  const handleSubmit = async (e) => {
    e.preventDefault();
    const supplierData = { name, contactInfo, address, active: true };

    try {
      if (isEditing) {
        await ApiService.updateSupplier(supplierId, supplierData);
        showMessage("Cập nhật nhà cung cấp thành công");
        navigate("/supplier");
      } else {
        await ApiService.addSupplier(supplierData);
        showMessage("Thêm nhà cung cấp thành công");
        navigate("/supplier");
      }
    } catch (error) {
      showMessage(
        error.response?.data?.message ||
          "Lỗi khi lưu nhà cung cấp: " + error,
      );
    }
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
      <div className="supplier-form-page">
        <h1>{isEditing ? "Sửa nhà cung cấp" : "Thêm nhà cung cấp"}</h1>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tên nhà cung cấp</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              type="text"
            />
          </div>

          <div className="form-group">
            <label>Thông tin liên hệ</label>
            <input
              value={contactInfo}
              onChange={(e) => setContactInfo(e.target.value)}
              required
              type="text"
            />
          </div>

          <div className="form-group">
            <label>Địa chỉ</label>
            <input
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              required
              type="text"
            />
          </div>
          <button type="submit">
            {isEditing ? "Lưu thay đổi" : "Thêm nhà cung cấp"}
          </button>
        </form>
      </div>
    </Layout>
  );
};
export default AddEditSupplierPage;
