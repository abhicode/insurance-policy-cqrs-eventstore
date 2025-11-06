import axios from "axios";

let BASE_URL = '';
if (window.location.hostname === 'localhost') {
  BASE_URL = 'http://localhost:8080/api/policies';
} else {
  BASE_URL = '/api/policies';
}

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Accept": "application/json"
  },
  withCredentials: true
});

export const getPolicies = () => {
  return api.get("");
};

export const getPolicy = (id) => {
  if (!id) throw new Error("Policy ID is required");
  return api.get(`/${id}`);
};

export const createPolicy = (data) => {
  if (!data) throw new Error("Policy data is required");
  return api.post("", data);
};

export const updatePolicy = (id, data) => {
  if (!id) throw new Error("Policy ID is required");
  if (!data) throw new Error("Policy data is required");
  return api.put(`/${id}`, data);
};
