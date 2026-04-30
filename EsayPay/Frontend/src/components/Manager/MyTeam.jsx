import React, { useState, useEffect } from 'react';
import axios from 'axios';

function MyTeam() {
  const [teamMembers, setTeamMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        setLoading(true);
        const token = localStorage.getItem('token'); 
        
        
        const response = await axios.get('http://localhost:8080/api/manager/get-employees?size=50', {
          headers: {
            'Authorization': `Bearer ${token}` 
          }
        });
        setTeamMembers(response.data.data);
        console.log(teamMembers)
        
      } catch (err) {
        console.error("Error fetching team:", err);
        setError("Could not load team members. Please try again.");
      } finally {
        setLoading(false); 
      }
    };

    fetchEmployees();
  }, []); 

  
  if (loading) {
    return (
      <div className="container mt-5 text-center text-muted">
        <div className="spinner-border spinner-border-sm me-2" role="status"></div>
        Loading team members...
      </div>
    );
  }

  if (error) {
    return <div className="container mt-5 text-center text-danger">{error}</div>;
  }

  
  return (
    <div className="container-flud p-2">
      
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="mb-1 text-dark fw-bold">My Team</h2>
          <p className="text-muted mb-0">See your Team Memebers</p>
        </div>
        <div>
          <span className="badge bg-danger rounded-pill px-3 py-2 fs-6 opacity-75">
            Team Size = {teamMembers.length}
          </span>
        </div>
      </div>

      {teamMembers.length === 0 ? (
        <div className="card shadow-sm border-0">
          <div className="card-body text-center p-5">
            <h4 className="text-muted mb-0">👥 No team members assigned to you yet.</h4>
          </div>
        </div>
      ) : (
        <div className="row g-3">
          {teamMembers.map((member, index) => (
            <div key={member.email || index} className="col-12  col-lg-3">
              <div className="card h-100 shadow-sm border-light transition-hover">
                <div className="card-body">
                  
                 
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <div>
                      <h5 className="card-title fw-bold text-dark mb-1">
                        {member.firstName} {member.lastName}
                      </h5>
                      <p className="card-text text-secondary mb-0" >
                        {member.designation}
                      </p>
                      <p className="card-text text-muted" >
                        {member.department}
                      </p>
                    </div>
                    <div>
                      <span className="badge bg-success rounded-pill px-2 py-1">Active</span>
                    </div>
                  </div>

                  <hr className="text-muted opacity-25 my-3" />

                  {/* Card Details */}
                  <div className="d-flex flex-column gap-2">
                    <div className="d-flex justify-content-between align-items-center">
                      <span className="text-muted" >Role :</span>
                      <span className="fw-semibold text-dark" >
                        {member.role}
                      </span>
                    </div>
                    
                    <div className="d-flex justify-content-between align-items-center">
                      <span className="text-muted" style={{ fontSize: '0.85rem' }}>Years In Service : </span>
                      <span className="fw-semibold text-dark" >
                        {member.yearsInService} Years
                      </span>
                    </div>
                    
                    <div className="d-flex justify-content-between align-items-center">
                      <span className="text-muted" >Email :</span>
                      <span className="fw-semibold text-dark" >
                        {member.email}
                      </span>
                    </div>
                  </div>

                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyTeam;