import {React,useState,useEffect} from "react";
import { Link, useNavigate } from "react-router-dom";

function EmployeeTopNavbar() {
  const navigate = useNavigate();
  const [userProfile, setUserProfile] = useState(null);
    
    useEffect(() => {
      const storedProfile = localStorage.getItem("userProfile");
      if (storedProfile) {
        setUserProfile(JSON.parse(storedProfile));
      }
    }, []);

  const handleLogoutBtn = () => {
    console.log("logout called");
    localStorage.removeItem("token");
     localStorage.removeItem("userProfile");
    navigate("/");
  };
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4 px-3 py-2 sticky-top">
      <div className="container-fluid">
        <Link
          className="navbar-brand d-flex flex-column gap-1 me-5 text-decoration-none"
          to="/employee"
        >
          <span className="fs-4 fw-bold text-primary">EasyPay</span>
          <span className="text-secondary" style={{ fontSize: "0.75rem" }}>
            Employee Portal
          </span>
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0 gap-2 fw-semibold">
            <li className="nav-item">
              <Link to="/employee" className="nav-link text-dark hover-primary">
                Dashboard
              </Link>
            </li>

            <li className="nav-item">
              <Link
                to="/employee/leave-request"
                className="nav-link text-dark hover-primary"
              >
                Leave Requests
              </Link>
            </li>

            <li className="nav-item">
              <Link
                to="/employee/timesheet"
                className="nav-link text-dark hover-primary"
              >
                Timesheet
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/employee/paystub"
                className="nav-link text-dark hover-primary"
              >
                Pay Stubs
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/employee/benefit-request"
                className="nav-link text-dark hover-primary"
              >
                Benefit Request
              </Link>
            </li>
            

          </ul>

          <div className="d-flex align-items-center flex-column flex-lg-row gap-3 mt-3 mt-lg-0">
            <div
              className="d-flex align-items-center border rounded px-3 py-2 bg-light"
              style={{ cursor: "pointer" }}
              onClick={() => navigate("/employee/profile")}
            >
              <div className="d-flex flex-column me-2 text-start">
                <span
                  className="fw-bold text-dark"
                  style={{ fontSize: "0.9rem", lineHeight: "1.2" }}
                >
                  {userProfile
                    ? `${userProfile.firstName} ${userProfile.lastName}`
                    : ""}
                </span>
                <span className="text-muted" style={{ fontSize: "0.75rem" }}>
                  {userProfile ? "EP00" + userProfile.Empid : ""}
                </span>
              </div>
            </div>
            <button
              className="btn btn-outline-danger fw-bold d-flex align-items-center"
              onClick={handleLogoutBtn}
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default EmployeeTopNavbar;
