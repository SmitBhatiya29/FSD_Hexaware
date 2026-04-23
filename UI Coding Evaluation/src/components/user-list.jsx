import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
function UserList() {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const getAllUsers = async () => {
      try {
        const res = await axios.get(
          "https://jsonplaceholder.typicode.com/users",
        );
        setUsers(res.data);
       
      } catch (err) {
        console.log(err);
      }
    };
    getAllUsers();
  }, []);

  const handleDelete = async (id) => {
    try {
      await axios.delete(`https://jsonplaceholder.typicode.com/users/${id}`);
      setUsers(users.filter((user) => user.id !== id));
    } catch (err) {
      console.log(err);
    }
  };
  return (
    <div>
      <div>
        <h4>User Management</h4>
        <button
          className="btn btn-primary mb-2 mt-2"
          onClick={() => navigate("/add-user")}
        >
          {" "}
          + Add New User{" "}
        </button>
      </div>
      <div className="card">
        <div className="card-header">
          <h4>Users ({users.length})</h4>
        </div>

        <div children="card-body">
          <table className="table">
            <thead className="table-header">
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Company Name</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user, index) => (
                <tr key={user.id}>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td>{user.phone}</td>
                  <td>{user.company.name}</td>
                  <td>
                    <button
                      className="btn btn-danger btn-sm"
                      onClick={() => handleDelete(user.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}

              {users.length === 0 && (
                <tr>
                  <td className="text-center">No users found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
export default UserList;
