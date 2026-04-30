import React from "react";
import { Outlet } from "react-router-dom";
import TopNavbar from "./HrTopNavbar";

function HrLayout() {
  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      <TopNavbar />
      <Outlet />
    </div>
  );
}

export default HrLayout;