import { useState, useEffect } from "react";

import axios from "axios";
import TopNavbar from "./HrTopNavbar";

function EmployeeDirectory() {
  //variables
  const [employees, setEmployees] = useState([]);
  const [totalEmployee, setTotalEmployee] = useState(0);
  const [errMsg, setErrMsg] = useState(undefined);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 5;

  const [roleFilter, setRoleFilter] = useState("");
  const [deptFilter, setDeptFilter] = useState("");

  //api
  const GetAllEmployeesApi = "http://localhost:8080/api/hr/all-employee";

  const getAllEmployee = async () => {
    const token = localStorage.getItem("token");
    const queryParams = {};
    if (roleFilter) {
      queryParams.role = roleFilter;
    }
    if (deptFilter) {
      queryParams.department = deptFilter;
    }
    queryParams.page = currentPage;
    queryParams.size = pageSize;
    try {
      const response = await axios.get(GetAllEmployeesApi, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: queryParams,
      });

      setEmployees(response.data.data);
      setTotalEmployee(response.data.totalRecords);
      setTotalPages(response.data.totalPages);
    } catch (err) {
      setErrMsg(err.message + "Somthing Is Wrong HR ......!");
      console.log(errMsg);
    }
  };

  useEffect(() => {
    getAllEmployee();
  }, [currentPage, roleFilter, deptFilter]);

  // Pagination Handler
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
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <div className="container-fluid px-4 pb-4">
        <div className="row mb-3">
          <div>
            <h2 className="fw-bold">Employee Directory</h2>
            <p className="text-secondary">
              View and manage all employees in the organization
            </p>
          </div>
        </div>

        <div className="card shadow-sm border-0 mb-4">
          <div className="card-header bg-white pt-3 pb-3 border-0">
            <div className="row align-items-center">
              <div className="col-md-8">
                <h5 className="fw-bold mb-0">
                  All Employees ({totalEmployee})
                </h5>
                {errMsg == undefined ? (
                  ""
                ) : (
                  <div className="alert alert-danger mt-4 ">{errMsg}</div>
                )}
              </div>

              <div className="col-md-4 text-md-end mt-2 mt-md-0">
                <select
                 
                  className="form-select w-auto d-inline-block me-3" 
                  value={roleFilter}
                  onChange={(e) => setRoleFilter(e.target.value)}
                >
                  <option value="">All Roles</option>
                  <option value="HR">HR</option>
                  <option value="MANAGER">Manager</option>
                  <option value="EMPLOYEE">Employee</option>
                </select>
                
                <select
                  className="form-select w-auto d-inline-block"
                  value={deptFilter}
                  onChange={(e) => setDeptFilter(e.target.value)}
                >
                  <option value="">All Departments</option>
                  <option value="Engineering">Engineering</option>
                  <option value="HR">Human Resources</option>
                  <option value="Marketing">Marketing</option>
                </select>
              </div>
            </div>
          </div>

          <hr className="m-0" />

          <div className="card-body p-0">
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead
                  className="table-light text-secondary"
                  style={{ fontSize: "0.85rem" }}
                >
                  <tr>
                    <th className="ps-4 py-3">EMPLOYEE</th>
                    <th>ID</th>
                    <th>DESIGNATION</th>
                    <th>DEPARTMENT</th>
                    <th>ROLE</th>
                    <th>JOINING DATE</th>
                    <th>STATUS</th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map((emp) => (
                    <tr key={emp.id}>
                      <td className="ps-4 py-3">
                        <div className="d-flex align-items-center">
                          <div>
                            <h6 className="mb-0 fw-semibold text-dark">
                              {emp.firstName} {emp.lastName}
                            </h6>
                            <small className="text-muted">
                              {emp.userEmail}
                            </small>
                          </div>
                        </div>
                      </td>
                      <td className="text-muted">
                        <small className="bg-light px-2 py-1 rounded">
                          EP0{emp.id}
                        </small>
                      </td>
                      <td className="text-dark">{emp.designation}</td>
                      <td className="text-dark">{emp.department}</td>
                      <td>
                        <span className="badge bg-primary bg-opacity-10 text-primary px-2 py-1">
                          {emp.role}
                        </span>
                      </td>
                      <td className="text-dark">{emp.joiningDate}</td>
                      <td>
                        <span className="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 px-3 py-1 rounded-pill">
                          {emp.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
          {totalPages > 0 && (
            <div className="card-footer ">
              <ul className="pagination justify-content-end mb-0">
                {/* Previous Button */}
                <li
                  className={`page-item ${currentPage === 0 ? "disabled" : ""}`}
                >
                  <button
                    className="page-link"
                    onClick={handlePrevPage}
                    disabled={currentPage === 0}
                  >
                    Previous
                  </button>
                </li>

                {/* Page Numbers */}
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

                {/* Next Button */}
                <li
                  className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}
                >
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
    </div>
  );
}

export default EmployeeDirectory;
