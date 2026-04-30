import { useState } from "react";
import "./Landing-page.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function LandingPage() {
  const [isLogin, setIsLogin] = useState(true);

  const [username, setUsername] = useState(undefined);
  const [password, setPassword] = useState(undefined);
  const [confirmPassword, setConfirmPassword] = useState(undefined);

  const [token, setToken] = useState(undefined);
  const [errorMsg, setErrorMsg] = useState(undefined);
  const [successMsg, setSuccessMsg] = useState(undefined);

  const navigate = useNavigate();

  const loginAPi = "http://localhost:8080/api/auth/login";
  const detailsApi = "http://localhost:8080/api/auth/user-details";

  const sigupApi = "http://localhost:8080/api/employee/signup";
  const statusCheckApi = "http://localhost:8080/api/employee/profile-details";

  const processAuth = async (e) => {
    e.preventDefault();
    //clear the earlyer message....
    setErrorMsg(undefined);
    setSuccessMsg(undefined);
    // Generate encoded string from username and password using btoa (binary to ASCII)
    //djfghjdfhgjdhgjdhgjdfhg : username:password
    if (isLogin) {
      try {
        let encodedString = window.btoa(username + ":" + password);
        //console.log(encodedString)
        const config = {
          headers: {
            Authorization: "Basic " + encodedString,
          },
        };

        // Call token API to get the token
        const response = await axios.get(loginAPi, config);
        setToken(response.data.token);
        localStorage.setItem("token", response.data.token);

        const detailsHeader = {
          headers: {
            Authorization: "Bearer " + response.data.token,
          },
        };

        const apiResponse = await axios.get(detailsApi, detailsHeader);

        if (apiResponse.data.isActive) {
          //1. fetch the status
          console.log("user ma isactive true chhe");

          const profileStatusRes = await axios.get(
            statusCheckApi,
            detailsHeader,
          );

          localStorage.setItem(
            "userProfile",
            JSON.stringify(profileStatusRes.data),
          );

          console.log("API fetch THai gai chhe");

          //2. check stats approved or pending
          console.log(profileStatusRes.data);

          if (profileStatusRes.data.status == "PENDING") {
            console.log("inside of if " + profileStatusRes.data.status);
            console.log(profileStatusRes.data);
            navigate("/wait-for-Hrapprove");
          } //approval logic lagavanu chhe but pehla pending vadu joi le ek baar
          else if (profileStatusRes.data.status == "APPROVED") {
            switch (apiResponse.data.role) {
              case "HR":
                navigate("/hr");
                break;
              case "EMPLOYEE":
                navigate("/employee");
                break;
              case "MANAGER":
                navigate("/manager");
                break;
              case "PAYROLL_PROCESSOR":
                navigate("/payroll-process");
                break;
            }
          }
          //employee e eni form dill kari didhi chhe have ahiya evu check kar ke hr e approve aapi ke nai
        } else {
          navigate("/primary-details");
        }
      } catch (err) {
        console.log(err.errorMsg);
        setErrorMsg("Invalid Credentials");
      }
    }
    if (!isLogin) {
     
      if (password !== confirmPassword) {
        setErrorMsg("Password and Confirm Password do not match!");
        return; 
      }

      try {
        await axios.post(sigupApi, {
          email: username,
          password: password,
        });
        setSuccessMsg("Sign Up complete!!, please Login");
        navigate("/");
      } catch (err) {
        setErrorMsg(err.message);
      }
    }
  };
  return (
    <div className="page-container">
      <nav className="navbar">
        <div className="nav-logo">MyLogo</div>
      </nav>

      <div className="content">
        <div className="auth-container">
          <div className="toggle-buttons">
            <button
              className={isLogin ? "active" : ""}
              onClick={() => {
                setIsLogin(true);
                setErrorMsg(undefined);
                setSuccessMsg(undefined);
              }}
            >
              Login
            </button>
            <button
              className={!isLogin ? "active" : ""}
              onClick={() => {
                setIsLogin(false);
                setErrorMsg(undefined);
                setSuccessMsg(undefined);
              }}
            >
              Register
            </button>
          </div>

          <form className="auth-form" onSubmit={(e) => processAuth(e)}>
            {errorMsg == undefined ? (
              ""
            ) : (
              <div className="alert alert-danger mt-4">{errorMsg}</div>
            )}
            {successMsg == undefined ? (
              ""
            ) : (
              <div className="alert alert-primary mt-4">{successMsg}</div>
            )}
            <h2>{isLogin ? "Login" : "Create Account"}</h2>

            <input
              type="text "
              placeholder="Email Address"
              required
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="password"
              placeholder="Password"
              required
              onChange={(e) => setPassword(e.target.value)}
            />

            {!isLogin && (
              <input
                type="password"
                placeholder="Confirm Password"
                required
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            )}

            <button type="submit">{isLogin ? "Login" : "Sign Up"}</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
