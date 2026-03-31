import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import RegisterPage from "./pages/RegisterPage";
import { ProtectedRoute, AdminRoute } from "./service/Guard.jsx";
import LoginPage from "./pages/Loginpage.jsx";
import CategoryPage from "./pages/CategoryPage.jsx";
import SupplierPage from "./pages/SupplierPage.jsx";
import AddEditSupplierPage from "./pages/AddEditingSupplierPage.jsx";
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />

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
      </Routes>
    </Router>
  );
}

export default App;
