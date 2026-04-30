import axios from "axios";
import { useState, useEffect } from "react";

function LeaveRequest() {
  const getLeaveReqestAPI =
    "http://localhost:8080/api/employee/leave-request/v1";
  const getLeaveTypesAPI = "http://localhost:8080/api/leave-request/type";
  const submitLeaveReqApi = "http://localhost:8080/api/leave-request/add";
  const deleteLeaveReqApi = "http://localhost:8080/api/leave-request/delete";

  const [refreshTrigger, setRefreshTrigger] = useState(true);

  // UI States
  const [newLeaveReqBtn, setnewLeaveReqBtn] = useState(false);
  const [leaveRequest, setLeaveRequest] = useState([]);
  const [leaveTypesList, setLeaveTypesList] = useState([]);
  const [errMsg, setErrMsg] = useState("");

  //  Pagination States
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 5;

  const [filterStatus, setFilterStatus] = useState("");
  const [filterLeaveType, setFilterLeaveType] = useState("");

  // Form States
  const [selectedLeaveType, setSelectedLeaveType] = useState("");
  const [toDate, setToDate] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [details, setDetails] = useState("");

  useEffect(() => {
    const getAllLeaveRequest = async () => {
      try {
        const token = localStorage.getItem("token");
        const queryParams = {
          page: currentPage,
          size: pageSize,
          ...(filterStatus && { status: filterStatus }),
          ...(filterLeaveType && { leaveType: filterLeaveType }),
        };

        const res = await axios.get(getLeaveReqestAPI, {
          headers: { Authorization: `Bearer ${token}` },
          params: queryParams,
        });

        setLeaveRequest(res.data.data);
        setTotalPages(res.data.totalPages || 0);
      } catch (err) {
        setErrMsg(err.message);
        console.error(err.message);
      }
    };

    const getLeaveType = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(getLeaveTypesAPI, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setLeaveTypesList(res.data);

        if (res.data && res.data.length > 0) {
          setSelectedLeaveType(res.data[0].value);
        }
      } catch (err) {
        setErrMsg(err.message);
        console.error(err.message);
      }
    };

    getAllLeaveRequest();
    getLeaveType();
  }, [refreshTrigger, currentPage, filterStatus, filterLeaveType]);

  const submitLeaveReq = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");

      const res = await axios.post(
        submitLeaveReqApi,
        {
          leaveType: selectedLeaveType,
          fromDate: fromDate,
          toDate: toDate,
          details: details,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      console.log("Leave Submitted Successfully:", res.data);
      alert("Leave Request Submitted!");

      setnewLeaveReqBtn(false);
      setFromDate("");
      setToDate("");
      setDetails("");

      setCurrentPage(0);
      setFilterStatus("");
      setFilterLeaveType("");
      setRefreshTrigger(!refreshTrigger);
    } catch (err) {
      setErrMsg(err.message);
      console.error(err.message);
    }
  };

  const handleDelete = async (id) => {
   
    if (!window.confirm("Are you sure you want to delete this leave request?")) {
      return;
    }

    try {
      const token = localStorage.getItem("token");
      await axios.delete(`${deleteLeaveReqApi}/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      alert("Leave Request Deleted Successfully!");
      
      setRefreshTrigger(!refreshTrigger);
    } catch (err) {
     
      const errorMessage = err.response?.data?.message || err.message;
      setErrMsg(`Failed to delete: ${errorMessage}`);
      console.error(err);
    }
  };

  // Pagination Handlers
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

  const handleStatusChange = (e) => {
    setFilterStatus(e.target.value);
    setCurrentPage(0);
  };

  const handleLeaveTypeChange = (e) => {
    setFilterLeaveType(e.target.value);
    setCurrentPage(0);
  };

  return (
    <div>
      <div className="m-2">
        <div className="row ">
          <h3>Leave Requests</h3>
          <p className="text-muted">Submit and track your leave applications</p>
        </div>

        {errMsg && <div className="alert alert-danger">{errMsg}</div>}

        <div className="mb-2">
          <button
            className="btn btn-primary"
            onClick={() => setnewLeaveReqBtn(true)}
          >
            + New Request
          </button>
        </div>

        {newLeaveReqBtn && (
          <div className="card border-info mb-2">
            <div className="card-header">
              <span className="card-title">New Leave Request</span>
            </div>
            <div className="card-body">
              <form className="form" onSubmit={submitLeaveReq}>
                <label className="form-label">Leave Type :</label>
                <select
                  className="form-select mb-2"
                  value={selectedLeaveType}
                  onChange={(e) => setSelectedLeaveType(e.target.value)}
                  required
                >
                  {leaveTypesList.map((l, index) => (
                    <option key={index} value={l.value}>
                      {l.value}
                    </option>
                  ))}
                </select>

                <label className="form-label">Start Date :</label>
                <input
                  type="date"
                  className="form-control mb-2"
                  value={fromDate}
                  onChange={(e) => setFromDate(e.target.value)}
                  required
                />

                <label className="form-label">End Date :</label>
                <input
                  type="date"
                  className="form-control mb-2"
                  value={toDate}
                  onChange={(e) => setToDate(e.target.value)}
                  required
                />

                <label className="form-label">Reason :</label>
                <input
                  type="text"
                  className="form-control mb-3"
                  value={details}
                  onChange={(e) => setDetails(e.target.value)}
                  required
                />

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-primary">
                    Submit Leave Request
                  </button>
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setnewLeaveReqBtn(false)}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="card ">
          <div className="card-header">
            <h4>My Leave History ({leaveRequest.length})</h4>

            <div className="d-flex gap-2">
              <select
                className="form-select form-select-sm"
                value={filterLeaveType}
                onChange={handleLeaveTypeChange}
              >
                <option value="">All Leave Types</option>
                <option value="SICK_LEAVE">Sick Leave</option>
                <option value="CASUAL_LEAVE">Casual Leave</option>
                <option value="EARNED_LEAVE">Earned Leave</option>
                <option value="LEAVE_WITHOUT_PAY">Leave Without Pay</option>
              </select>

              <select
                className="form-select form-select-sm"
                value={filterStatus}
                onChange={handleStatusChange}
              >
                <option value="">All Statuses</option>
                <option value="PENDING">Pending</option>
                <option value="APPROVED">Approved</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>
          </div>
          <div className="card-body">
            <table className="table">
              <thead>
                <tr>
                  <th>Leave Type</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>No of Days</th>
                  <th>Reason</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {leaveRequest.length > 0 ? (
                  leaveRequest.map((leaveReq, index) => (
                 
                    <tr key={leaveReq.id || index}>
                      <td>{leaveReq.leaveType}</td>
                      <td>{leaveReq.fromDate}</td>
                      <td>{leaveReq.toDate}</td>
                      <td>{leaveReq.numofDays}</td>
                      <td>{leaveReq.details}</td>
                      <td>
                        <span
                          className={`badge ${
                            leaveReq.status === "APPROVED"
                              ? "bg-success"
                              : leaveReq.status === "REJECTED"
                                ? "bg-danger"
                                : "bg-warning text-dark"
                          } mt-1`}
                        >
                          {leaveReq.status}
                        </span>
                      </td>
                      <td>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDelete(leaveReq.id)}
                          disabled = {leaveReq.status == "APPROVED"}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" className="text-center text-muted">
                      No leave requests found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
            {totalPages > 0 && (
              <div className="d-flex justify-content-between align-items-center mt-3">
                <button
                  className="btn btn-outline-secondary btn-sm"
                  onClick={handlePrevPage}
                  disabled={currentPage === 0}
                >
                  Previous
                </button>
                <span>
                  Page {currentPage + 1} of {totalPages}
                </span>
                <button
                  className="btn btn-outline-secondary btn-sm"
                  onClick={handleNextPage}
                  disabled={currentPage === totalPages - 1}
                >
                  Next
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default LeaveRequest;
