import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import UserList from './components/user-list.jsx';
import AddUser from './components/AddUser.jsx';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
const routes = createBrowserRouter([
  {
    path: "/",
    element: <App />, 
    children: [
      {
        index: true,
        element:<UserList />
      },
      {
        path: "users",
        element: <UserList />
      },
      {
        path: "add-user",
        element: <AddUser />
      }
    ]
  }
]);
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={routes} />
  </StrictMode>,
)
