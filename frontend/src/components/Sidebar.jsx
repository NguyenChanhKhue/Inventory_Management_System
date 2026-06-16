import React from "react";
import { Link } from "react-router-dom";
import ApiService from "../service/ApiService";

const logout = () => {
  ApiService.logout();
};

const Sidebar = () => {
  const isAuth = ApiService.isAuthenticated();
  const isAdmin = ApiService.isAdmin();

  return (
    <div className="sidebar">
      <h1 className="ims">IMS</h1>
      <ul className="nav-links">
        {isAuth && (
          <li>
            <Link to="/dashboard">Bảng điều khiển</Link>
          </li>
        )}

        {isAuth && (
          <li>
            <Link to="/transaction">Giao dịch</Link>
          </li>
        )}

        {isAdmin && (
          <li>
            <Link to="/category">Danh mục</Link>
          </li>
        )}

        {isAdmin && (
          <li>
            <Link to="/product">Sản phẩm</Link>
          </li>
        )}

        {isAdmin && (
          <li>
            <Link to="/supplier">Nhà cung cấp</Link>
          </li>
        )}

        {isAuth && (
          <li>
            <Link to="/purchase">Nhập hàng</Link>
          </li>
        )}

        {isAuth && (
          <li>
            <Link to="/sell">Xuất bán</Link>
          </li>
        )}

        {isAuth && (
          <li>
            <Link to="/return">Hoàn trả</Link>
          </li>
        )}

        {isAuth && (
          <li>
            <Link to="/profile">Hồ sơ cá nhân</Link>
          </li>
        )}

        {isAuth && (
          <li>
            <Link onClick={logout} to="/login">
              Đăng xuất
            </Link>
          </li>
        )}
      </ul>
    </div>
  );
};

export default Sidebar;
