import axios from "axios";

export const GET_ALL_POLICIES = "GET_ALL_POLICIES";

export const getAllPolicies = () => {
  return async (dispatch) => {
    try {
      const response = await axios.get("http://localhost:8080/api/payroll-policies/all", {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });
      
      // Data aane ke baad reducer ko bhejenge
      dispatch({
        type: GET_ALL_POLICIES,
        payload: response.data,
      });
    } catch (error) {
      console.error("Error fetching policies:", error);
      alert("Policies load karne me error aayi!");
    }
  };
};