import { useState } from "react";
import UserList from "./components/user-list";
import AddUser from "./components/AddUser";
import { NavLink, Outlet } from "react-router-dom";

function App() {
  return (
    <div>
      <nav className="navbar bg-light mb-4">
        <div className="container-fluid d-flex justify-content-start align-items-center gap-4">
            <h3>User Management Dashboard</h3>
            <NavLink className="nav-link" to="/users"> User List</NavLink>
            <NavLink className="nav-link" to="/add-user">Add User</NavLink>
        </div>
      </nav>
      <div className="container-fluid p-2">
        <Outlet />
      </div>
    </div>
  );
}

export default App;
