import { useState } from 'react'
import Dashboard from './components/HR/Dashboard'
import PendingApprovals from './components/HR/PendingApprovals'
import LandingPage from './components/auth/Landing-page'
import PrimaryDetailsForm from './components/auth/PrimaryDetailsForm'
import EmployeeDirectory from './components/HR/EmployeeDirectory'



function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <LandingPage/>
     
    </>
  )
}

export default App
