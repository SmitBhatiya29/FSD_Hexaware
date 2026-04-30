import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Profile() {
  const navigate = useNavigate();
  const [profileData, setProfileData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/employee/profile-details",
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          },
        );
        console.log("API Response Data:", res.data);
        setProfileData(res.data);
      } catch (error) {
        console.error("Error fetching profile:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, []);

  if (loading) {
    return (
      <div
        className="d-flex justify-content-center align-items-center"
        style={{ height: "80vh" }}
      >
        <div
          className="spinner-border text-primary"
          role="status"
          style={{ width: "3rem", height: "3rem" }}
        >
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (!profileData) {
    return (
      <div className="text-center mt-5">
        <h4 className="text-danger">
          Error loading profile data. Please try again.
        </h4>
        <button className="btn btn-primary mt-3" onClick={() => navigate(-1)}>
          Go Back
        </button>
      </div>
    );
  }

  return (
    <div className="container-fluid  bg-light min-vh-100">
      <div className="row justify-content-center">
        <div className="col-12 col-md-10 col-lg-9 col-xl-8">
          <div className="card border-0 shadow-sm rounded-4 overflow-hidden">
            <div className="card-header bg-primary text-white py-3 px-4 d-flex align-items-center justify-content-between">
              <h5 className="mb-0 fw-bold d-flex align-items-center gap-2">
                <i className="bi bi-person-badge"></i> My Profile
              </h5>
              <button
                className="btn btn-sm btn-light fw-bold px-3 shadow-sm"
                onClick={() => navigate(-1)}
              >
                <i className="bi bi-arrow-left me-1"></i> Back
              </button>
            </div>

            <div className="card-body p-0">
              <div className="bg-white text-center pt-5 pb-4 border-bottom">
                <div
                  className="rounded-circle bg-primary d-flex align-items-center justify-content-center mx-auto text-white fw-bold shadow-sm"
                  style={{ width: "90px", height: "90px", fontSize: "2.5rem" }}
                >
                  {profileData.firstName
                    ? profileData.firstName.charAt(0).toUpperCase()
                    : "U"}
                </div>
                <h3 className="mt-3 mb-1 fw-bold text-dark">
                  {profileData.firstName} {profileData.lastName}
                </h3>
                <span className="badge bg-primary text-light px-3 py-2 mt-1 rounded-pill fw-semibold">
                  {profileData.designation || "Employee"}
                </span>
              </div>

              <div className="p-4 bg-light">
                <h6
                  className="text-primary fw-bold mb-3 text-uppercase"
                  style={{ letterSpacing: "1px", fontSize: "0.85rem" }}
                >
                  Employee Details
                </h6>

                <div className="row g-3">
                  <div className="col-12 col-md-6">
                    <div className="p-3 bg-white border rounded-3 shadow-sm h-100 d-flex flex-column">
                      <span className="text-muted small fw-bold mb-1">
                        Employee ID
                      </span>
                      <span
                        className="text-dark fw-semibold"
                        style={{ fontSize: "1.1rem" }}
                      >
                        EP00{profileData.Empid}
                      </span>
                    </div>
                  </div>

                  <div className="col-12 col-md-6">
                    <div className="p-3 bg-white border rounded-3 shadow-sm h-100 d-flex flex-column">
                      <span className="text-muted small fw-bold mb-1">
                        Email Address
                      </span>
                      <span
                        className="text-dark fw-semibold"
                        style={{ fontSize: "1.1rem" }}
                      >
                        {profileData.UserEmail || "N/A"}
                      </span>
                    </div>
                  </div>

                  <div className="col-12 col-md-6">
                    <div className="p-3 bg-white border rounded-3 shadow-sm h-100 d-flex flex-column">
                      <span className="text-muted small fw-bold mb-1">
                        Department
                      </span>
                      <span
                        className="text-dark fw-semibold"
                        style={{ fontSize: "1.1rem" }}
                      >
                        {profileData.department || "N/A"}
                      </span>
                    </div>
                  </div>

                  <div className="col-12 col-md-6">
                    <div className="p-3 bg-white border rounded-3 shadow-sm h-100 d-flex flex-column">
                      <span className="text-muted small fw-bold mb-1">
                        System Role
                      </span>
                      <span
                        className="text-dark fw-semibold"
                        style={{ fontSize: "1.1rem" }}
                      >
                        {profileData.role || "N/A"}
                      </span>
                    </div>
                  </div>

                  <div className="col-12 col-md-6">
                    <div className="p-3 bg-white border rounded-3 shadow-sm h-100 d-flex flex-column">
                      <span className="text-muted small fw-bold mb-1">
                        Reporting Manager
                      </span>
                      <span
                        className="text-dark fw-semibold"
                        style={{ fontSize: "1.1rem" }}
                      >
                        {profileData.ManagerFirstName
                          ? `${profileData.ManagerFirstName} ${profileData.ManagerLastName}`
                          : "Not Assigned"}
                      </span>
                    </div>
                  </div>

                  <div className="col-12 col-md-6">
                    <div className="p-3 bg-white border rounded-3 shadow-sm h-100 d-flex justify-content-between align-items-center">
                      <div>
                        <span className="text-muted small fw-bold mb-1 d-block">
                          Account Status
                        </span>
                        <span
                          className="text-dark fw-semibold"
                          style={{ fontSize: "1.1rem" }}
                        >
                          Current Status
                        </span>
                      </div>
                      <span
                        className={`badge ${profileData.status?.toUpperCase() === "ACTIVE" ? "bg-success" : "bg-danger"} px-3 py-2 rounded-pill`}
                      >
                        {profileData.status || "N/A"}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
