import { useState, useEffect } from "react";
import TopNavbar from "./HrTopNavbar";
import ApprovalModal from "./HrApprovalModal";
import axios from "axios";

function PendingApprovals() {
  const [pendingRequests, setPendingRequests] = useState([]);
  const [countOfPendingApprovals, setCountOfPendingApprovals] = useState(0);
  const PendingformApi = "http://localhost:8080/api/hr/pending-profiles";
  const [errMsg, setErrMsg] = useState(undefined);

  //pagination var
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 5;

  useEffect(() => {
    const getAllPendingForm = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(PendingformApi, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            page: currentPage,
            size: pageSize,
          },
        });
        setPendingRequests(response.data.data);
        setCountOfPendingApprovals(response.data.totalRecords);

        //paging
       
        setTotalPages(response.data.totalPages);

      } catch (err) {
        setErrMsg(err.message);
        console.log(errMsg);
      }
    };
    getAllPendingForm();
  }, [currentPage]);

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

  const [showModal, setShowModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  const handleApproveClick = (user) => {
    setSelectedUser(user);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedUser(null);
  };

  const handleSuccessRefresh = (approvedEmployeeId) => {
    
    setPendingRequests(pendingRequests.filter((req) => req.employeeId !== approvedEmployeeId));
    setCountOfPendingApprovals(prevCount => prevCount - 1);
  };

  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <div className="container-fluid px-4 pb-4 pt-2">
        <div className="mb-4">
          <h2 className="fw-bold text-dark mb-1">Pending Approvals</h2>
          <p className="text-secondary">
            Review and approve new employee registration requests
          </p>
        </div>

        <div className="card shadow-sm border-0 mb-4">
          <div className="card-header bg-white pt-4 pb-3 border-0">
            <h5 className="mb-0 fw-bold">
              Pending Requests ({countOfPendingApprovals})
            </h5>
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
                    <th className="ps-4 py-3">APPLICANT</th>
                    <th>EMAIL</th>
                    <th>REQUESTED DESIGNATION</th>
                    <th>DEPARTMENT</th>
                    {/* <th>SUBMITTED</th> */}
                    <th>ACTIONS</th>
                  </tr>
                </thead>
                <tbody>
                  {pendingRequests.map((user, index) => (
                    <tr key={index}>
                      <td className="ps-4 py-3 fw-semibold text-dark">
                        {" "}
                        {user.firstName} {user.lastName}
                      </td>
                      <td className="text-muted">{user.userEmail}</td>
                      <td className="text-dark">{user.designation}</td>
                      <td className="text-dark">{user.department}</td>
                     
                      <td>
                        <button
                          className="btn btn-success btn-sm me-2 fw-semibold px-3"
                          onClick={() => handleApproveClick(user)} 
                        >
                          Approve
                        </button>
                        <button className="btn btn-danger btn-sm fw-semibold px-3">
                          Reject
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
               {totalPages > 0 && (
            <div className="card-footer ">
              <ul className="pagination justify-content-end mb-0">
             
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
      </div>

      <ApprovalModal
        show={showModal}
        user={selectedUser}
        onClose={closeModal}
       onSuccessRefresh={handleSuccessRefresh}
      />
    </div>
  );
}

export default PendingApprovals;