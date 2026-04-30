import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function PrimaryDetailsForm() {
  const formAPI = "http://localhost:8080/api/employee/submit-profile";

  const [firstName, setFirstName] = useState(undefined);
  const [lastName, setLastName] = useState(undefined);
  const [department, setDepartment] = useState(undefined);
  const [designation, setDesignation] = useState(undefined);
  
  const navigate = useNavigate();
  const processFormFill = async (e) => {
    e.preventDefault();

    // validation 
  if (!firstName || !lastName || !department || !designation) {
    alert("Please fill all the required fields before submitting!");
    return; 
  }

    const token = localStorage.getItem("token");

    const config = {
      headers: {
        Authorization: "Bearer " + token,
      },
    };
    
    try {
      
      await axios.post(
        formAPI,
        {
          firstName: firstName,
          lastName: lastName,
          department: department,
          designation: designation,
        },
        config
      );
      
      navigate("/wait-for-Hrapprove")
      
    } catch (err) {
      console.error("Error submitting form", err);
      alert("Failed to submit details.");
    }
  };

  return (
    <div className="container mt-4">
      <div className="card p-4 shadow-sm">
        <div className="row text-center">
          <h2>EsayPay</h2>
          <h5>Tell us about yourself</h5>
        </div>
        <hr />
        <form onSubmit={processFormFill}>
          <div className="row mt-2 mb-4">
            <div className="col-md-6">
              <label className="form-label">First Name:</label>
              <input
                type="text"
                className="form-control"
                placeholder="Enter Your First Name"
                
                onChange={(e) => setFirstName(e.target.value)}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Last Name:</label>
              <input
                type="text"
                className="form-control"
                placeholder="Enter Your Last Name"
                
                onChange={(e) => setLastName(e.target.value)}
              />
            </div>
          </div>
          <div className="row mt-2 mb-2">
            <div className="col">
              <label className="form-label fw-semibold">
                Requested Designation:
              </label>
              <input
                type="text"
                className="form-control"
                placeholder="e.g. Software Engineer"
                
                onChange={(e) => setDesignation(e.target.value)}
              />
            </div>
          </div>
          <div className="mb-4">
            <label className="form-label fw-semibold">Department:</label>
            <select
              className="form-select"
              name="department"
             
              onChange={(e) => setDepartment(e.target.value)}
            >
              <option value="">Select department...</option>
              <option value="Engineering">Engineering</option>
              <option value="Marketing">Marketing</option>
              <option value="IT Support">IT Support</option>
               <option value="HR">Human Resources </option>
            </select>
          </div>

          <div className="row">
            <div className="col-3"></div>
            <div className="col-6">
              <button type="submit" className="btn btn-primary w-100">
                Submit for HR Approval
              </button>
            </div>
            <div className="col-3"></div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default PrimaryDetailsForm;