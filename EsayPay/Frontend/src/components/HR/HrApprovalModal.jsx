import axios from "axios";
import React, { useState, useEffect } from "react";

function ApprovalModal({ show, user, onClose, onSuccessRefresh }) {
  const getManagerApi = "http://localhost:8080/api/hr/all-manager";
  const getRoleApi = "http://localhost:8080/api/hr/type";

  const [manager, setManager] = useState([]);
  const [roleList, setRoleList] = useState([]);

  const [selectedRole, setSelectedRole] = useState("");
  const [selectedManagerId, setSelectedManagerId] = useState(""); 
  const [joiningDate, setJoiningDate] = useState("");

  const [basicSalary, setBasicSalary] = useState("");

  useEffect(() => {
    const getManagerList = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(getManagerApi, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setManager(response.data);
      } catch (err) {
        console.log(err);
      }
    };

    const getRoleList = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(getRoleApi, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setRoleList(response.data);
      } catch (err) {
        console.log(err);
      }
    };

    if (show) {
      getManagerList();
      getRoleList();
    }
  }, [show]);

  if (!show || !user) return null;

  const handleSubmit = async () => {
   
    const isManagerIdRequired = selectedRole !== "MANAGER";
    
    if (!selectedRole || !joiningDate || !basicSalary || (isManagerIdRequired && !selectedManagerId)) {
      alert("Please fill all required fields (*)");
      return;
    }

    
    const approvalData = {
      managerId: selectedRole === "MANAGER" ? null : parseInt(selectedManagerId),
      role: selectedRole,
      joiningDate: joiningDate,
      status: "APPROVED",
    };

    const salaryData = {
      employeeId: user.employeeId,
      basicSalary: parseFloat(basicSalary),
    };

    try {
      const token = localStorage.getItem("token");
      const approveApiUrl = `http://localhost:8080/api/hr/approve-employee/${user.employeeId}`;
      const salaryApiUrl = `http://localhost:8080/api/salary-structure/add`;

      // 1. Employee Approval
      await axios.put(approveApiUrl, approvalData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

    
      await axios.post(salaryApiUrl, salaryData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      alert(`${user.firstName} Approved Successfully!`);

      onSuccessRefresh(user.employeeId);
      onClose();
    } catch (err) {
      console.error(err);
      alert("Error approving employee. Please try again.");
    }
  };

 
  const handleRoleChange = (e) => {
    const role = e.target.value;
    setSelectedRole(role);
    if (role === "MANAGER") {
      setSelectedManagerId("");
    }
  };

  return (
    <div
      className="modal d-block"
      style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }} 
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              Approve: {user.firstName} {user.lastName}
            </h5>
          </div>

          <div className="modal-body">
            <div>
              <p className="mb-1">
                <strong>Name:</strong> {user.firstName} {user.lastName}
              </p>
              <p><strong>Email:</strong> {user.userEmail}</p>
            </div>
            <hr />

            <div>
              <label>Assign Role *</label>
              <select
                className="form-select"
                value={selectedRole}
                onChange={handleRoleChange} 
              >
                <option value="">Select a Role</option>
                {roleList.map((r, index) => (
                  <option key={index} value={r.role}>
                    {r.role}
                  </option>
                ))}
              </select>
            </div>
            <br />

            <div>
              <label>Joining Date *</label>
              <input
                type="date"
                className="form-control"
                value={joiningDate}
                onChange={(e) => setJoiningDate(e.target.value)}
              />
            </div>
            <br />

            <div>
              <label>Actual Designation</label>
              <input
                type="text"
                className="form-control"
                defaultValue={user.designation}
                readOnly
              />
            </div>
            <br />

            <div>
              
              <label>Reporting Manager {selectedRole !== "MANAGER" && "*"}</label>
              <select
                className="form-select"
                value={selectedManagerId}
                onChange={(e) => setSelectedManagerId(e.target.value)}
                disabled={selectedRole === "MANAGER"} 
              >
                
                <option value="">Select a Manager</option> 
                {manager.map((m, index) => (
                  <option key={index} value={m.managerId}>
                    {m.firstName} {m.lastName}
                  </option>
                ))}
              </select>
              {selectedRole === "MANAGER" && (
                <small className="text-muted">Not required for Manager role.</small>
              )}
            </div>

            <br />

            <div>
              <label>Basic Monthly Salary *</label>
              <input
                type="number"
                className="form-control"
                value={basicSalary}
                onChange={(e) => setBasicSalary(e.target.value)}
                placeholder="e.g. 20000"
              />
            </div>
          </div>
          <div className="modal-footer">
            <button
              className="btn btn-secondary"
              type="button"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              className="btn btn-primary"
              type="button"
              onClick={handleSubmit}
            >
              Approve & Activate
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ApprovalModal;