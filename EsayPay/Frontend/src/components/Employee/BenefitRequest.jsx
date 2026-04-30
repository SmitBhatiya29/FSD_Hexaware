import axios from "axios";
import { useState, useEffect } from "react";

function BenefitRequest() {
  const getAvailableBenefitsAPI = "http://localhost:8080/api/benefits/all"; 

  const getMyBenefitRequestsAPI = "http://localhost:8080/api/employee/get/benefit-reqest";

  const submitBenefitReqAPI = "http://localhost:8080/api/employee/benefit-request";
  
  const deleteBenefitReqAPI = "http://localhost:8080/api/employee/delete-benefit-request";

  const [refreshTrigger, setRefreshTrigger] = useState(true);

  const [newReqBtn, setNewReqBtn] = useState(false);
  const [myRequests, setMyRequests] = useState([]);
  const [availableBenefits, setAvailableBenefits] = useState([]);
  const [errMsg, setErrMsg] = useState("");

  const [selectedBenefitId, setSelectedBenefitId] = useState("");

  useEffect(() => {
    const getAvailableBenefits = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(`${getAvailableBenefitsAPI}?page=0&size=50`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const benefitsList = res.data.data;
        setAvailableBenefits(benefitsList);

        if (benefitsList && benefitsList.length > 0) {
          setSelectedBenefitId(benefitsList[0].id);
        }
      } catch (err) {
        setErrMsg(err.message);
        console.error("Error fetching benefits:", err.message);
      }
    };

    const getMyRequests = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(`${getMyBenefitRequestsAPI}?page=0&size=10`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setMyRequests(res.data.data || []);
      } catch (err) {
        setErrMsg(err.message);
        console.error("Error fetching my requests:", err.message);
      }
    };

    getAvailableBenefits();
    getMyRequests();
  }, [refreshTrigger]);

  const submitBenefitReq = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");
      const res = await axios.post(
        `${submitBenefitReqAPI}?benefitId=${selectedBenefitId}`,
        null,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log("Benefit Requested Successfully:", res.data);
      alert("Benefit Request Submitted!");
      setNewReqBtn(false);
      setErrMsg("");
      setRefreshTrigger(!refreshTrigger);
    } catch (err) {
      if (err.response && err.response.data) {
        setErrMsg(err.response.data); 
      } else {
        setErrMsg("Something went wrong. Please try again.");
      }
      console.error("Submit Error:", err);
    }
  };

  const handleDeleteRequest = async (requestId) => {
    if (!window.confirm("Are you sure you want to delete this benefit request?")) {
      return;
    }

    try {
      const token = localStorage.getItem("token");
      await axios.delete(`${deleteBenefitReqAPI}/${requestId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      alert("Request Deleted Successfully!");
      setErrMsg(""); 
      setRefreshTrigger(!refreshTrigger); 
    } catch (err) {
      if (err.response && err.response.data) {
        setErrMsg(err.response.data);
      } else {
        setErrMsg("Failed to delete the request.");
      }
      console.error("Delete Error:", err);
    }
  };

  return (
    <div>
      <div className="m-2">
        <div className="row">
          <h3>Benefit Requests</h3>
          <p className="text-muted">Explore and request company benefits</p>
        </div>

        {errMsg && <div className="alert alert-danger">{errMsg}</div>}

        <div className="mb-2">
          <button
            className="btn btn-primary"
            onClick={() => setNewReqBtn(true)}
          >
            + Request Benefit
          </button>
        </div>

        {newReqBtn && (
          <div className="card border-info mb-4">
            <div className="card-header">
              <span className="card-title">New Benefit Request</span>
            </div>
            <div className="card-body">
              <form className="form" onSubmit={submitBenefitReq}>
                
                <label className="form-label">Select Benefit :</label>
                <select
                  className="form-select mb-3"
                  value={selectedBenefitId}
                  onChange={(e) => setSelectedBenefitId(e.target.value)}
                  required
                >
                  <option value="" disabled>-- Select a Benefit --</option>
                  {availableBenefits.map((benefit, index) => (
                    <option key={benefit.id || index} value={benefit.id}>
                      {benefit.benefitName}
                    </option>
                  ))}
                </select>

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-primary" disabled={!selectedBenefitId}>
                    Submit Request
                  </button>
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setNewReqBtn(false)}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        <div className="card">
          <div className="card-header">
            <h4>My Benefit Requests</h4>
          </div>
          <div className="card-body">
            <table className="table">
              <thead>
                <tr>
                  <th>Benefit Name</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {myRequests.length > 0 ? (
                  myRequests.map((req, index) => (
                    <tr key={req.id || index}>
                      <td>{req.benefitName}</td>
                      <td>
                        {req.benefitAmount ? `₹${req.benefitAmount}` : "N/A"}
                      </td>
                      <td>
                        <span className={`badge mt-1 ${
                          req.status === 'APPROVED' ? 'bg-success' :
                          req.status === 'PENDING' ? 'bg-warning text-dark' :
                          req.status === 'REJECTED' ? 'bg-danger' : 'bg-secondary'
                        }`}>
                          {req.status}
                        </span>
                      </td>
                      <td>
                        <button 
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => handleDeleteRequest(req.id)}
                          disabled={req.status !== 'PENDING'}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="4" className="text-center text-muted">
                      No benefit requests found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default BenefitRequest;