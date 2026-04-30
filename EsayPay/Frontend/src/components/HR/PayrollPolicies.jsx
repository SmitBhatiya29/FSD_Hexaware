import React, { useState, useEffect } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getAllPolicies } from "../../actions/actionPolicy";

function PayrollPolicies() {
  const dispatch = useDispatch();
  
  const policies = useSelector((state) => state.policy.policies) || []; 

  const [showForm, setShowForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editId, setEditId] = useState(null);

  const [formData, setFormData] = useState({
    minSalary: "",
    maxSalary: "",
    taxPercentage: "",
    hraPercentage: "",
    standardDeduction: "",
  });

  const handleEditClick = (policy) => {
    setIsEditing(true);
    setEditId(policy.id);

    setFormData({
      minSalary: policy.minSalary,
      maxSalary: policy.maxSalary,
      taxPercentage: policy.taxPercentage,
      hraPercentage: policy.hraPercentage,
      standardDeduction: policy.standardDeduction,
    });

    setShowForm(true); 
  };

 
  useEffect(() => {
    dispatch(getAllPolicies());
  }, [dispatch]);

  
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const headers = {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      };

      if (isEditing) {
        await axios.put(
          `http://localhost:8080/api/payroll-policies/update/${editId}`,
          formData,
          { headers },
        );
        alert("Payroll Policy Updated Successfully!");
      } else {
        await axios.post(
          "http://localhost:8080/api/payroll-policies/add",
          formData,
          { headers },
        );
        alert("New Payroll Policy Added Successfully!");
      }

      resetForm();
     
      dispatch(getAllPolicies());
    } catch (error) {
      console.error("Error saving policy:", error);
      alert("Policy save karne me error aayi!");
    }
  };

  const resetForm = () => {
    setShowForm(false);
    setIsEditing(false);
    setEditId(null);
    setFormData({
      minSalary: "",
      maxSalary: "",
      taxPercentage: "",
      hraPercentage: "",
      standardDeduction: "",
    });
  };

  return (
    <div className="container-fluid mt-2">
      <div>
        <h2>Payroll Policies</h2>
        <p className="text-muted">Manage Payroll Policies of Your Company</p>
        <button
          className="btn btn-primary fw-bold mt-2 mb-2"
          onClick={() => {
            if (showForm) {
              resetForm();
            } else {
              setShowForm(true);
            }
          }}
        >
          {showForm ? "Close Form" : "+ Add New Policy"}
        </button>
      </div>

      {showForm && (
        <div className="card mb-4 bg-light">
          <div className="card-body">
            <h3 className="card-title mb-4">{isEditing ? "Edit Policy" : "Add Policy"}</h3>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Min Salary (Yearly)</label>
                <input
                  type="number"
                  step="any"
                  name="minSalary"
                  className="form-control"
                  value={formData.minSalary}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Max Salary (Yearly)</label>
                <input
                  type="number"
                  step="any"
                  name="maxSalary"
                  className="form-control"
                  value={formData.maxSalary}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Tax Percentage (%)</label>
                <input
                  type="number"
                  step="any"
                  name="taxPercentage"
                  className="form-control"
                  value={formData.taxPercentage}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">HRA Percentage (%)</label>
                <input
                  type="number"
                  step="any"
                  name="hraPercentage"
                  className="form-control"
                  value={formData.hraPercentage}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Standard Deduction</label>
                <input
                  type="number"
                  step="any"
                  name="standardDeduction"
                  className="form-control"
                  value={formData.standardDeduction}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mt-4">
                <button type="submit" className="btn btn-success me-2">
                 {isEditing ? "Update Policy" : "Save Policy"}
                </button>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={resetForm}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <br />

      <div className="table mt-1">
        <table className="table table-bordered  table-hover mt-2">
          <thead className="table-light">
            <tr>
              <th>ID</th>
              <th>Min Salary</th>
              <th>Max Salary</th>
              <th>Tax (%)</th>
              <th>HRA (%)</th>
              <th>Standard Deduction</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {policies.length > 0 ? (
              policies.map((policy) => (
                <tr key={policy.id}>
                  <td>{policy.id}</td>
                  <td>₹{policy.minSalary}</td>
                  <td>₹{policy.maxSalary}</td>
                  <td>{policy.taxPercentage}%</td>
                  <td>{policy.hraPercentage}%</td>
                  <td>₹{policy.standardDeduction}</td>
                  <td>
                    <button
                      className="btn btn-secondary btn fw-semibold "
                      onClick={() => handleEditClick(policy)}
                    >
                      Edit
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="7" className="text-center py-4">
                  No Payroll Policies Found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default PayrollPolicies;