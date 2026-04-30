import React from "react";
import { Outlet } from "react-router-dom";
import EmployeeTopNavbar from "./EmployeeTopNavbar"
function EmployeeLayout() {
  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <EmployeeTopNavbar />
      <Outlet />
    </div>
  );
}

export default EmployeeLayout;