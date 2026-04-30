import { useState, useEffect } from "react";
import axios from "axios";
import { data } from "react-router-dom";


function StatCard({ label, value, icon }) {
  return (
    <div className="card shadow-sm border-0 mb-3">
      <div className="card-body d-flex align-items-center">
        <div className="bg-primary bg-opacity-10 p-3 rounded me-3 fs-3">
          {icon}
        </div>
        <div>
          <h3 className="fs-4 fw-bold mb-0">{value}</h3>
          <p className="text-muted mb-0">{label}</p>
        </div>
      </div>
    </div>
  );
}
function ManagerDashboard(){
  const PendingLeaveRequestApi = "http://localhost:8080/api/manager/get-leave-req";
  const PayStubByDepartment ="http://localhost:8080/api/manager/department-by-overview";

  const [pendingLeaves, setPendingLeaves] = useState([]);
  const [departmentOverview, setDepartmentOverview] = useState([]);
 
  const [errMsg, setErrMsg] = useState(undefined);
  const [userProfile, setUserProfile] = useState(null);
  const now = new Date();

  const[teamSize,setTeamSize]= useState(undefined);
  const[countPendingLeaveReq,setCountPendingLeaveReq]= useState(undefined);
  const[countPendingTimeSheet,setCountPendingTimeSheet]= useState(undefined);
  const[totalTeamPayStub,setTotalTeamPayStub] = useState(undefined)

  useEffect(() => {
    //profile
    try {
      const token = localStorage.getItem("token");

      const AuthConfig = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
      const storedProfile = localStorage.getItem("userProfile");
      if (storedProfile) {
        setUserProfile(JSON.parse(storedProfile));
      }
    } catch (err) {
      setErrMsg(err.message);
      console.log(errMsg);
    }

    const getAllPendingLeaveRequest = async () => {
      try {
        const token = localStorage.getItem("token");
        const AuthConfig = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params : {
            page : 0,
            size : 5
          },
        };

        const response = await axios.get(PendingLeaveRequestApi, AuthConfig);
        setPendingLeaves(response.data.data);
        
      } catch (err) {
        setErrMsg(err.message);
        console.log(errMsg);
      }
    };
    getAllPendingLeaveRequest();

    
    const getPayStubByDep = async () => {
      try {
        const token = localStorage.getItem("token");
        const AuthConfig = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        const response = await axios.get(PayStubByDepartment, AuthConfig);
        setDepartmentOverview(response.data);
        
      } catch (err) {
        setErrMsg(err.message);
        console.log(errMsg);
      }
    };
    getPayStubByDep();


    const getStats = async () => {
      try {
        const token = localStorage.getItem("token");
        const AuthConfig = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };
        const response = await axios.get("http://localhost:8080/api/manager/stats", AuthConfig);

        setTeamSize(response.data.teamSize);
        setCountPendingLeaveReq(response.data.countPendingLeaveReq);
        setCountPendingTimeSheet(response.data.countPendingTimeSheet);
        setTotalTeamPayStub(response.data.totalTeamPayStub);

      } catch (err) {
        setErrMsg(err.message);
        console.log(errMsg);
      }
    };
    getStats();



  }, []);

  const dashboardStats = [
    { id: 1, label: "Team Size", value: teamSize, icon: "👥" },
    { id: 2,label: "Pending Leave Requests",value: countPendingLeaveReq,icon: "⏳"},
    { id: 3, label: "Pending Timesheets", value: countPendingTimeSheet, icon: "🏢" },
    { id: 4, label: "Pay Stubs Over-all", value: totalTeamPayStub , icon: "💸" },
  ];

  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <div className="container-fluid px-4 pb-4">
        <div className="mb-4">
          <h2>Good morning, {userProfile?.firstName || "Manager"}! 👋</h2>
          <p className="text-secondary">
            {now.toLocaleDateString("en-US", {
              weekday: "long",
              year: "numeric",
              month: "long",
              day: "numeric",
            })}
          </p>
        </div>

        {/* Stats Row */}
        <div className="row mb-4">
          {dashboardStats.map((stat) => (
            <div className="col-md-3" key={stat.id}>
              <StatCard
                label={stat.label}
                value={stat.value}
                icon={stat.icon}
              />
            </div>
          ))}
        </div>

        <div className="row">
          {/* Table 1: Team Leave Requests */}
          <div className="col-md-6 mb-4">
            <div className="card shadow-sm border-0 h-100">
              <div className="card-header bg-white pt-3 pb-2 border-0">
                <h5 className="card-title mb-0">Team Leave Requests</h5>
              </div>
              <hr className="m-0" />
              <div className="card-body">
                <table className="table table-hover align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {pendingLeaves.map((user, index) => (
                      <tr key={index}>
                        <td>
                          {user.employeeName} 
                        </td>
                        <td>{user.leaveType}</td>
                        <td>{user.numofDays}</td>
                        <td>
                          <span className="badge bg-warning text-dark">
                            {user.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Table 2: Quick Team Overview*/}
          <div className="col-md-6 mb-4">
            <div className="card shadow-sm border-0 h-100">
              <div className="card-header bg-white pt-3 pb-2 border-0">
                <h5 className="card-title mb-0">Quick Team PayStub Overview</h5>
              </div>
              <hr className="m-0" />
              <div className="card-body">
                <table className="table table-borderless table-hover align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>Department Name</th>
                      <th>Total Employees</th>
                    </tr>
                  </thead>
                  <tbody>
                    {departmentOverview.map((dept, index) => (
                      <tr key={index}>
                        <td>{dept.departmentName}</td>
                        <td className="fw-bold ">{dept.totalPayStub}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
export default ManagerDashboard;