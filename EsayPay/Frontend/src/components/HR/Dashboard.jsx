import { useState, useEffect } from "react";

import axios from "axios";
import { data } from "react-router-dom";

function StatCard({ label, value, icon }) {
  return (
    <div className="card shadow-sm border-0 mb-3">
      <div className="card-body d-flex align-items-center">
        <div className="bg-primary bg-opacity-10 p-3 rounded me-3 fs-3">
          {icon}
        </div>
        <div>
          <h3 className="fs-4 fw-bold mb-0">{value}</h3>
          <p className="text-muted mb-0">{label}</p>
        </div>
      </div>
    </div>
  );
}

function Dashboard() {
  const PendingformApi = "http://localhost:8080/api/hr/pending-profiles";
  const DepartmentOverviewApi =
    "http://localhost:8080/api/hr/department-overview";

  const [pendingUsers, setPendingUsers] = useState([]);
  const [countOfPendingApprovals, setCountOfPendingApprovals] = useState(0);
  const [totalEmployees, setTotalEmployees] = useState(0);
  const [estimatedPayStub,setEstimatedPayStub] = useState(0)

  const [departmentOverview, setDepartmentOverview] = useState([]);
  const [countOfDepartment, setCountOfDepartment] = useState(0);

  const [errMsg, setErrMsg] = useState(undefined);
  const [userProfile, setUserProfile] = useState(null);
  const now = new Date();

  useEffect(() => {
    //token
    try {
      const token = localStorage.getItem("token");

      const AuthConfig = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
      const storedProfile = localStorage.getItem("userProfile");
      if (storedProfile) {
        setUserProfile(JSON.parse(storedProfile));
      }
    } catch (err) {
      setErrMsg(err.message);
      console.log(errMsg);
    }

    const getAllPendingForm = async () => {
      try {
        const token = localStorage.getItem("token");
        const AuthConfig = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        const response = await axios.get(PendingformApi, AuthConfig);

        setPendingUsers(response.data.data);
        setCountOfPendingApprovals(response.data.totalRecords);
      } catch (err) {
        setErrMsg(err.message);
        console.log(errMsg);
      }
    };
    getAllPendingForm();

    const getDepartmentOverview = async () => {
      try {
        const token = localStorage.getItem("token");
        const AuthConfig = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get(DepartmentOverviewApi, AuthConfig);
        setDepartmentOverview(response.data.data);
        setCountOfDepartment(response.data.data.length);
        setTotalEmployees(response.data.totalEmployee);
        setEstimatedPayStub(response.data.totalEstimatedPay)
      } catch (err) {
        setErrMsg(err.message);
        console.log(errMsg);
      }
    };
    getDepartmentOverview();
  }, []);

  const dashboardStats = [
    { id: 1, label: "Total Employees", value: totalEmployees, icon: "👥" },
    {
      id: 2,
      label: "Pending Approvals",
      value: countOfPendingApprovals,
      icon: "⏳",
    },
    { id: 3, label: "Departments", value: countOfDepartment, icon: "🏢" },
    { id: 4, label: "Total Payroll Cost", value: estimatedPayStub, icon: "💸" },
  ];

  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
     

      {/* Main Dashboard Content */}
      <div className="container-fluid px-4 pb-4">
        <div className="mb-4">
          <h2>Good morning, {userProfile?.firstName || "HR"}! 👋</h2>
          <p className="text-secondary">
            {now.toLocaleDateString("en-US", {
              weekday: "long",
              year: "numeric",
              month: "long",
              day: "numeric",
            })}
          </p>
        </div>

        {/* Stats Row */}
        <div className="row mb-4">
          {dashboardStats.map((stat) => (
            <div className="col-md-3" key={stat.id}>
              <StatCard
                label={stat.label}
                value={stat.value}
                icon={stat.icon}
              />
            </div>
          ))}
        </div>

        <div className="row">
          {/* Table 1: Pending Approvals */}
          <div className="col-md-6 mb-4">
            <div className="card shadow-sm border-0 h-100">
              <div className="card-header bg-white pt-3 pb-2 border-0">
                <h5 className="card-title mb-0">Pending Approvals</h5>
              </div>
              <hr className="m-0" />
              <div className="card-body">
                <table className="table table-hover align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {pendingUsers.map((user, index) => (
                      <tr key={index}>
                        <td>
                          {user.firstName} {user.lastName}
                        </td>
                        <td>{user.userEmail}</td>
                        <td>{user.role}</td>
                        <td>
                          <span className="badge bg-warning text-dark">
                            PENDING
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Table 2: Departments */}
          <div className="col-md-6 mb-4">
            <div className="card shadow-sm border-0 h-100">
              <div className="card-header bg-white pt-3 pb-2 border-0">
                <h5 className="card-title mb-0">Department Overview</h5>
              </div>
              <hr className="m-0" />
              <div className="card-body">
                <table className="table table-borderless table-hover align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>Department Name</th>
                      <th>Total Employees</th>
                    </tr>
                  </thead>
                  <tbody>
                    {departmentOverview.map((dept, index) => (
                      <tr key={index}>
                        <td>{dept.departmentName}</td>
                        <td className="fw-bold ">{dept.totalEmployees}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
