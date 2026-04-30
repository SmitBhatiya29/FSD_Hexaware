import axios from "axios";
import EmployeeTopNavbar from "./EmployeeTopNavbar";

import { useState, useEffect } from "react";
function TimeSheet() {
  const [newTimeSheetBtn, setnewTimeSheetBtn] = useState(false);

  const [date, setDate] = useState(undefined);
  const [hoursWorked, setHoursWorked] = useState(undefined);
  const [project, setProject] = useState(undefined);
  const [description, setDescription] = useState(undefined);

  const [timeSheet, setTimeSheet] = useState([]);
  const [refreshTrigger, setRefreshTrigger] = useState(true);

  const [errMsg, setErrMsg] = useState("");

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [statusFilter, setStatusFilter] = useState(""); 
  const pageSize = 5;

  const getTimeSheetApi = "http://localhost:8080/api/employee/time-sheet";
  useEffect(() => {
    const getTimeSheet = async () => {
      try {
        let api = `http://localhost:8080/api/employee/time-sheet/v1?page=${currentPage}&size=${pageSize}`;
        if (statusFilter) {
          api += `&status=${statusFilter}`;
        }

        const token = localStorage.getItem("token");
        const res = await axios.get(api, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTimeSheet(res.data.data);
      } catch (err) {
        setErrMsg(err.message);
        console.error(err.message);
      }
    };
    getTimeSheet();
  }, [refreshTrigger,currentPage, statusFilter]);
  const submitTimeSheet = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };

      await axios.post(
        "http://localhost:8080/api/timesheets/submit",
        {
          workDate: date,
          hoursWorked: hoursWorked,
          project: project,
          Description: description,
        },
        config,
      );

      alert("Timesheet submitted successfully!");

      setnewTimeSheetBtn(false);
      setDate("");
      setHoursWorked("");
      setProject("");
      setDescription("");

      setCurrentPage(0);

      setRefreshTrigger(!refreshTrigger);
    } catch (err) {
      console.log(err);
      alert("Error submitting timesheet!");
    }
  };

  const handleFilterChange = (e) => {
    setStatusFilter(e.target.value);
    setCurrentPage(0); 
  };

  return (
    <div>
      <div className="m-2 ">
        <div className="row">
          <h2>Daily Timesheet</h2>
          <p>Log and track your Monthly work hours</p>
        </div>

        <div className="mb-2">
          <button
            className="btn btn-primary"
            onClick={() => setnewTimeSheetBtn(true)}
          >
            + Log Hours
          </button>
        </div>

        {newTimeSheetBtn && (
          <div className="card  border-info mb-2">
            <div className="card-header">
              <span className="card-title">Log Time Entry</span>
            </div>
            <div className="card-body ">
              <form className="form " onSubmit={submitTimeSheet}>
                <label className="form-lable">Work Month End Date :</label>
                <input
                  type="date"
                  className="form-control mb-2"
                  onChange={(e) => setDate(e.target.value)}
                />

                <label className="form-lable">hoursWorked :</label>
                <input
                  type="number"
                  className="form-control mb-3"
                  onChange={(e) => setHoursWorked(e.target.value)}
                />

                <label className="form-lable">Project :</label>
                <input
                  type="text"
                  className="form-control mb-3"
                  onChange={(e) => setProject(e.target.value)}
                />

                <label className="form-lable">Description :</label>
                <input
                  type="text"
                  className="form-control mb-3"
                  onChange={(e) => setDescription(e.target.value)}
                />

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-primary ">
                    Submit TimeSheet
                  </button>
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setnewTimeSheetBtn(false)}
                  >
                    {" "}
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="card">
          <div className="p-2 text-center">
            <h3>Timesheet History</h3>
            <div>
              <select 
                className="form-select form-select-sm" 
                value={statusFilter} 
                onChange={handleFilterChange}
              >
                <option value="">All Statuses</option>
                <option value="PENDING">Pending</option>
                <option value="APPROVED">Approved</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>
          </div>
          <div className="card-header">
            <table className="table table-hover">
              <thead className="table-light">
                <tr>
                  <th>Date</th>
                  <th>Project</th>
                  <th>Description</th>
                  <th>Hours</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {timeSheet.map((t, index) => (
                  <tr key={index}>
                    <td>{t.workDate}</td>

                    <td>{t.project}</td>
                    <td>{t.Description}</td>
                    <td>{t.hoursWorked}</td>
                    <td>
                      <span
                        className={`badge ${
                          t.status === "APPROVED"
                            ? "bg-success"
                            : t.status === "REJECTED"
                              ? "bg-danger"
                              : "bg-warning text-dark"
                        }`}
                      >
                        {t.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="card-footer d-flex justify-content-between align-items-center">
            <button 
              className="btn btn-sm btn-outline-primary"
              disabled={currentPage === 0}
              onClick={() => setCurrentPage(prev => prev - 1)}
            >
              Previous
            </button>
            <span>
              Page <strong>{currentPage + 1}</strong> of <strong>{totalPages === 0 ? 1 : totalPages}</strong>
            </span>
            <button 
              className="btn btn-sm btn-outline-primary"
              disabled={currentPage >= totalPages - 1 || totalPages === 0}
              onClick={() => setCurrentPage(prev => prev + 1)}
            >
              Next
            </button>
          </div>
        
        </div>
      </div>
    </div>
  );
}
export default TimeSheet;
