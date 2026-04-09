import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import ApiService from "../service/ApiService";

const ForgotPasswordPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const showMessage = (msg) => {
    setMessage(msg);
    setErrorMessage("");
    setTimeout(() => {
      setMessage("");
    }, 5000);
  };

  const showError = (msg) => {
    setErrorMessage(msg);
    setMessage("");
    setTimeout(() => {
      setErrorMessage("");
    }, 5000);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const res = await ApiService.forgotPassword(email);

      if (!res.emailExists) {
        showError(res.message);
        return;
      }

      showMessage(res.message);
      setTimeout(() => {
        navigate(`/verify-otp?email=${encodeURIComponent(email)}`);
      }, 1000);
    } catch (error) {
      showError(
        error.response?.data?.message ||
          "Unable to send OTP to this email right now.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-container auth-card">
        <h2>Forgot Password</h2>
        <p className="auth-subtitle">
          Enter your account email. We will send a 6-digit OTP to verify your
          identity.
        </p>

        {message && <p className="message">{message}</p>}
        {errorMessage && <p className="error-message">{errorMessage}</p>}

        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? "Sending OTP..." : "Send OTP"}
          </button>
        </form>

        <p className="auth-helper-text">
          Remembered your password? <Link to="/login">Back to login</Link>
        </p>
      </div>
    </div>
  );
};

export default ForgotPasswordPage;
