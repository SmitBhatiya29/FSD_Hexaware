import { useState, useEffect } from "react";
import axios from "axios";

function Benefit() {
  const [benefits, setBenefits] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [benefitName, setBenefitName] = useState("");
  const [description, setDescription] = useState("");
  const [errMsg, setErrMsg] = useState(undefined);

  const getBenefitsApi = "http://localhost:8080/api/benefits/all";
  const addBenefitApi = "http://localhost:8080/api/benefits/add";

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 5;

  const [isEditing, setIsEditing] = useState(false);
  const [editId, setEditId] = useState(null);

  const updateBenefitApi = "http://localhost:8080/api/benefits/update";
  const deleteBenefitApi = "http://localhost:8080/api/benefits/delete";


  useEffect(() => {
    const token = localStorage.getItem("token");
    const getBenefits = async () => {
      try {
        
        const response = await axios.get(getBenefitsApi, {
          headers: { Authorization: `Bearer ${token}` },
          params: {
          page: currentPage,
          size: pageSize,
        },
        });
        setBenefits(response.data.data);
        setTotalPages(response.data.totalPages)
  
      } catch (err) {
        setErrMsg(err.message + " Something went wrong fetching benefits!");
        console.log(errMsg);
      }
    };
    getBenefits();
  }, []);

  const handleEditClick = (benefit) => {
    setIsEditing(true);
    setEditId(benefit.id);
    setBenefitName(benefit.benefitName);
    setDescription(benefit.description);
    setShowAddForm(true);
    window.scrollTo(0, 0); // Scroll to top form
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    const payload = {benefitName,description};
    const headers = {Authorization : `Bearer ${token}`}

    try{
      if(isEditing){
        await axios.put(`${updateBenefitApi}/${editId}`,payload,{headers});
        setBenefits(benefits.map((b)=> 
        b.id === editId ? {...b,benefitName,description} : b
        ));
      }else{
        await axios.post(addBenefitApi, payload, { headers });
        window.location.reload();
      }
      resetForm();
    }catch(err){

      console.error("Error saving benefit:", err);
      alert("Failed to save benefit.");
    }
  }
  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this benefit?")) {
      try {
        const token = localStorage.getItem("token");
        await axios.delete(`${deleteBenefitApi}/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        
        
        setBenefits(benefits.filter((b) => b.id !== id));
        alert("Benefit deleted successfully!");
      } catch (err) {
        console.error("Error deleting benefit:", err);
        alert("Failed to delete benefit.");
      }
    }
  };

  const resetForm = () => {
    setBenefitName("");
    setDescription("");
    setShowAddForm(false);
    setIsEditing(false);
    setEditId(null);
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

  return (
    <div className="container-fluid px-4 pb-4">
      <div className="row mb-2 bg-light">
        <h3>Manage Benefits</h3>
        <p className="text-muted">Add and view employee benefits</p>
      </div>
      <div className="mb-3">
        <button 
          className={showAddForm ? "btn btn-secondary" : "btn btn-primary"}
          onClick={() =>{ if (showAddForm) resetForm(); 
            else setShowAddForm(true);}}
        >
          {showAddForm ? "Cancel" : "Add Benefit"}
        </button>
      </div>

      {showAddForm && (
        <div className="card mb-4">
          <div className="card-header">
            <h4>{isEditing ? "Update Benefit" : "Add New Benefit"}</h4>
          </div>
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              <div className="row mb-3">
                <div className="col-md-6">
                  <label className="form-label">Benefit Name</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    placeholder="Enter benefit name"
                    value={benefitName}
                    onChange={(e) => setBenefitName(e.target.value)}
                    required
                  />
                </div>
                <div className="col-md-6">
                  <label className="form-label">Description</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    placeholder="Enter description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                  />
                </div>
              </div>
              <button type="submit" className="btn btn-success">{isEditing ? "Update Benefit" : "Save Benefit"}</button>
            </form>
          </div>
        </div>
      )}


      <div className="card">
        <div className="card-header">
          <div className="row">
            <h4>Benefits List ({benefits.length})</h4>
          </div>
        </div>
        <div className="card-body">
          <table className="table">
            <thead>
              <tr>
                <th>Sr No.</th>
                <th>Benefit Name</th>
                <th>Description</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {benefits.map((b, index) => (
                <tr key={b.id || index}>
                  <td>{index + 1}</td>
                  <td>{b.benefitName}</td>
                  <td>{b.description}</td>
                  <td>
                    <button className="btn btn-primary me-3" onClick={() => handleEditClick(b)}>
                      Update
                    </button>
                    <button 
                      className="btn btn-danger"
                      onClick={() => handleDelete(b.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
              {benefits.length === 0 && (
                <tr>
                  <td colSpan="4" className="text-center text-muted py-3">
                    No benefits found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        {totalPages > 0 && (
          <div className="card-footer">
            <ul className="pagination justify-content-end mb-0">
              <li className={`page-item ${currentPage === 0 ? "disabled" : ""}`}>
                <button
                  className="page-link"
                  onClick={handlePrevPage}
                  disabled={currentPage === 0}
                >
                  Previous
                </button>
              </li>

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

              <li className={`page-item ${currentPage === totalPages - 1 ? "disabled" : ""}`}>
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
  );
}

export default Benefit;