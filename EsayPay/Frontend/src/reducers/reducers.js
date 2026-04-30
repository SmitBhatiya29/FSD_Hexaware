import { GET_ALL_POLICIES } from "../actions/actionPolicy";

const initialState = {
  policies: [],
};

const policyReducer = (state = initialState, action) => {
  switch (action.type) {
    case GET_ALL_POLICIES:
      return {
        ...state,
        policies: action.payload,
      };
    default:
      return state;
  }
};

export default policyReducer;