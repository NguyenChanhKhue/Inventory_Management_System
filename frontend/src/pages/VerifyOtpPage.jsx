import React, { useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import ApiService from "../service/ApiService";

const VerifyOtpPage = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const email = searchParams.get("email") || "";

  const [otp, setOtp] = useState("");
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

    if (!email) {
      showError("Email is missing. Please request a new OTP.");
      return;
    }

    try {
      setLoading(true);
      const res = await ApiService.verifyPasswordResetOtp({ email, otp });
      showMessage(res.message);

      if (res.otpVerified && res.resetToken) {
        setTimeout(() => {
          navigate(`/reset-password?resetToken=${encodeURIComponent(res.resetToken)}`);
        }, 800);
      }
    } catch (error) {
      showError(
        error.response?.data?.message || "Unable to verify OTP right now.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-container auth-card">
        <h2>Verify OTP</h2>
        <p className="auth-subtitle">
          Enter the 6-digit code we sent to <strong>{email || "your email"}</strong>.
        </p>

        {message && <p className="message">{message}</p>}
        {errorMessage && <p className="error-message">{errorMessage}</p>}

        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Enter OTP"
            value={otp}
            onChange={(e) => setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? "Verifying..." : "Verify OTP"}
          </button>
        </form>

        <p className="auth-helper-text">
          Need a new code? <Link to="/forgot-password">Send OTP again</Link>
        </p>
      </div>
    </div>
  );
};

export default VerifyOtpPage;
