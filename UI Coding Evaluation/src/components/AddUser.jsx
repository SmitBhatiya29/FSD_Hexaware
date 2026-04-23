import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AddUser() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [companyName, setCompanyName] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "https://jsonplaceholder.typicode.com/users",
        {
          name: name,
          email: email,
          phone: phone,
          company: { name: companyName },
        },
      );
      console.log("Response from server:", response.data);
      alert("User added successfully!");
      navigate("/users");
    } catch (err) {
      console.log(err);
    }
  };
  return (
    <div>
      <div className="card shadow-sm ">
        <div className="card-header">
          <h2 className="mb-4 text-center ">Add New User</h2>
        </div>
      </div>
      <div className="card-body p-4">
        <form onSubmit={handleSubmit}>
          <div>
            <label className="form-label ">Name</label>
            <input
              className="form-control"
              placeholder="Enter full name"
              type="text"
              required
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div>
            <label className="form-label ">Email</label>
            <input
              className="form-control"
              placeholder="name@gmail.com"
              type="email"
              required
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div>
            <label className="form-label ">Phone</label>
            <input
              className="form-control"
              placeholder="e.g. 1234567890"
              type="text"
              required
              onChange={(e) => setPhone(e.target.value)}
            />
          </div>
          <div>
            <label className="form-label ">Company Name</label>
            <input
              className="form-control"
              placeholder="Enter company name"
              type="text"
              required
              onChange={(e) => setCompanyName(e.target.value)}
            />
          </div>
          <div className="d-flex gap-3 mt-2">
            <button
              type="button"
              className="btn btn-outline-secondary w-50"
              onClick={() => navigate("/users")}
            >
              Cancel
            </button>
            <button type="submit" className="btn btn-primary w-50">
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
export default AddUser;
