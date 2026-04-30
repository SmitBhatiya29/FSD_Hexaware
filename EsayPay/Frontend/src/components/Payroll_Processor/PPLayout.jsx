import React from "react";
import { Outlet } from "react-router-dom";
import PPTopNavbar from "./PPTopNavbar";

function PPLayout() {
  return (
    <div className="bg-light" style={{ minHeight: "100vh" }}>
      < PPTopNavbar />
      <Outlet />
    </div>
  );
}

export default PPLayout;