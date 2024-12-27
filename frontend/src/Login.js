import React, { useState } from "react";
import "./Login.css";
import "boxicons/css/boxicons.min.css";
import { useNavigate } from "react-router-dom";

const Login = ({ setUserId }) => {
  const navigate = useNavigate();
  const [active, setActive] = useState(false);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [plateNumber, setPlateNumber] = useState("");
  const [loginError, setLoginError] = useState(false);
  const [signupError, setSignupError] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isAdmin, setIsAdminState] = useState(true);

  const toggleActive = () => {
    setActive(!active);
    setLoginError(false);
    setSignupError(false);
  };

  const handleCheckboxChange = () => {
    setIsAdminState(!isAdmin);
  };

  const validateUser = async (email, password) => {
    const url = `http://localhost:8080/api/auth/Login/${email}/${password}`;

    try {
      setIsLoading(true);
      const response = await fetch(url, {
        method: "GET",
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const userId = await response.text();
      setIsLoading(false);
      return userId || null;
    } catch (error) {
      setIsLoading(false);
      console.error("Error validating user:", error.message);
      return null;
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    const userId = await validateUser(email, password);

    if (userId) {
      setLoginError(false);
      setUserId(userId);
      if (isAdmin) {
        navigate(`/home`);
      } else {
        navigate(`/admin`);
      }
    } else {
      setLoginError(true);
    }
  };
  const handleSignup = async (e) => {
    e.preventDefault();
    const url = `http://localhost:8080/api/auth/signup`;

    try {
      setIsLoading(true);
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, email, password, plateNumber }),
      });

      if (!response.ok) {
        throw new Error("Failed to sign up. Please try again.");
      }

      const message = await response.text();
      setIsLoading(false);

      alert(message); // Inform the user about the email verification
      toggleActive();
    } catch (error) {
      setIsLoading(false);
      console.error("Error signing up:", error.message);
      setSignupError(true);
    }
  };

  return (
    <div className="component-wrapper">
      <div className="container">
        <div className={`wrapper ${active ? "active" : ""}`}>
          <span className="bg-animate"></span>
          <span className="bg-animate2"></span>

          {isLoading && (
            <div className="loading-overlay">
              <div className="loading-spinner"></div>
              <p>Loading...</p>
            </div>
          )}

          <div className="form-box login">
            <h2 className="animation" style={{ "--i": 0, "--j": 21 }}>
              Login
            </h2>
            <form onSubmit={handleLogin}>
              <div
                className="input-box animation"
                style={{ "--i": 1, "--j": 22 }}
              >
                <input
                  type="text"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <label>Email</label>
                <i className="bx bxs-envelope"></i>
              </div>
              <div
                className="input-box animation"
                style={{ "--i": 2, "--j": 23 }}
              >
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <label>Password</label>
                <i className="bx bxs-lock-alt"></i>
              </div>

              <label
                className="checkbox-label animation"
                style={{ "--i": 3, "--j": 24 }}
              >
                <input
                  type="checkbox"
                  checked={!isAdmin}
                  onChange={handleCheckboxChange}
                  className="checkbox-input"
                />
                <span className="checkbox-text">Sign me in as Admin</span>
              </label>

              <button className="btn animation" style={{ "--i": 3, "--j": 24 }}>
                Login
              </button>

              {loginError && (
                <p
                  className="error-message"
                  style={{ fontSize: "14px", color: "red", margin: "20px" }}
                >
                  Incorrect username or password.
                </p>
              )}

              <div
                className="logreg-link animation"
                style={{ "--i": 4, "--j": 25 }}
              >
                <p>
                  Don't have an account?{" "}
                  <a
                    className="register-link"
                    onClick={toggleActive}
                    style={{ cursor: "pointer" }}
                  >
                    Sign Up
                  </a>
                </p>
              </div>
            </form>
          </div>

          <div className="info-text login">
            <h2 className="animation" style={{ "--i": 0, "--j": 19 }}>
              Welcome Back!
            </h2>
            <p className="animation" style={{ "--i": 1, "--j": 20 }}>
              Hope, You and your Family have a Great Day
            </p>
          </div>

          <div className="form-box register">
            <h2 className="animation" style={{ "--i": 17, "--j": 0 }}>
              Sign Up
            </h2>
            <form onSubmit={handleSignup}>
              <div
                className="input-box animation"
                style={{ "--i": 18, "--j": 1 }}
              >
                <input
                  type="text"
                  required
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
                <label>Username</label>
                <i className="bx bxs-user"></i>
              </div>
              <div
                className="input-box animation"
                style={{ "--i": 19, "--j": 2 }}
              >
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <label>Email</label>
                <i className="bx bxs-envelope"></i>
              </div>
              <div
                className="input-box animation"
                style={{ "--i": 20, "--j": 3 }}
              >
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <label>Password</label>
                <i className="bx bxs-lock-alt"></i>
              </div>
              <div
                className="input-box animation"
                style={{ "--i": 20, "--j": 4 }}
              >
                <input
                  type="text"
                  required
                  value={plateNumber}
                  onChange={(e) => setPlateNumber(e.target.value)}
                />
                <label>Plate Number</label>
                <i class="bi bi-car-front"></i>
              </div>
              <button className="btn animation" style={{ "--i": 21, "--j": 5 }}>
                Register
              </button>

              {signupError && (
                <p
                  className="error-message"
                  style={{
                    fontSize: "14px",
                    color: "red",
                    margin: "15px",
                    marginLeft: "20%",
                  }}
                >
                  Failed to save. Try again.
                </p>
              )}

              <div
                className="logreg-link animation"
                style={{ "--i": 22, "--j": 5 }}
              >
                <p>
                  Already have an account?{" "}
                  <a
                    className="login-link"
                    onClick={toggleActive}
                    style={{ cursor: "pointer" }}
                  >
                    Login
                  </a>
                </p>
              </div>
            </form>
          </div>

          <div className="info-text register">
            <h2 className="animation" style={{ "--i": 17, "--j": 0 }}>
              Welcome!
            </h2>
            <p className="animation" style={{ "--i": 18, "--j": 1 }}>
              Hope, You and your Family have a Great Day
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
