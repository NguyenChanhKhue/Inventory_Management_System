import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [showAddForm, setShowAddForm] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phoneNumber: "",
    password: "",
    role: "MANAGER"
  });

  useEffect(() => {
    fetchUsers();
    fetchCurrentUser();
  }, []);

  const fetchCurrentUser = async () => {
    try {
      const res = await ApiService.getLoggedInUserInfo();
      setCurrentUser(res);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const response = await ApiService.getAllUser();
      if (response.status === 200) {
        setUsers(response.users || []);
      }
    } catch (err) {
      setError(err.response?.data?.message || "Lỗi khi lấy danh sách người dùng");
    } finally {
      setLoading(false);
    }
  };

  const showMsg = (msg, isError = false) => {
    if (isError) {
      setError(msg);
      setTimeout(() => setError(""), 4000);
    } else {
      setMessage(msg);
      setTimeout(() => setMessage(""), 4000);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleAddUser = async (e) => {
    e.preventDefault();
    try {
      await ApiService.registerUser(formData);
      showMsg("Tạo người dùng thành công!");
      setShowAddForm(false);
      setFormData({ name: "", email: "", phoneNumber: "", password: "", role: "MANAGER" });
      fetchUsers();
    } catch (err) {
      showMsg(err.response?.data?.message || "Lỗi khi tạo người dùng", true);
    }
  };

  const handleToggleStatus = async (user) => {
    const newStatus = !user.isActive;
    const actionText = newStatus ? "mở khóa" : "khóa";
    if (!window.confirm(`Bạn có chắc chắn muốn ${actionText} tài khoản này không?`)) return;

    if (!newStatus) {
      // Locking user (Soft delete via delete endpoint or update)
      try {
        await ApiService.deleteUser(user.id);
        showMsg("Đã khóa tài khoản thành công!");
        fetchUsers();
      } catch (err) {
        showMsg(err.response?.data?.message || "Lỗi khi khóa tài khoản", true);
      }
    } else {
      // Unlocking user (Update isActive to true)
      try {
        await ApiService.updateUser(user.id, { isActive: true });
        showMsg("Đã mở khóa tài khoản thành công!");
        fetchUsers();
      } catch (err) {
        showMsg(err.response?.data?.message || "Lỗi khi mở khóa tài khoản", true);
      }
    }
  };

  const handleRoleChange = async (userId, newRole) => {
    if (!window.confirm(`Đổi quyền tài khoản này thành ${newRole}?`)) return;
    try {
      await ApiService.updateUser(userId, { role: newRole });
      showMsg("Cập nhật quyền thành công!");
      fetchUsers();
    } catch (err) {
      showMsg(err.response?.data?.message || "Lỗi khi cập nhật quyền", true);
    }
  };

  return (
    <Layout>
      <div className="p-6 w-full" style={{ padding: '20px', width: '100%', backgroundColor: '#f3f4f6' }}>
        {message && <div style={{ backgroundColor: '#d1fae5', color: '#065f46', padding: '10px', borderRadius: '5px', marginBottom: '15px' }}>{message}</div>}
        {error && <div style={{ backgroundColor: '#fee2e2', color: '#991b1b', padding: '10px', borderRadius: '5px', marginBottom: '15px' }}>{error}</div>}
        
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <div>
            <h1 style={{ fontSize: '2rem', fontWeight: 'bold', color: '#008080' }}>Quản lý Người dùng</h1>
            <p style={{ color: '#50646a' }}>Quản lý tài khoản, phân quyền và trạng thái hoạt động</p>
          </div>
          <button 
            onClick={() => setShowAddForm(!showAddForm)}
            style={{ padding: '10px 18px', backgroundColor: '#008080', color: 'white', borderRadius: '5px', cursor: 'pointer' }}
          >
            {showAddForm ? "Hủy" : "+ Thêm Tài Khoản Mới"}
          </button>
        </div>

        {showAddForm && (
          <div style={{ backgroundColor: 'white', padding: '20px', borderRadius: '10px', marginBottom: '20px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}>
            <h2 style={{ marginBottom: '15px', color: '#2f4f4f' }}>Tạo Tài Khoản Mới</h2>
            <form onSubmit={handleAddUser} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
              <div>
                <label style={{ display: 'block', marginBottom: '5px' }}>Họ và tên</label>
                <input type="text" name="name" value={formData.name} onChange={handleInputChange} required style={{ width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }} />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '5px' }}>Email</label>
                <input type="email" name="email" value={formData.email} onChange={handleInputChange} required style={{ width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }} />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '5px' }}>Số điện thoại</label>
                <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleInputChange} required style={{ width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }} />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '5px' }}>Mật khẩu</label>
                <input type="password" name="password" value={formData.password} onChange={handleInputChange} required style={{ width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }} />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '5px' }}>Quyền hạn</label>
                <select name="role" value={formData.role} onChange={handleInputChange} style={{ width: '100%', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
                  <option value="MANAGER">MANAGER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>
              <div style={{ display: 'flex', alignItems: 'flex-end' }}>
                <button type="submit" style={{ width: '100%', padding: '10px', backgroundColor: '#008080', color: 'white', borderRadius: '5px', cursor: 'pointer' }}>Lưu Tài Khoản</button>
              </div>
            </form>
          </div>
        )}

        <div style={{ backgroundColor: 'white', borderRadius: '10px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)', overflow: 'hidden' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
            <thead>
              <tr style={{ backgroundColor: '#f8fafc', color: '#475569', textTransform: 'uppercase', fontSize: '0.85rem' }}>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>ID</th>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>Tên</th>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>Email</th>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>SĐT</th>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>Quyền</th>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>Trạng thái</th>
                <th style={{ padding: '16px', borderBottom: '1px solid #f1f5f9' }}>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr><td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>Đang tải...</td></tr>
              ) : users.length > 0 ? (
                users.map(user => (
                  <tr key={user.id} style={{ borderBottom: '1px solid #f1f5f9' }}>
                    <td style={{ padding: '16px' }}>{user.id}</td>
                    <td style={{ padding: '16px', fontWeight: 'bold' }}>{user.name}</td>
                    <td style={{ padding: '16px' }}>{user.email}</td>
                    <td style={{ padding: '16px' }}>{user.phoneNumber}</td>
                    <td style={{ padding: '16px' }}>
                      <select 
                        value={user.role} 
                        onChange={(e) => handleRoleChange(user.id, e.target.value)}
                        disabled={currentUser && currentUser.id === user.id}
                        style={{ padding: '5px', borderRadius: '5px', border: '1px solid #ddd', opacity: (currentUser && currentUser.id === user.id) ? 0.5 : 1 }}
                        title={(currentUser && currentUser.id === user.id) ? "Bạn không thể tự đổi quyền của chính mình" : ""}
                      >
                        <option value="ADMIN">ADMIN</option>
                        <option value="MANAGER">MANAGER</option>
                      </select>
                    </td>
                    <td style={{ padding: '16px' }}>
                      <span style={{ 
                        padding: '4px 10px', borderRadius: '12px', fontSize: '0.8rem', fontWeight: 'bold',
                        backgroundColor: user.isActive ? '#d1fae5' : '#fee2e2',
                        color: user.isActive ? '#065f46' : '#991b1b'
                      }}>
                        {user.isActive ? 'Hoạt động' : 'Đã khóa'}
                      </span>
                    </td>
                    <td style={{ padding: '16px' }}>
                      <button 
                        onClick={() => handleToggleStatus(user)}
                        disabled={currentUser && currentUser.id === user.id}
                        title={(currentUser && currentUser.id === user.id) ? "Bạn không thể tự khóa chính mình" : ""}
                        style={{ 
                          padding: '6px 12px', borderRadius: '5px', border: 'none', 
                          cursor: (currentUser && currentUser.id === user.id) ? 'not-allowed' : 'pointer', 
                          color: 'white',
                          backgroundColor: user.isActive ? '#dc3545' : '#008080',
                          opacity: (currentUser && currentUser.id === user.id) ? 0.5 : 1
                        }}
                      >
                        {user.isActive ? 'Khóa' : 'Mở khóa'}
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr><td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>Chưa có người dùng nào.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  );
};

export default UserManagementPage;
