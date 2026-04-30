import React from "react";
import { Outlet } from "react-router-dom";
import ManagerTopNavbar from "./ManagerTopNavbar";

function ManagerLayout() {
  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <ManagerTopNavbar />
      <Outlet />
    </div>
  );
}

export default ManagerLayout;