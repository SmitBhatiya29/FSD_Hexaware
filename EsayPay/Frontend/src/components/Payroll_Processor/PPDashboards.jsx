import React, { useState, useEffect } from "react";
import PayrollMasterTable from "./PayrollMasterTable";
import axios from "axios";

function PPDashboard() {
  const [userProfile, setUserProfile] = useState(null);
  
  
  const currentMonth = new Date().toISOString().slice(0, 7);
  const [selectedMonth, setSelectedMonth] = useState(currentMonth);

 
  const [stats, setStats] = useState({
    pendingBenefitRequests: 0,
    totalBenefitPaid: 0,
    totalEstimatedPay: 0,
  });

  useEffect(() => {
    const storedProfile = localStorage.getItem("userProfile");
    if (storedProfile) {
      setUserProfile(JSON.parse(storedProfile));
    }
  }, []);


  useEffect(() => {
    fetchDashboardStats();
  }, [selectedMonth]);

  const fetchDashboardStats = async () => {
    try {

      const [year, month] = selectedMonth.split("-");
      const date = new Date(year, parseInt(month) - 1);
      const formattedMonthApi = `${date.toLocaleString("en-US", { month: "short" })}-${year}`;

      
      const token = localStorage.getItem("token"); 
      const response = await axios.get(`http://localhost:8080/api/payroll/stats?month=${formattedMonthApi}`, {
        headers: {
          "Authorization": `Bearer ${token}`,  
        }
      });

      
        setStats(response.data);
      
    } catch (error) {
      console.error("Error fetching dashboard stats:", error);
    }
  };

  
  const currentDate = new Date().toLocaleDateString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

  const hour = new Date().getHours();
  const greeting = hour < 12 ? "Good morning" : hour < 18 ? "Good afternoon" : "Good evening";

  return (
    <div className="container-fluid px-4 py-3 bg-light" style={{ minHeight: "100vh" }}>
      {/* Header Section */}
      <div className="row mb-4 align-items-center">
        <div className="col-12">
          <h2 className="fw-bold text-dark mb-1">
            {greeting}, {userProfile ? userProfile.firstName : "Admin"}! 👋
          </h2>
          <p className="text-muted mb-0">{currentDate}</p>
        </div>
      </div>

      
      <div className="row mb-4 g-3">
        
        {/* Card 1: Active Payroll Cycle */}
        <div className="col-12 col-md-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body d-flex align-items-center">
              <div
                className="bg-primary bg-opacity-10 rounded d-flex align-items-center justify-content-center me-3 text-primary"
                style={{ width: "60px", height: "60px", fontSize: "24px" }}
              >
                📅
              </div>
              <div>
                <h4 className="fw-bold mb-0">
                  {new Date(selectedMonth + "-01").toLocaleString("default", { month: "long", year: "numeric" })}
                </h4>
                <div className="text-dark fw-semibold" style={{ fontSize: "0.9rem" }}>Active Payroll Cycle</div>
                <small className="text-muted">Selected month</small>
              </div>
            </div>
          </div>
        </div>

        {/* Card 2: Pending Benefit Requests */}
        <div className="col-12 col-md-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body d-flex align-items-center">
              <div
                className="bg-warning bg-opacity-10 rounded d-flex align-items-center justify-content-center me-3"
                style={{ width: "60px", height: "60px", fontSize: "24px" }}
              >
                ⏳
              </div>
              <div>
                <h4 className="fw-bold mb-0">{stats.pendingBenefitRequests}</h4>
                <div className="text-dark fw-semibold" style={{ fontSize: "0.9rem" }}>Pending Benefits</div>
                <small className="text-muted">Requires action</small>
              </div>
            </div>
          </div>
        </div>

        {/* Card 3: Total Benefit Amount Paid */}
        <div className="col-12 col-md-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body d-flex align-items-center">
              <div
                className="bg-info bg-opacity-10 rounded d-flex align-items-center justify-content-center me-3"
                style={{ width: "60px", height: "60px", fontSize: "24px" }}
              >
                🎁
              </div>
              <div>
                <h4 className="fw-bold mb-0">₹{stats.totalBenefitPaid.toLocaleString()}</h4>
                <div className="text-dark fw-semibold" style={{ fontSize: "0.9rem" }}>Benefits Paid</div>
                <small className="text-muted">Overall approved</small>
              </div>
            </div>
          </div>
        </div>

        {/* Card 4: Total Estimated Pay */}
        <div className="col-12 col-md-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body d-flex align-items-center">
              <div
                className="bg-success bg-opacity-10 rounded d-flex align-items-center justify-content-center me-3"
                style={{ width: "60px", height: "60px", fontSize: "24px" }}
              >
                💸
              </div>
              <div>
                <h4 className="fw-bold mb-0">₹{stats.totalEstimatedPay.toLocaleString()}</h4>
                <div className="text-dark fw-semibold" style={{ fontSize: "0.9rem" }}>Total Payroll Cost</div>
                <small className="text-muted">For {new Date(selectedMonth + "-01").toLocaleString("default", { month: "short" })}</small>
              </div>
            </div>
          </div>
        </div>

      </div>

      {/* Main Content Area: Master Table */}
      <div className="row">
        <div className="col-12">
   
          <PayrollMasterTable
            selectedMonth={selectedMonth}
            setSelectedMonth={setSelectedMonth}
          />
        </div>
      </div>
    </div>
  );
}

export default PPDashboard;