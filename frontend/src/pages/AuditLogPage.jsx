import React, { useState, useEffect } from "react";
import ApiService from "../service/ApiService";
import Layout from "../components/Layout";

const AuditLogPage = () => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = async () => {
    setLoading(true);
    setError("");
    try {
      const response = await ApiService.getAllAuditLogs();
      if (response.status === 200) {
        setLogs(response.auditLogs || []);
      }
    } catch (error) {
      console.error(error);
      setError(
        error.response?.data?.message || "Lỗi khi lấy nhật ký hệ thống"
      );
    } finally {
      setLoading(false);
    }
  };

  const getActionBadgeClass = (action) => {
    switch (action) {
      case "CREATE": return "badge badge-create";
      case "UPDATE": return "badge badge-update";
      case "DELETE": return "badge badge-delete";
      default: return "badge badge-default";
    }
  };

  const getEntityBadgeClass = (entity) => {
    switch (entity) {
      case "Product": return "badge badge-product";
      case "Supplier": return "badge badge-supplier";
      case "Category": return "badge badge-category";
      case "Transaction": return "badge badge-transaction";
      default: return "badge badge-default";
    }
  };

  const translateAction = (action) => {
    switch (action) {
      case "CREATE": return "Tạo mới";
      case "UPDATE": return "Cập nhật";
      case "DELETE": return "Xóa";
      default: return action;
    }
  };

  const translateEntity = (entity) => {
    switch (entity) {
      case "Product": return "Sản phẩm";
      case "Supplier": return "Nhà cung cấp";
      case "Category": return "Danh mục";
      case "Transaction": return "Giao dịch";
      default: return entity;
    }
  };

  return (
    <Layout>
      <div className="audit-log-page">
        {error && <div className="error-message">{error}</div>}
        <div className="audit-log-header">
        <div>
          <h1>
            Nhật ký Truy vết Hệ thống
          </h1>
          <p>
            Ghi nhận toàn bộ thao tác thêm, sửa, xóa dữ liệu trên hệ thống
          </p>
        </div>
        <button onClick={fetchLogs} className="refresh-btn">
          Làm mới
        </button>
      </div>

      <div className="audit-log-container">
        <div className="table-responsive">
          <table className="audit-log-table">
            <thead>
              <tr>
                <th>Thời gian</th>
                <th>Người thực hiện</th>
                <th>Hành động</th>
                <th>Đối tượng</th>
                <th>Chi tiết</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="5" className="text-center">
                    Đang tải dữ liệu...
                  </td>
                </tr>
              ) : logs.length > 0 ? (
                logs.map((log) => (
                  <tr key={log.id}>
                    <td>{log.createdAt}</td>
                    <td>{log.userName}</td>
                    <td>
                      <span className={getActionBadgeClass(log.action)}>
                        {translateAction(log.action)}
                      </span>
                    </td>
                    <td>
                      <span className={getEntityBadgeClass(log.entityName)}>
                        {translateEntity(log.entityName)} (ID: {log.entityId})
                      </span>
                    </td>
                    <td className="log-details" title={log.details}>
                      {log.details}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center">
                    Chưa có nhật ký hoạt động nào.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
    </Layout>
  );
};

export default AuditLogPage;
