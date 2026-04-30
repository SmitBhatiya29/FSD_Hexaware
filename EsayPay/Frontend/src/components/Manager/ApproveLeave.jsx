import { useState, useEffect } from "react";
import axios from "axios";

function ApproveLeave() {
  const [leaveReq, setLeaveReq] = useState([]);
  const [errMsg, setErrMsg] = useState(undefined);
  const [totalRecords, setTotalRecords] = useState(0);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  
  const pageSize = 5;

  const getLeaveReqApi = "http://localhost:8080/api/manager/get-leave-req";
  const updateLeaveStatusApi = "http://localhost:8080/api/manager/update-leave-status";

  const getLeaveReq = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await axios.get(getLeaveReqApi, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
          page: currentPage,
          size: pageSize
        }
      });

      setLeaveReq(response.data.data);
      setTotalRecords(response.data.totalRecords);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setErrMsg(err.message + " Somthing Is Wrong Manger ......!");
      console.log(err.message);
    }
  };

  useEffect(() => {
    getLeaveReq();
  }, [currentPage]);

  const handleStatusUpdate = async (leaveId, newStatus, noOfDays) => {
    const token = localStorage.getItem("token");
    
    try {
      await axios.put(
        updateLeaveStatusApi,
        {
          leaveReqId: leaveId,
          status: newStatus,
          noOdDays: noOfDays 
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert(`Leave request ${newStatus.toLowerCase()} successfully!`);
      getLeaveReq(); 
    } catch (err) {
      console.error("Error updating leave status:", err);
      alert("Failed to update leave status.");
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePageClick = (pageIndex) => {
    setCurrentPage(pageIndex);
  };

  return (
    <div className="container-flud  px-4 pb-4">
      <div className="row mb-2 bg-light">
        <h3>Approve Leave Request</h3>
        <p className="text-muted">Review and action your team's requests</p>
      </div>

      <div className="card border-0 shadow-sm">
        <div className="card-header bg-white pt-3 pb-3 border-0">
          <div className="row">
            <h4>Leave Request ({totalRecords})</h4>
            {errMsg && <div className="alert alert-danger mt-2">{errMsg}</div>}
          </div>
        </div>
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th className="ps-4">Employee</th>
                  <th>Type</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>No of Days</th>
                  <th>Reason</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {leaveReq.map((l, index) => (
                  <tr key={index}>
                    <td className="ps-4">{l.employeeName}</td>
                    <td>{l.leaveType}</td>
                    <td>{l.fromDate}</td>
                    <td>{l.toDate}</td>
                    <td>{l.numofDays}</td>
                    <td>{l.details}</td>
                    <td>
                      <span className="badge bg-warning text-dark">{l.status}</span>
                    </td>
                    <td>
                      {l.status?.toUpperCase() === "PENDING" ? (
                        <div>
                          <button 
                            className="btn btn-primary btn-sm me-2" 
                            onClick={() => handleStatusUpdate(l.id || l.leaveReqId, "APPROVED", l.numofDays)}
                          >
                            Approve
                          </button>
                          <button 
                            className="btn btn-danger btn-sm"
                            onClick={() => handleStatusUpdate(l.id || l.leaveReqId, "REJECTED", l.numofDays)}
                          >
                            Reject
                          </button>
                        </div>
                      ) : (
                        <span className="text-muted fw-bold">-</span>
                      )}
                    </td>
                  </tr>
                ))}
                {leaveReq.length === 0 && !errMsg && (
                  <tr>
                    <td colSpan="8" className="text-center py-4 text-muted">
                      No leave requests found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
        {totalPages > 0 && (
          <div className="card-footer bg-white border-0 py-3">
            <ul className="pagination justify-content-end mb-0">
              <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
                <button
                  className="page-link"
                  onClick={handlePrevPage}
                  disabled={currentPage === 0}
                >
                  Previous
                </button>
              </li>

              {[...Array(totalPages)].map((_, index) => (
                <li
                  key={index}
                  className={`page-item ${currentPage === index ? "active" : ""}`}
                >
                  <button
                    className="page-link"
                    onClick={() => handlePageClick(index)}
                  >
                    {index + 1}
                  </button>
                </li>
              ))}

              <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
                <button
                  className="page-link"
                  onClick={handleNextPage}
                  disabled={currentPage === totalPages - 1}
                >
                  Next
                </button>
              </li>
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}

export default ApproveLeave;