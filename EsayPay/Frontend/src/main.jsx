import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Dashboard from "./components/HR/Dashboard.jsx";
import EmployeeDirectory from "./components/HR/EmployeeDirectory.jsx";
import PendingApprovals from "./components/HR/PendingApprovals.jsx";
import PrimaryDetailsForm from "./components/auth/PrimaryDetailsForm.jsx";
import WaitingForHRApproval from "./components/WaitingForHRApproval.jsx";
import EmployeeDashboard from "./components/Employee/EmployeeDashboard.jsx";
import ManagerDashboard from "./components/Manager/ManagerDashboard.jsx";
import LeaveRequest from "./components/Employee/LeaveRequests.jsx";
import TimeSheet from "./components/Employee/Timesheet.jsx";
import PayStub from "./components/Employee/PayStubs.jsx";
import { Provider } from "react-redux";
import HrLayout from "./components/HR/HrLayout.jsx";
import EmployeeLayout from "./components/Employee/EmployeeLayout.jsx"
import PayrollPolicies from "./components/HR/PayrollPolicies.jsx"
import ManagerLayout from "./components/Manager/ManagerLayout.jsx";
import MyTeam from "./components/Manager/MyTeam.jsx";
import ApproveLeave from "./components/Manager/ApproveLeave.jsx";
import ApproveTimeSheet from "./components/Manager/ApproveTimeSheet.jsx";
import PPLayout from "./components/Payroll_Processor/PPLayout.jsx"
import PPDashboard from "./components/Payroll_Processor/PPDashboards.jsx"
import AssignBenefit from "./components/Payroll_Processor/AssignBenefit.jsx";
import Benefit from "./components/Payroll_Processor/Benefit.jsx";
import BenefitRequest from "./components/Employee/BenefitRequest.jsx";
import PayrollProcess from "./components/Payroll_Processor/PayrollProcess.jsx";
import Profile from "./components/Employee/Profile.jsx";
import { store } from './store.js'

import Practice from "./components/HR/Practice.jsx";
const routes = createBrowserRouter([
  {
    path: "/",
    element: <App />, // Login page
  },
  {
    path: "/primary-details",
    element: <PrimaryDetailsForm />,
  },
  {
    path: "/wait-for-Hrapprove",
    element: <WaitingForHRApproval />,
  },
  {
    path: "/hr", 
    element: <HrLayout />, 
    children: [
      {
        index: true, 
        element: <Dashboard />,
      },
      {
        path: "employees",
        element: <EmployeeDirectory />,
      },
      {
        path: "pending-approvals", 
        element: <PendingApprovals />,
      },
      {
        path: "payroll", 
        element: <PayrollPolicies />,
      },
      
     
    ],
  },
  {
    path: "/employee",
    element: <EmployeeLayout />,
    children: [
      {
        index: true, 
        element: <EmployeeDashboard />,
      },
      {
        path: "leave-request",
        element: <LeaveRequest />,
      },
      {
        path: "timesheet",
        element: <TimeSheet />,
      },
      {
        path: "paystub",
        element: <PayStub />,
      },
      {
        path: "benefit-request",
        element: <BenefitRequest />,
      },
      {
        path : "profile",
        element : <Profile/>
      }
    ]
  },

  {
    path: "/manager",
    element : <ManagerLayout/>,
    children: [
      {
        index: true, 
        element: <ManagerDashboard />,
      },
      {
        path: "my-team",
        element: <MyTeam />,
      },
      {
        path: "approve-leave",
        element: <ApproveLeave />,
      },
      {
        path: "approve-timesheet",
        element: <ApproveTimeSheet />,
      },
    ]
  },
  {
    path: "/payroll-process",
    element : <PPLayout/>,
    children: [
      {
        index: true, 
        element: <PPDashboard />,
      },
      {
        path: "payment",
        element: <PayrollProcess />,
      },
      {
        path: "benefit",
        element: <Benefit />,
      },
      {
        path: "assign-benefit",
        element: <AssignBenefit />,
      },
    ]
  }
]);
createRoot(document.getElementById("root")).render(
 <Provider store={store}>
     <RouterProvider router={routes} > 
      <App />
  </RouterProvider>
   </Provider>
);
