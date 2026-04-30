import React, { useState, useEffect } from "react";
import axios from "axios";

function AssignBenefit() {
  const [benefits, setBenefits] = useState([]);
  const [totalRecords, setTotalRecords] = useState(0);
  const [errMsg, setErrMsg] = useState(undefined);

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 5;

  const getAllBenefits = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await axios.get("http://localhost:8080/api/employee-benefits/get-all-benefitReq", {
        headers: {
          Authorization: `Bearer ${token}`, 
        },
        params: {
          page: currentPage,
          size: pageSize
        },
      });

      setBenefits(response.data.data);
      setTotalRecords(response.data.totalRecords);
      setTotalPages(response.data.totalPages);
      setErrMsg(undefined);
    } catch (err) {
      setErrMsg(err.message + " - Something went wrong while fetching benefits!");
      console.log(err);
    }
  };

  useEffect(() => {
    getAllBenefits();
  }, [currentPage]);

  const updateBenefitStatus = async (benefitId, status, amount = null) => {
    const token = localStorage.getItem("token");
    try {
      await axios.put(
        "http://localhost:8080/api/employee-benefits/assign",
        {
          benefitId: benefitId,
          benefitAmount: amount, 
          status: status        
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      alert(`Request ${benefitId} successfully marked as ${status}!`);
      getAllBenefits(); 
    } catch (err) {
      console.error(err);
      alert("Error updating request status. Please try again.");
    }
  };

  const handleApprove = (id) => {
    const amountInput = window.prompt("Please enter the approved benefit amount:");
    if (amountInput !== null && amountInput.trim() !== "") {
      const amount = parseFloat(amountInput);
      if (isNaN(amount) || amount <= 0) {
        alert("Please enter a valid positive number for the amount.");
        return;
      }
      updateBenefitStatus(id, "APPROVED", amount);
    }
  };

  const handleReject = (id) => {
    const isConfirmed = window.confirm("Are you sure you want to reject this benefit request?");
    if (isConfirmed) {
      updateBenefitStatus(id, "REJECTED", 0);
    }
  };

 
  const handleRevoke = async (id) => {
    const isConfirmed = window.confirm("Are you sure you want to revoke this benefit? It will be deleted permanently from this employee.");
    if (isConfirmed) {
      const token = localStorage.getItem("token");
      try {
        await axios.delete(`http://localhost:8080/api/employee-benefits/revoke/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        alert(`Benefit request ${id} has been revoked successfully.`);
        getAllBenefits(); 
      } catch (err) {
        console.error(err);
        alert("Error revoking the benefit. Please try again.");
      }
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 0) setCurrentPage(currentPage - 1);
  };

  const handleNextPage = () => {
    if (currentPage < totalPages - 1) setCurrentPage(currentPage + 1);
  };

  const handlePageClick = (pageIndex) => {
    setCurrentPage(pageIndex);
  };

  const getStatusBadge = (status) => {
    if (status === 'PENDING') return "badge bg-warning bg-opacity-10 text-warning border border-warning border-opacity-25 px-3 py-1 rounded-pill";
    if (status === 'APPROVED') return "badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 px-3 py-1 rounded-pill";
    return "badge bg-danger bg-opacity-10 text-danger border border-danger border-opacity-25 px-3 py-1 rounded-pill";
  };

  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <div className="container-fluid px-4 pb-4 pt-4">
        
        <div className="row mb-3">
          <div>
            <h2 className="fw-bold">Benefit Requests</h2>
            <p className="text-secondary">
              View and manage all employee benefit requests
            </p>
          </div>
        </div>

        <div className="card shadow-sm border-0 mb-4">
          <div className="card-header bg-white pt-3 pb-3 border-0">
            <div className="row align-items-center">
              <div className="col-md-12">
                <h5 className="fw-bold mb-0">
                  All Requests ({totalRecords})
                </h5>
                {errMsg !== undefined && (
                  <div className="alert alert-danger mt-3 mb-0 py-2">{errMsg}</div>
                )}
              </div>
            </div>
          </div>

          <hr className="m-0" />

          <div className="card-body p-0">
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead className="table-light text-secondary" style={{ fontSize: "0.85rem" }}>
                  <tr>
                    <th>REQ ID</th>
                    <th className="ps-4 py-3">EMPLOYEE</th>
                    <th>BENEFIT NAME</th>
                    <th>DESCRIPTION</th>
                    <th>AMOUNT</th>
                    <th>STATUS</th>
                    <th className="text-end pe-4">ACTION</th>
                  </tr>
                </thead>
                <tbody>
                  {benefits.length > 0 ? (
                    benefits.map((benefit) => (
                      <tr key={benefit.id}>
                         <td className="text-muted">
                          <small className="bg-light border px-2 py-1 rounded fw-medium">
                            REQ-{benefit.id}
                          </small>
                        </td>
                        <td className="ps-4 py-3">
                          <div className="d-flex align-items-center">
                            <div>
                              <h6 className="mb-0 fw-semibold text-dark">
                                {benefit.firstName} {benefit.lastName}
                              </h6>
                              <small className="text-muted">
                                {benefit.department}
                              </small>
                            </div>
                          </div>
                        </td>
                       
                        <td className="text-dark fw-medium">{benefit.benefitName}</td>
                        <td className="text-muted">
                          <div style={{ maxWidth: "200px", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>
                            {benefit.description}
                          </div>
                        </td>
                        <td className="text-dark fw-semibold">₹{benefit.benefitAmount || 0}</td>
                        <td>
                          <span className={getStatusBadge(benefit.status)}>
                            {benefit.status}
                          </span>
                        </td>
                        <td className="text-end pe-4">
                          {benefit.status === 'PENDING' ? (
                            <>
                              <button 
                                className="btn btn-sm btn-outline-success rounded-pill px-3 me-2"
                                onClick={() => handleApprove(benefit.id)}
                              >
                                Approve
                              </button>
                              <button 
                                className="btn btn-sm btn-outline-danger rounded-pill px-3"
                                onClick={() => handleReject(benefit.id)}
                              >
                                Reject
                              </button>
                            </>
                          ) : (
                            
                            <button 
                                className="btn btn-sm btn-outline-secondary rounded-pill px-3"
                                onClick={() => handleRevoke(benefit.id)}
                            >
                                Revoke
                            </button>
                          )}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="7" className="text-center py-5 text-muted">
                        No benefit requests found.
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
                  <button className="page-link shadow-none" onClick={handlePrevPage} disabled={currentPage === 0}>
                    Previous
                  </button>
                </li>
                {[...Array(totalPages)].map((_, index) => (
                  <li key={index} className={`page-item ${currentPage === index ? "active" : ""}`}>
                    <button className="page-link shadow-none" onClick={() => handlePageClick(index)}>
                      {index + 1}
                    </button>
                  </li>
                ))}
                <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
                  <button className="page-link shadow-none" onClick={handleNextPage} disabled={currentPage === totalPages - 1}>
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

export default AssignBenefit;