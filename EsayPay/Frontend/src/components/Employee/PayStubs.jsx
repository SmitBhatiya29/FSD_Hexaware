import React, { useState, useEffect } from "react";
import axios from "axios";
import EmployeeTopNavbar from "./EmployeeTopNavbar"; 

function PayStub() {
  const [payStubs, setPayStubs] = useState([]);
  const [selectedMonth, setSelectedMonth] = useState("");
  const [loading, setLoading] = useState(false);

  
  const formatMonthForBackend = (yyyyMm) => {
    if (!yyyyMm) return "";
    const [year, month] = yyyyMm.split("-");
    const date = new Date(year, month - 1);
   
    const shortMonth = date.toLocaleString("en-US", { month: "short" });
    return `${shortMonth}-${year}`;
  };

  const fetchPayStubs = async () => {
    setLoading(true);
    try {
     
      const backendMonthFormat = formatMonthForBackend(selectedMonth);
      
      const response = await axios.get("http://localhost:8080/api/payroll/pay-stub", {
        params: { month: backendMonthFormat },
        
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}` 
        }
      });
      setPayStubs(response.data);
    } catch (error) {
      console.error("Error fetching pay stubs", error);
    } finally {
      setLoading(false);
    }
  };

  
  useEffect(() => {
    fetchPayStubs();
  }, [selectedMonth]);

  return (
    <div>
      
      
      <div className="m-4">
        <div className="p-2 mb-2 d-flex justify-content-between align-items-center">
          <div>
            <h2>My Pay Stubs</h2>
            <p className="text-muted">
              View your salary statements and download pay slips
            </p>
          </div>
          
          
          <div className="d-flex align-items-center">
            <label className="me-2 fw-bold">Filter by Month:</label>
            <input 
              type="month" 
              className="form-control" 
              value={selectedMonth}
              onChange={(e) => setSelectedMonth(e.target.value)}
            />
            <button 
              className="btn btn-outline-secondary ms-2"
              onClick={() => setSelectedMonth("")}
            >
              Clear
            </button>
          </div>
        </div>

        <div className="card shadow-sm p-3">
          <div className="text-center mb-3">
            <h3>Pay Slip History</h3>
          </div>

          {loading ? (
            <div className="text-center">Loading...</div>
          ) : (
            <table className="table table-hover table-bordered">
              <thead className="table-light">
                <tr className="table-active">
                  <th>Month</th>
                  <th>Gross Pay (Earnings)</th>
                  <th>Total Deductions</th>
                  <th>Net Pay</th>
                  <th>Processed On</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {payStubs.length > 0 ? (
                  payStubs.map((stub) => (
                    <tr key={stub.id}>
                      <td>{stub.payMonth}</td>
                      <td className="text-success">+ ₹{stub.totalEarnings}</td>
                      <td className="text-danger">- ₹{stub.totalDeductions}</td>
                      <td className="fw-bold">₹{stub.netPayable}</td>
                      <td>{stub.processedDate ? stub.processedDate : "N/A"}</td>
                      <td>
                        <span className={`badge ${stub.status === 'PAID' ? 'bg-success' : 'bg-primary'}`}>
                          {stub.status}
                        </span>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" className="text-center text-muted py-4">
                      No pay stubs found for the selected period.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}

export default PayStub;