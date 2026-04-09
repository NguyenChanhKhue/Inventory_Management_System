import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import RegisterPage from "./pages/RegisterPage";
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
import SellPage from "./pages/SellPage.jsx";
import TransactionsPage from "./pages/TransactionPage.jsx";
import TransactionDetailsPage from "./pages/TransactionDetailPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx";
import DashboardPage from "./pages/DashboardPage.jsx";
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/verify-otp" element={<VerifyOtpPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />

        {/* ADMIN ROUTE */}
        <Route
          path="/category"
          element={<AdminRoute element={<CategoryPage />} />}
        />
        <Route
          path="/supplier"
          element={<AdminRoute element={<SupplierPage />} />}
        />
        <Route
          path="/add-supplier"
          element={<AdminRoute element={<AddEditSupplierPage />} />}
        />
        <Route
          path="/edit-supplier/:supplierId"
          element={<AdminRoute element={<AddEditSupplierPage />} />}
        />

        <Route
          path="/add-product"
          element={<AdminRoute element={<AddEditProductPage />} />}
        />
        <Route
          path="/product"
          element={<AdminRoute element={<ProductPage />} />}
        />
        <Route
          path="/edit-product/:productId"
          element={<AdminRoute element={<AddEditProductPage />} />}
        />

        {/* ADMIN AND MANAGERS ROUTES */}
        <Route
          path="/purchase"
          element={<ProtectedRoute element={<PurchasePage />} />}
        />
        <Route
          path="/sell"
          element={<ProtectedRoute element={<SellPage />} />}
        />
        <Route
          path="/return"
          element={<ProtectedRoute element={<ReturnPage />} />}
        />
        <Route
          path="/transaction"
          element={<ProtectedRoute element={<TransactionsPage />} />}
        />
        <Route
          path="/transaction/:transactionId"
          element={<ProtectedRoute element={<TransactionDetailsPage />} />}
        />
        <Route
          path="/profile"
          element={<ProtectedRoute element={<ProfilePage />} />}
        />
        <Route
          path="/dashboard"
          element={<ProtectedRoute element={<DashboardPage />} />}
        />

        <Route path="*" element={<LoginPage />} />
      </Routes>
    </Router>
  );
}

export default App;
