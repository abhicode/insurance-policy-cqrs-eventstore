import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getPolicies } from "../api/api";

const PolicyList = () => {
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPolicies = async () => {
      try {
        const res = await getPolicies();
        setPolicies(res.data);
        setLoading(false);
      } catch (err) {
        setError("Failed to fetch policies. Please try again later.");
        setLoading(false);
      }
    };
    fetchPolicies();
  }, []);

  if (loading) {
    return <div className="text-center mt-5"><div className="spinner-border" role="status"></div></div>;
  }

  if (error) {
    return <div className="alert alert-danger">{error}</div>;
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Insurance Policies</h2>
        <Link to="/policies/new" className="btn btn-primary">
          <i className="bi bi-plus-lg"></i> Create New Policy
        </Link>
      </div>
      
      {policies.length === 0 ? (
        <div className="text-center mt-4">
          <p>No policies found. Create your first policy to get started!</p>
        </div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover">
            <thead>
              <tr>
                <th>Policy Name</th>
                <th>Status</th>
                <th>Created Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {policies.map((policy) => (
                <tr key={policy.id}>
                  <td>
                    <Link to={`/policies/${policy.id}`} className="text-decoration-none">
                      {policy.name}
                    </Link>
                  </td>
                  <td>
                    <span className={`badge bg-${policy.policyStatus === 'ACTIVE' ? 'success' : 'secondary'}`}>
                      {policy.status}
                    </span>
                  </td>
                  <td>{new Date(policy.creationDate).toLocaleDateString()}</td>
                  <td>
                    <Link to={`/policies/${policy.id}`} className="btn btn-sm btn-info me-2">
                      View
                    </Link>
                    <Link to={`/policies/${policy.id}/edit`} className="btn btn-sm btn-primary">
                      Edit
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default PolicyList;
