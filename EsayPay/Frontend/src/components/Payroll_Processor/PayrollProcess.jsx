import React, { useState, useEffect } from "react";
import axios from "axios";

function PayrollProcess() {
  
  const currentMonth = new Date().toISOString().slice(0, 7);
  
  const [selectedMonth, setSelectedMonth] = useState(currentMonth);
  const [drafts, setDrafts] = useState([]);
  const [employeeId, setEmployeeId] = useState("");
  const [loading, setLoading] = useState(false);
  const [errMsg, setErrMsg] = useState(undefined);

 
  const formatMonthForBackend = (yyyy_mm) => {
    const date = new Date(yyyy_mm + "-01");
    return date.toLocaleString('en-US', { month: 'short', year: 'numeric' }).replace(' ', '-');
  };

  // 1. Fetch Drafts
  const fetchDraftPayrolls = async () => {
    const token = localStorage.getItem("token");
    const formattedMonth = formatMonthForBackend(selectedMonth);
    
    try {
      const response = await axios.get(`http://localhost:8080/api/payroll/drafts`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { month: formattedMonth }
      });
      setDrafts(response.data);
      setErrMsg(undefined);
    } catch (err) {
      console.error(err);
      setErrMsg("Failed to fetch draft payrolls. " + (err.response?.data?.message || ""));
      setDrafts([]);
    }
  };

 
  const handleBulkGenerate = async () => {
    const isConfirmed = window.confirm(`Are you sure you want to generate drafts for ALL active employees for ${formatMonthForBackend(selectedMonth)}?`);
    if (!isConfirmed) return;

    setLoading(true);
    const token = localStorage.getItem("token");
    const formattedMonth = formatMonthForBackend(selectedMonth);

    try {
      const response = await axios.post(
        `http://localhost:8080/api/payroll/generate-bulk`,
        null,
        {
          headers: { Authorization: `Bearer ${token}` },
          params: { month: formattedMonth }
        }
      );
      
      alert(response.data); 
      fetchDraftPayrolls(); 
    } catch (err) {
      console.error(err);
      alert("Error generating bulk drafts: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDraftPayrolls();
  }, [selectedMonth]);

  // 2. Generate Draft for Employee
  const handleGenerateDraft = async (e) => {
    e.preventDefault();
    if (!employeeId) return alert("Please enter an Employee ID");
    
    setLoading(true);
    const token = localStorage.getItem("token");
    const formattedMonth = formatMonthForBackend(selectedMonth);

    try {
      await axios.post(
        `http://localhost:8080/api/payroll/generate/${employeeId}`,
        null, 
        {
          headers: { Authorization: `Bearer ${token}` },
          params: { month: formattedMonth }
        }
      );
      alert(`Draft generated successfully for Employee ID: ${employeeId}`);
      setEmployeeId(""); 
      fetchDraftPayrolls(); 
    } catch (err) {
      console.error(err);
      alert("Error generating draft: " + (err.response?.data?.message || err.response?.data || err.message));
    } finally {
      setLoading(false);
    }
  };

  // 3. Process Payment
  const handleProcessPayment = async (payrollId) => {
    const isConfirmed = window.confirm("Are you sure you want to process this payment? This action cannot be undone.");
    if (!isConfirmed) return;

    const token = localStorage.getItem("token");
    try {
      await axios.put(
        `http://localhost:8080/api/payroll/process/${payrollId}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      );
      alert("Payroll Processed Successfully! Salary has been finalized.");
      fetchDraftPayrolls(); 
    } catch (err) {
      console.error(err);
      alert("Error processing payment: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <div className="container-fluid px-4 pb-4 pt-4">
        
        {/* Header Section */}
        <div className="row mb-4 align-items-end">
          <div className="col-md-8">
            <h2 className="fw-bold text-dark mb-1">Payroll Processing</h2>
            <p className="text-secondary mb-0">Generate drafts, verify amounts, and process payments.</p>
          </div>
          <div className="col-md-4 text-md-end mt-3 mt-md-0">
            <label className="fw-semibold text-muted mb-1 d-block">Select Payroll Month</label>
            <input 
              type="month" 
              className="form-control d-inline-block w-auto shadow-sm" 
              value={selectedMonth}
              onChange={(e) => setSelectedMonth(e.target.value)}
            />
          </div>
        </div>

        {errMsg && <div className="alert alert-danger shadow-sm">{errMsg}</div>}

        <div className="row g-4">
          
          {/* Left Column: Generate Draft Form */}
          {/* <div className="col-12 col-lg-4">
            <div className="card shadow-sm border-0 rounded-4">
              <div className="card-header bg-white pt-4 pb-2 border-0">
                <h5 className="fw-bold mb-0">🛠️ Generate Draft</h5>
              </div>
              <div className="card-body">
                <p className="text-muted small mb-4">
                  Calculate Basic, HRA, Benefits, Overtime, and Deductions for the selected month.
                </p>
                <form onSubmit={handleGenerateDraft}>
                  <div className="mb-3">
                    <label className="form-label fw-semibold text-dark">Employee ID</label>
                    <input 
                      type="number" 
                      className="form-control bg-light" 
                      placeholder="e.g. 101"
                      value={employeeId}
                      onChange={(e) => setEmployeeId(e.target.value)}
                      required
                    />
                  </div>
                  <button 
                    type="submit" 
                    className="btn btn-primary w-100 fw-bold py-2"
                    disabled={loading}
                  >
                    {loading ? "Calculating..." : "Generate Payroll Draft"}
                  </button>
                </form>
              </div>
            </div>
          </div> */}
          {/* Left Column: Bulk Generate Panel */}
          <div className="col-12 col-lg-4">
            <div className="card shadow-sm border-0 rounded-4">
              <div className="card-header bg-white pt-4 pb-2 border-0">
                <h5 className="fw-bold mb-0">⚡ Bulk Generate Drafts</h5>
              </div>
              <div className="card-body text-center py-4">
                <div 
                  className="bg-primary bg-opacity-10 rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3"
                  style={{ width: "80px", height: "80px", fontSize: "32px" }}
                >
                  🏢
                </div>
                <h6 className="fw-bold text-dark">Generate for All Employees</h6>
                <p className="text-muted small mb-4">
                  Automatically calculates salary, benefits, and deductions for all <strong>APPROVED</strong> employees with a defined Salary Structure.
                </p>
                
                <button 
                  className="btn btn-primary w-100 fw-bold py-3 shadow-sm rounded-3"
                  onClick={handleBulkGenerate}
                  disabled={loading}
                >
                  {loading ? (
                    <span><span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Processing Batch...</span>
                  ) : (
                    " Generate Batch Payroll"
                  )}
                </button>
              </div>
            </div>
          </div>

          {/* Right Column: Drafts Table */}
          <div className="col-12 col-lg-8">
            <div className="card shadow-sm border-0 rounded-4 h-100">
              <div className="card-header bg-white pt-4 pb-3 border-bottom d-flex justify-content-between align-items-center">
                <h5 className="fw-bold mb-0">
                  Pending Drafts <span className="text-muted fs-6">({formatMonthForBackend(selectedMonth)})</span>
                </h5>
                <span className="badge bg-warning bg-opacity-25 text-dark border border-warning px-3 py-2 rounded-pill">
                  Needs Review
                </span>
              </div>
              
              <div className="card-body p-0">
                <div className="table-responsive">
                  <table className="table table-hover align-middle mb-0">
                    <thead className="table-light text-secondary" style={{ fontSize: "0.85rem" }}>
                      <tr >
                        <th className="ps-4 py-3">EMP ID</th>
                        <th>EMPLOYEE NAME</th>
                        <th>EARNINGS</th>
                        <th>DEDUCTIONS</th>
                        <th className="text-success">NET PAYABLE</th>
                        <th className="text-end pe-4">ACTION</th>
                      </tr>
                    </thead>
                    <tbody>
                      {drafts.length > 0 ? (
                        drafts.map((draft) => (
                          <tr key={draft.id}>
                            <td className="ps-4 fw-medium text-dark">#{draft.employeeId}</td>
                            <td className="fw-semibold">{draft.employeeName}</td>
                            <td className="text-muted">₹{draft.totalEarnings}</td>
                            <td className="text-danger">₹{draft.totalDeductions}</td>
                            <td className="fw-bold text-success fs-6">₹{draft.netPayable}</td>
                            <td className="text-end pe-4">
                              <button 
                                className="btn btn-sm btn-success fw-semibold px-3 rounded-pill shadow-sm"
                                onClick={() => handleProcessPayment(draft.id)}
                              >
                                Process Payment
                              </button>
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan="6" className="text-center py-5 text-muted">
                            <div className="fs-1 mb-2">📄</div>
                            <h6 className="fw-semibold text-dark">No drafts found</h6>
                            <p className="small mb-0">Generate a draft from the left panel to review it here.</p>
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

export default PayrollProcess;