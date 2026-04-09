import React, { useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import ApiService from "../service/ApiService";

const ResetPasswordPage = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const resetToken = searchParams.get("resetToken") || "";

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const showError = (msg) => {
    setErrorMessage(msg);
    setTimeout(() => {
      setErrorMessage("");
    }, 5000);
  };

  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 5000);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!resetToken) {
      showError("Reset session is missing");
      return;
    }

    if (newPassword !== confirmPassword) {
      showError("Confirm password does not match");
      return;
    }

    try {
      setLoading(true);
      const res = await ApiService.resetPassword({
        resetToken,
        newPassword,
        confirmPassword,
      });

      showMessage(res.message);
      setNewPassword("");
      setConfirmPassword("");

      setTimeout(() => {
        navigate("/login");
      }, 1500);
    } catch (error) {
      showError(
        error.response?.data?.message || "Unable to reset password right now.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-container auth-card">
        <h2>Reset Password</h2>
        <p className="auth-subtitle">
          Your OTP has been verified. Create a new password for your account.
        </p>

        {message && <p className="message">{message}</p>}
        {errorMessage && <p className="error-message">{errorMessage}</p>}

        <form onSubmit={handleSubmit}>
          <input
            type="password"
            placeholder="New Password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? "Updating..." : "Reset Password"}
          </button>
        </form>

        <p className="auth-helper-text">
          Want to sign in instead? <Link to="/login">Back to login</Link>
        </p>
      </div>
    </div>
  );
};

export default ResetPasswordPage;
