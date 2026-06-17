import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ApiService from "../service/ApiService";

const logout = () => {
  ApiService.logout();
};

const Sidebar = () => {
  const isAuth = ApiService.isAuthenticated();
  const isAdmin = ApiService.isAdmin();
  const [lowStockCount, setLowStockCount] = useState(0);

  useEffect(() => {
    if (isAuth) {
      ApiService.getLowStockProducts()
        .then(res => {
          if (res.status === 200 && res.products) {
            setLowStockCount(res.products.length);
          }
        })
        .catch(err => console.error("Error fetching low stock", err));
    }
  }, [isAuth]);

  return (
    <div className="sidebar">
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", paddingRight: "20px" }}>
        <h1 className="ims">IMS</h1>
        {isAuth && (
          <div style={{ position: "relative", cursor: "pointer" }} title="Cảnh báo sắp hết hàng">
            <span style={{ fontSize: "24px" }}>🔔</span>
            {lowStockCount > 0 && (
              <span style={{
                position: "absolute",
                top: "-5px",
                right: "-10px",
                background: "red",
                color: "white",
                borderRadius: "50%",
                padding: "2px 6px",
                fontSize: "12px",
                fontWeight: "bold"
              }}>
                {lowStockCount}
              </span>
            )}
          </div>
        )}
      </div>
      <ul className="nav-links">
        {isAuth && (
          <li className="menu-group-title" style={{ fontWeight: "bold", color: "#888", fontSize: "12px", textTransform: "uppercase", padding: "10px 15px 5px", listStyle: "none", marginTop: "10px" }}>
            📊 Tổng quan
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/dashboard">Bảng báo cáo</Link>
          </li>
        )}

        {isAuth && (
          <li className="menu-group-title" style={{ fontWeight: "bold", color: "#888", fontSize: "12px", textTransform: "uppercase", padding: "10px 15px 5px", listStyle: "none", marginTop: "10px" }}>
            📦 Nghiệp vụ Kho
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/purchase">Nhập kho</Link>
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/xuat-kho">Xuất kho</Link>
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/return">Hoàn trả</Link>
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/lich-su-chung-tu">Lịch sử giao dịch</Link>
          </li>
        )}

        {isAuth && (
          <li className="menu-group-title" style={{ fontWeight: "bold", color: "#888", fontSize: "12px", textTransform: "uppercase", padding: "10px 15px 5px", listStyle: "none", marginTop: "10px" }}>
            📂 Dữ liệu Hệ thống
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/inventory">Tồn kho</Link>
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/warehouse-map">Sơ đồ kho</Link>
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/product">Sản phẩm</Link>
          </li>
        )}
        {isAdmin && (
          <li>
            <Link to="/category">Danh mục</Link>
          </li>
        )}
        {isAdmin && (
          <li>
            <Link to="/supplier">Nhà cung cấp</Link>
          </li>
        )}

        {isAdmin && (
          <li className="menu-group-title" style={{ fontWeight: "bold", color: "#888", fontSize: "12px", textTransform: "uppercase", padding: "10px 15px 5px", listStyle: "none", marginTop: "10px" }}>
            ⚙️ Quản trị viên
          </li>
        )}
        {isAdmin && (
          <li>
            <Link to="/users">Quản lý Người dùng</Link>
          </li>
        )}
        {isAdmin && (
          <li>
            <Link to="/audit-logs">Nhật ký Hệ thống</Link>
          </li>
        )}

        {isAuth && (
          <li className="menu-group-title" style={{ fontWeight: "bold", color: "#888", fontSize: "12px", textTransform: "uppercase", padding: "10px 15px 5px", listStyle: "none", marginTop: "10px" }}>
            👤 Tài khoản
          </li>
        )}
        {isAuth && (
          <li>
            <Link to="/profile">Hồ sơ cá nhân</Link>
          </li>
        )}
        {isAuth && (
          <li>
            <Link onClick={logout} to="/login" style={{ color: "red" }}>
              Đăng xuất
            </Link>
          </li>
        )}
      </ul>
    </div>
  );
};

export default Sidebar;
