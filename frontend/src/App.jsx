import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import { ProtectedRoute, AdminRoute } from "./service/Guard.jsx";
import LoginPage from "./pages/Loginpage.jsx";
import ForgotPasswordPage from "./pages/ForgotPasswordPage.jsx";
import VerifyOtpPage from "./pages/VerifyOtpPage.jsx";
import ResetPasswordPage from "./pages/ResetPasswordPage.jsx";
import CategoryPage from "./pages/CategoryPage.jsx";
import SupplierPage from "./pages/SupplierPage.jsx";
import AddEditSupplierPage from "./pages/AddEditingSupplierPage.jsx";
import ProductPage from "./pages/ProductPage.jsx";
import AddEditProductPage from "./pages/AddEditProductPage.jsx";
import PurchasePage from "./pages/PurchasePage.jsx";
import ReturnPage from "./pages/ReturnPage.jsx";
import XuatKhoComponent from "./pages/XuatKhoPage.jsx";
import LichSuChungTuComponent from "./pages/LichSuChungTuPage.jsx";
import ChiTietChungTuComponent from "./pages/ChiTietChungTuPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";
import DashboardPage from "./pages/DashboardPage.jsx";
import InventoryPage from "./pages/InventoryPage.jsx";
import WarehouseMapPage from "./pages/WarehouseMapPage.jsx";
import AuditLogPage from "./pages/AuditLogPage.jsx";
import UserManagementPage from "./pages/UserManagementPage.jsx";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/verify-otp" element={<VerifyOtpPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />

        {/* ADMIN ROUTE - Dữ liệu Hệ thống & Quản trị viên */}
        <Route path="/category" element={<AdminRoute element={<CategoryPage />} />} />
        <Route path="/supplier" element={<AdminRoute element={<SupplierPage />} />} />
        <Route path="/add-supplier" element={<AdminRoute element={<AddEditSupplierPage />} />} />
        <Route path="/edit-supplier/:supplierId" element={<AdminRoute element={<AddEditSupplierPage />} />} />
        <Route path="/add-product" element={<AdminRoute element={<AddEditProductPage />} />} />
        <Route path="/edit-product/:productId" element={<AdminRoute element={<AddEditProductPage />} />} />
        <Route path="/audit-logs" element={<AdminRoute element={<AuditLogPage />} />} />
        <Route path="/users" element={<AdminRoute element={<UserManagementPage />} />} />

        {/* ADMIN AND MANAGERS ROUTES - Tổng quan & Nghiệp vụ Kho */}
        <Route path="/dashboard" element={<ProtectedRoute element={<DashboardPage />} />} />
        <Route path="/profile" element={<ProtectedRoute element={<ProfilePage />} />} />
        
        <Route path="/purchase" element={<ProtectedRoute element={<PurchasePage />} />} />
        <Route path="/xuat-kho" element={<ProtectedRoute element={<XuatKhoComponent />} />} />
        <Route path="/return" element={<ProtectedRoute element={<ReturnPage />} />} />
        <Route path="/lich-su-chung-tu" element={<ProtectedRoute element={<LichSuChungTuComponent />} />} />
        <Route path="/lich-su-chung-tu/:transactionId" element={<ProtectedRoute element={<ChiTietChungTuComponent />} />} />
        
        <Route path="/inventory" element={<ProtectedRoute element={<InventoryPage />} />} />
        <Route path="/product" element={<ProtectedRoute element={<ProductPage />} />} />
        <Route path="/warehouse-map" element={<ProtectedRoute element={<WarehouseMapPage />} />} />

        <Route path="*" element={<LoginPage />} />
      </Routes>
    </Router>
  );
}

export default App;
