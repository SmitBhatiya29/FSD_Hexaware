import { useState, useEffect } from "react";
import axios from "axios";
function ApproveTimeSheet() {
  const [leaveReq, setLeaveReq] = useState([]);
  const [errMsg, setErrMsg] = useState(undefined);

  const [currentPage, setCurrentPag] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pagesize = 5;

  const getLeaveReqApi = "http://localhost:8080/api/timesheets/pending";
  useEffect(() => {
    const token = localStorage.getItem("token");
    const getLeaveReq = async () => {
      console.log("Api called");
      try {
        const response = await axios.get(getLeaveReqApi, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            page: currentPage,
            size: pagesize,
          },
        });

        setTotalPages(response.data.totalPages);
        setLeaveReq(response.data.data);
        console.log("response is " + leaveReq);
        console.log(leaveReq[0]);
      } catch (err) {
        setErrMsg(err.message + "Somthing Is Wrong Manger ......!");
        console.log(errMsg);
      }
    };
    getLeaveReq();
  }, [currentPage]);

  const handleApprove = async (timesheetId) => {
    const token = localStorage.getItem("token");

    try {
      await axios.put(
        `http://localhost:8080/api/timesheets/status/${timesheetId}`,
        {
          requestStatus: "APPROVED",
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      setLeaveReq((preReq) => preReq.filter((req) => req.timesheetId !== timesheetId));
      alert("TimeSheet APPROVED successfully!");
    } catch (err) {
      console.error("Error approving timesheet:", err);
      alert("Failed to approve timesheet.");
    }
  };

  const handlePre = () => {
    if (currentPage > 0) {
      setCurrentPag(currentPage - 1);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPag(currentPage + 1);
    }
  };

  const handlePageClick = (pageIndex) => {
    setCurrentPag(pageIndex);
  };
  return (
    <div className="container-flud  px-4 pb-4">
      <div className="row mb-2 bg-light">
        <h3>Approve TimeSheet Request</h3>
        <p className="text-muted">Review and action your team's requests</p>
      </div>

      <div className="card">
        <div className="card-header">
          <div className="row">
            <h4>Leave Request ({leaveReq.length})</h4>
          </div>
        </div>
        <div className="card-body">
          <table className="table">
            <thead>
              <tr>
                <th>Employee</th>
                <th>Date</th>
                <th>Hours Worked</th>
                <th>Project</th>
                <th>Description</th>
                <th>Role</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {leaveReq.map((l, index) => (
                <tr key={index}>
                  <td>
                    {l.firstName} {l.lastName}
                  </td>
                  <td>{l.workDate}</td>
                  <td>{l.hoursWorked}</td>
                  <td>{l.project}</td>
                  <td>{l.Description}</td>
                  <td>{l.role}</td>
                  <td className="badge bg-warning mt-2">{l.status}</td>
                  <td>
                    <div className="">
                      <button
                        className="btn btn-primary me-3"
                        onClick={() => handleApprove(l.timesheetId)}
                        disabled = {l.status == "APPROVED"}
                      >
                        Approve
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {totalPages > 0 && (
          <div className="card-footer bg-white border-0 py-3">
            <ul className="pagination justify-content-end mb-0">
              
              <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
                <button className="page-link" onClick={handlePre} disabled={currentPage === 0}>
                  Previous
                </button>
              </li>

              {
                [...Array(totalPages)].map((_, index) => (
                  <li key={index} className={`page-item ${currentPage === index ? "active" : ""}`}>
                    <button
                      className="page-link"
                      onClick={() => handlePageClick(index)}
                    >
                      {index + 1}
                    </button>
                  </li>
                ))
              }

              <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
                <button className="page-link" onClick={handleNext} disabled={currentPage === totalPages - 1}>
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
export default ApproveTimeSheet;
