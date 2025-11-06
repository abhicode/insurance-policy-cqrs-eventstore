import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getPolicy } from "../api/api";

const PolicyDetail = () => {
  const { id } = useParams();
  const [policy, setPolicy] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPolicy = async () => {
      try {
        const res = await getPolicy(id);
        setPolicy(res.data);
        setLoading(false);
      } catch (err) {
        setError("Failed to fetch policy details. Please try again later.");
        setLoading(false);
      }
    };
    fetchPolicy();
  }, [id]);

  if (loading) {
    return <div className="text-center mt-5"><div className="spinner-border" role="status"></div></div>;
  }

  if (error) {
    return <div className="alert alert-danger">{error}</div>;
  }

  if (!policy) {
    return <div className="alert alert-warning">Policy not found</div>;
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Policy Details</h2>
        <div>
          <Link to="/policies" className="btn btn-outline-secondary me-2">
            <i className="bi bi-arrow-left"></i> Back to List
          </Link>
          <Link to={`/policies/${id}/edit`} className="btn btn-primary me-2">
            <i className="bi bi-pencil"></i> Edit
          </Link>
        </div>
      </div>

      <div className="card">
        <div className="card-body">
          <div className="row">
            <div className="col-md-6">
              <div className="mb-3">
                <label className="fw-bold">Policy Name</label>
                <p className="mb-0">{policy.name}</p>
              </div>
              <div className="mb-3">
                <label className="fw-bold">Status</label>
                <p className="mb-0">
                  <span className={`badge bg-${policy.status} === 'ACTIVE' ? 'success' : 'secondary'}`}>
                    {policy.status}
                  </span>
                </p>
              </div>
            </div>
            <div className="col-md-6">
              <div className="mb-3">
                <label className="fw-bold">Coverage Start Date</label>
                <p className="mb-0">{new Date(policy.coverageStartDate).toLocaleDateString()}</p>
              </div>
              <div className="mb-3">
                <label className="fw-bold">Coverage End Date</label>
                <p className="mb-0">{new Date(policy.coverageEndDate).toLocaleDateString()}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PolicyDetail;
