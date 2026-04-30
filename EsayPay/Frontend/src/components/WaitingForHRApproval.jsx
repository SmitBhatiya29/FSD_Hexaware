import { useLocation ,useNavigate} from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios"; 

function WaitingApproval() {

  const navigate = useNavigate();

  const statusCheckApi = "http://localhost:8080/api/employee/profile-details";
  
  const [errMsg, setErrMsg] = useState(undefined);
  const [firstName, setfirstName] = useState(undefined);
  const [lastName, setlastName] = useState(undefined);
  const [department, setdepartment] = useState(undefined);
  const [designation, setdesignation] = useState(undefined);
  const [status, setstatus] = useState(undefined);

   const handleLogoutBtn = () => {
    console.log("logout called")
    localStorage.removeItem("token");
    navigate("/")
  }

  useEffect(() => {
    const getDetails = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(statusCheckApi, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        
      
        const data = response.data;
        setfirstName(data.firstName);
        setlastName(data.lastName);
        setdepartment(data.department);
        setdesignation(data.designation);
        setstatus(data.status);
        
      } catch (err) {
        setErrMsg(err.message);
       
        console.error("Error fetching details:", err.message);
      }
    };
    
    getDetails();
  }, []);

  return (
    <div className="card mt-4 p-2 m-4">
     
      {errMsg && <div className="alert alert-danger text-center">{errMsg}</div>}
      
      <div className="card-header text-center">
        <h2>Waiting For HR Approval of Your Profile </h2>
        <p className="text-muted">Your Submitted details here....</p>
      </div>
      <div className="card-body text-center">
        <p>First Name : {firstName}</p>
        <p>Last Name : {lastName}</p>
        <p>Department : {department}</p>
        <p>Designation : {designation}</p>
        <p className="badge bg-warning text-dark">Status : {status}</p>
        <hr />
        <button className="btn btn-secondary"  onClick={handleLogoutBtn}> Go To Home </button>
      </div>
    </div>
  );
}

export default WaitingApproval;