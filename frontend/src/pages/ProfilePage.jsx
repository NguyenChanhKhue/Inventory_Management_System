import React, { useEffect, useState } from "react";
import Layout from "../components/Layout";
import ApiService from "../service/ApiService";

const initialFormData = {
  name: "",
  email: "",
  phoneNumber: "",
  role: "",
};

const ProfilePage = () => {
  const [user, setUser] = useState(null);
  const [formData, setFormData] = useState(initialFormData);
  const [isEditing, setIsEditing] = useState(false);
  const [message, setMessage] = useState("");
  const isAdmin = ApiService.isAdmin();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const userInfo = await ApiService.getLoggedInUserInfo();
        setUser(userInfo);
        setFormData({
          name: userInfo.name || "",
          email: userInfo.email || "",
          phoneNumber: userInfo.phoneNumber || "",
          role: userInfo.role || "",
        });
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error getting user info: " + error,
        );
      }
    };

    fetchUserInfo();
  }, []);

  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const handleCancelEdit = () => {
    if (!user) return;

    setFormData({
      name: user.name || "",
      email: user.email || "",
      phoneNumber: user.phoneNumber || "",
      role: user.role || "",
    });
    setIsEditing(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!user) return;

    const userId = user.id || user.userId || user._id;
    const payload = {
      name: formData.name,
      email: formData.email,
      phoneNumber: formData.phoneNumber,
      ...(isAdmin ? { role: formData.role } : {}),
    };

    try {
      const response = await ApiService.updateUser(userId, payload);
      const updatedUser = response.user || { ...user, ...payload };

      setUser(updatedUser);
      setFormData({
        name: updatedUser.name || "",
        email: updatedUser.email || "",
        phoneNumber: updatedUser.phoneNumber || "",
        role: updatedUser.role || "",
      });

      if (updatedUser.role) {
        ApiService.saveRole(updatedUser.role);
      }

      setIsEditing(false);
      showMessage(response.message || "Profile updated successfully");
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error updating profile: " + error,
      );
    }
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="profile-page">
        {user && (
          <div className="profile-card">
            <div className="profile-header">
              <h1>Hello, {user.name}</h1>
              {!isEditing && (
                <button
                  className="profile-action-btn"
                  onClick={() => setIsEditing(true)}
                  type="button"
                >
                  Edit Profile
                </button>
              )}
            </div>

            {isEditing ? (
              <form className="profile-form" onSubmit={handleSubmit}>
                <div className="form-group">
                  <label>Name</label>
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Email</label>
                  <input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Phone Number</label>
                  <input
                    type="text"
                    name="phoneNumber"
                    value={formData.phoneNumber}
                    onChange={handleInputChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Role</label>
                  {isAdmin ? (
                    <select
                      name="role"
                      value={formData.role}
                      onChange={handleInputChange}
                    >
                      <option value="ADMIN">ADMIN</option>
                      <option value="MANAGER">MANAGER</option>
                    </select>
                  ) : (
                    <input type="text" value={formData.role} disabled />
                  )}
                </div>

                <div className="profile-form-actions">
                  <button type="submit" className="profile-save-btn">
                    Save Changes
                  </button>
                  <button
                    type="button"
                    className="profile-cancel-btn"
                    onClick={handleCancelEdit}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            ) : (
              <div className="profile-info">
                <div className="profile-item">
                  <label>Name</label>
                  <span>{user.name}</span>
                </div>
                <div className="profile-item">
                  <label>Email</label>
                  <span>{user.email}</span>
                </div>
                <div className="profile-item">
                  <label>Phone Number</label>
                  <span>{user.phoneNumber}</span>
                </div>
                <div className="profile-item">
                  <label>Role</label>
                  <span>{user.role}</span>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default ProfilePage;
