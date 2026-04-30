import { useState, useEffect } from "react";
import axios from "axios";

import StatCard from "./StatCard"

function EmployeeDashboard() {
  const [userProfile, setUserProfile] = useState(null);
  const [leaveStats, setLeaveStats] = useState([]);
  const [pendingLeave, setPendingLeave] = useState([]);
  const [countPendingBenefit, setCountPendingBenefit] = useState(0);

  useEffect(() => {
    const getUserName = () => {
      const storedProfile = localStorage.getItem("userProfile");
      if (storedProfile) {
        setUserProfile(JSON.parse(storedProfile));
      }
    };
    getUserName();

    const fetchLeaveStats = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          "http://localhost:8080/api/leave-balance/stats",
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setLeaveStats(res.data);
      } catch (err) {
        console.error("Error ", err.message);
      }
    };
    fetchLeaveStats();

   const fetchPendingLeave = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          "http://localhost:8080/api/employee/leave-request/v1", 
          { 
            headers: { Authorization: `Bearer ${token}` },
            params: { page: 0, size: 5 } 
          }
        );
        
        
        setPendingLeave(res.data.data || []); 

      } catch (err) {
        console.error("Error ", err.message);
      }
    };
    fetchPendingLeave();

    const countPendingBenefitRequest = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          "http://localhost:8080/api/employee/stats-pending-benefit",
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setCountPendingBenefit(res.data);
      } catch (err) {
        console.error("Error ", err.message);
      }
    };
    countPendingBenefitRequest();
  }, []);

  const formatLeaveType = (type) => {
    if (!type) return "";
    return type
      .split("_")
      .map((word) => word.charAt(0) + word.slice(1).toLowerCase())
      .join(" ");
  };

  const sickLeaveInfo = leaveStats.find(
    (l) => l.leaveType === "SICK_LEAVE"
  ) || { totalAllocated: 0, usedLeaves: 0 };
  const earnedLeaveInfo = leaveStats.find(
    (l) => l.leaveType === "EARNED_LEAVE"
  ) || { totalAllocated: 0, usedLeaves: 0 };

  const sickLeaveRemaining =
    sickLeaveInfo.totalAllocated - sickLeaveInfo.usedLeaves;
  const earnedLeaveRemaining =
    earnedLeaveInfo.totalAllocated - earnedLeaveInfo.usedLeaves;

  const pendingRequestsCount = pendingLeave.filter(
    (leave) => leave.status === "PENDING"
  ).length;

  return (
    <div className="bg-light min-vh-100 py-4">
      <div className="container-fluid px-4">
        {/* Header Section */}
        <div className="mb-4">
          <h3 className="fw-bold text-dark">
            Welcome Back, {userProfile?.firstName || "Employee"}! 👋
          </h3>
          <p className="text-muted">Here is an overview of your dashboard.</p>
        </div>

        {/* Top Statistics Cards */}
        <div className="row g-3 mb-4">
          <StatCard 
            value={earnedLeaveRemaining} 
            title="Earned Leave Left" 
            subtitle={`Used: ${earnedLeaveInfo.usedLeaves} / ${earnedLeaveInfo.totalAllocated}`} 
          />
          <StatCard 
            value={sickLeaveRemaining} 
            title="Sick Leave Left" 
            subtitle={`Used: ${sickLeaveInfo.usedLeaves} / ${sickLeaveInfo.totalAllocated}`} 
          />
          <StatCard 
            value={pendingRequestsCount} 
            title="Pending Leaves" 
            subtitle="Awaiting manager approval" 
          />
          <StatCard 
            value={countPendingBenefit} 
            title="Pending Benefits" 
            subtitle="Awaiting payroll approval" 
          />
        </div>

        {/* Tables Section */}
        <div className="row g-4">
          {/* Recent Leave Requests */}
          <div className="col-md-6">
            <div className="card shadow-sm border-0 rounded-3 h-100">
              <div className="card-header bg-white border-bottom pt-4 pb-3 px-4">
                <h5 className="mb-0 fw-bold text-dark">Recent Leave Requests</h5>
              </div>
              <div className="card-body p-0">
                <div className="table-responsive">
                  <table className="table table-hover align-middle mb-0">
                    <thead className="table-light">
                      <tr>
                        <th className="ps-4 text-dark">Type</th>
                        <th className="text-dark">Dates</th>
                        <th className="text-center text-dark">Days</th>
                        <th className="text-dark">Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {pendingLeave.length > 0 ? (
                        pendingLeave.map((leave, index) => (
                          <tr key={index}>
                            <td className="ps-4 fw-medium text-dark">{formatLeaveType(leave.leaveType)}</td>
                            <td className="text-muted">{leave.fromDate} to {leave.toDate}</td>
                            <td className="text-center text-dark">{leave.numofDays}</td>
                            <td>
                              <span 
                                className={`badge rounded-pill ${
                                  leave.status === 'PENDING' 
                                  ? 'bg-secondary text-white' 
                                  : 'bg-dark'
                                }`}
                              >
                                {leave.status}
                              </span>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan="4" className="text-center text-muted py-4">
                            No recent leave requests found.
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          {/* Leave Balance Overview */}
          <div className="col-md-6">
            <div className="card shadow-sm border-0 rounded-3 h-100">
              <div className="card-header bg-white border-bottom pt-4 pb-3 px-4">
                <h5 className="mb-0 fw-bold text-dark">Leave Balance Overview</h5>
              </div>
              <div className="card-body p-0">
                <div className="table-responsive">
                  <table className="table table-hover align-middle mb-0">
                    <thead className="table-light">
                      <tr>
                        <th className="ps-4 text-dark">Leave Type</th>
                        <th className="text-end pe-4 text-dark">Remaining</th>
                      </tr>
                    </thead>
                    <tbody>
                      {leaveStats.length > 0 ? (
                        leaveStats.map((leave, index) => {
                          const remaining = leave.totalAllocated - leave.usedLeaves;
                          return (
                            <tr key={index}>
                              <td className="ps-4 fw-medium text-dark">{formatLeaveType(leave.leaveType)}</td>
                              <td className="text-end pe-4">
                                <span className="fw-bold text-dark">{remaining}</span> 
                                <span className="text-muted small"> / {leave.totalAllocated}</span>
                              </td>
                            </tr>
                          );
                        })
                      ) : (
                        <tr>
                          <td colSpan="2" className="text-center text-muted py-4">
                            Loading leave balances...
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EmployeeDashboard;