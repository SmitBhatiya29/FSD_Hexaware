import { applyMiddleware, combineReducers, createStore } from "redux";
import { thunk } from "redux-thunk";

import policyReducer from "./reducers/reducers"; 

const reducers = combineReducers({
 
  policy: policyReducer, 
});

export const store = createStore(reducers, applyMiddleware(thunk));