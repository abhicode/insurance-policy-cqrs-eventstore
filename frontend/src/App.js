import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import PolicyList from "./components/PolicyList";
import PolicyDetail from "./components/PolicyDetail";
import PolicyForm from "./components/PolicyForm";
import "./styles.css";

const App = () => (
  <BrowserRouter>
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container">
          <span className="navbar-brand">Insurance Policy Manager</span>
        </div>
      </nav>

      <div className="container mt-4">
        <Routes>
          <Route path="/" element={<Navigate to="/policies" replace />} />
          <Route path="/policies" element={<PolicyList />} />
          <Route path="/policies/new" element={<PolicyForm />} />
          <Route path="/policies/:id" element={<PolicyDetail />} />
          <Route path="/policies/:id/edit" element={<PolicyForm isEdit />} />
        </Routes>
      </div>
    </div>
  </BrowserRouter>
);

export default App;
