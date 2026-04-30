import React, { useState, useEffect } from "react";
import axios from "axios";

function PayrollMasterTable({ selectedMonth, setSelectedMonth }) {
  const [payrolls, setPayrolls] = useState([]);
  const [loading, setLoading] = useState(false);

  const formatMonthForBackend = (yyyy_mm) => {
    if (!yyyy_mm) return "";
    const date = new Date(yyyy_mm + "-01");
    return date.toLocaleString("en-US", { month: "short", year: "numeric" }).replace(" ", "-");
  };

  const fetchAllPayrolls = async () => {
    setLoading(true);
    const token = localStorage.getItem("token");
    const formattedMonth = formatMonthForBackend(selectedMonth);

    try {
      const response = await axios.get(`http://localhost:8080/api/payroll/all`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { month: formattedMonth },
      });
      setPayrolls(response.data);
    } catch (err) {
      console.error("Failed to fetch all payrolls", err);
      setPayrolls([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (selectedMonth) {
      fetchAllPayrolls();
    }
  }, [selectedMonth]);

  const handleMarkAsPaid = async (payrollId) => {
    const isConfirmed = window.confirm("Are you sure you want to mark this as PAID? Bank transfer should be completed before doing this.");
    if (!isConfirmed) return;

    const token = localStorage.getItem("token");
    try {
      await axios.put(`http://localhost:8080/api/payroll/pay/${payrollId}`, {}, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert("Status updated to PAID!");
      fetchAllPayrolls(); 
    } catch (err) {
      console.error(err);
      alert("Error marking as paid: " + (err.response?.data?.message || err.message));
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "DRAFT":
        return <span className="badge bg-warning text-dark px-3 py-2 rounded-pill">Draft</span>;
      case "PROCESSED":
        return <span className="badge bg-info text-dark px-3 py-2 rounded-pill">Processed</span>;
      case "PAID":
        return <span className="badge bg-success px-3 py-2 rounded-pill">Paid</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  return (
    <div className="card shadow-sm border-0 rounded-4">
     
      <div className="card-header bg-dark text-white pt-3 pb-3 border-0 d-flex flex-wrap justify-content-between align-items-center rounded-top-4 gap-3">
        
        <div className="d-flex align-items-center gap-3">
          <h5 className="fw-bold mb-0">📊 Master Payroll Roster</h5>
          
       
          <input 
            type="month" 
            className="form-control form-control-sm bg-dark text-white border-secondary shadow-none" 
            style={{ width: "140px", colorScheme: "dark" }} 
            value={selectedMonth}
            onChange={(e) => setSelectedMonth(e.target.value)}
          />
        </div>

        <button className="btn btn-sm btn-outline-light" onClick={fetchAllPayrolls} disabled={loading}>
          {loading ? "Refreshing..." : "↻ Refresh Table"}
        </button>
      </div>

      <div className="card-body p-0">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light text-secondary" style={{ fontSize: "0.85rem" }}>
              <tr>
                <th className="ps-4 py-3">EMP ID</th>
                <th>EMPLOYEE NAME</th>
                <th>TOTAL EARNINGS</th>
                <th>TOTAL DEDUCTIONS</th>
                <th className="text-success fw-bold">NET PAYABLE</th>
                <th>STATUS</th>
                <th className="text-end pe-4">ACTION</th>
              </tr>
            </thead>
            <tbody>
              {payrolls.length > 0 ? (
                payrolls.map((record) => (
                  <tr key={record.id}>
                    <td className="ps-4 fw-medium text-dark">#{record.employeeId}</td>
                    <td className="fw-semibold">{record.employeeName}</td>
                    <td className="text-muted">₹{record.totalEarnings}</td>
                    <td className="text-danger">₹{record.totalDeductions}</td>
                    <td className="fw-bold text-success fs-6">₹{record.netPayable}</td>
                    <td>{getStatusBadge(record.status)}</td>
                    <td className="text-end pe-4">
                      {record.status === "PROCESSED" ? (
                        <button
                          className="btn btn-sm btn-primary fw-semibold px-3 rounded-pill shadow-sm"
                          onClick={() => handleMarkAsPaid(record.id)}
                        >
                          Mark as Paid
                        </button>
                      ) : record.status === "PAID" ? (
                        <span className="text-success small fw-bold">✔️ Completed</span>
                      ) : (
                        <span className="text-muted small italic">Pending Process</span>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="text-center py-5 text-muted">
                    <div className="fs-1 mb-2">📭</div>
                    <h6 className="fw-semibold text-dark">No Payroll Records Found</h6>
                    <p className="small mb-0">No records have been generated for {formatMonthForBackend(selectedMonth)} yet.</p>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default PayrollMasterTable;