import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { createPolicy, getPolicy, updatePolicy } from "../api/api";

const PolicyForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEdit = Boolean(id);

  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [form, setForm] = useState({
    name: "",
    status: "ACTIVE",
    coverageStartDate: "",
    coverageEndDate: "",
  });
  const [formErrors, setFormErrors] = useState({});

  useEffect(() => {
    if (isEdit) {
      const fetchPolicy = async () => {
        try {
          const res = await getPolicy(id);
          const data = res.data || {};
          setForm({
            name: data.name || data.name || "",
            status: data.status || data.status || "ACTIVE",
            coverageStartDate: data.coverageStartDate ? data.coverageStartDate.split('T')[0] : "",
            coverageEndDate: data.coverageEndDate ? data.coverageEndDate.split('T')[0] : "",
          });
          setLoading(false);
        } catch (err) {
          setError("Failed to fetch policy details. Please try again later.");
          setLoading(false);
        }
      };
      fetchPolicy();
    }
  }, [isEdit, id]);

  const validate = () => {
    const errors = {};
    
    if (!form.name || !form.name.toString().trim()) {
      errors.name = "Policy name is required";
    }
    if (!form.coverageStartDate) {
      errors.coverageStartDate = "Coverage start date is required";
    }
    if (!form.coverageEndDate) {
      errors.coverageEndDate = "Coverage end date is required";
    } else if (new Date(form.coverageEndDate) <= new Date(form.coverageStartDate)) {
      errors.coverageEndDate = "End date must be after start date";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name.includes(".")) {
      const [parent, child] = name.split(".");
      setForm((prev) => ({
        ...prev,
        [parent]: {
          ...(typeof prev[parent] === 'object' ? prev[parent] : {}),
          [child]: value,
        },
      }));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validate()) {
      return;
    }

    // send the normalized payload expected by backend
    const formData = {
      name: form.name,
      status: form.status,
      coverageStartDate: form.coverageStartDate,
      coverageEndDate: form.coverageEndDate,
    };

    try {
      setSubmitting(true);
      setError(null);
      
      if (isEdit) {
        await updatePolicy(id, formData);
      } else {
        await createPolicy(formData);
      }
      
      navigate("/policies", {
        state: { 
          alert: { 
            type: "success", 
            message: `Policy ${isEdit ? "updated" : "created"} successfully`
          }
        }
      });
    } catch (err) {
      setError(`Failed to ${isEdit ? "update" : "create"} policy. Please try again.`);
      setSubmitting(false);
    }
  };

  if (loading) {
    return <div className="text-center mt-5"><div className="spinner-border" role="status"></div></div>;
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>{isEdit ? "Edit Policy" : "Create New Policy"}</h2>
        <Link to="/policies" className="btn btn-outline-secondary">
          <i className="bi bi-x-lg"></i> Cancel
        </Link>
      </div>

      {error && <div className="alert alert-danger mb-4">{error}</div>}

      <div className="card">
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="row">
              <div className="col-md-6 mb-3">
                <label className="form-label">Policy Name</label>
                <input
                  type="text"
                  className={`form-control ${formErrors.name ? 'is-invalid' : ''}`}
                  name="name"
                  value={form.name}
                  onChange={handleChange}
                  required
                />
                {formErrors.name && <div className="invalid-feedback">{formErrors.name}</div>}
              </div>

              <div className="col-md-6 mb-3">
                <label className="form-label">Status</label>
                <select 
                  className="form-select"
                  name="status"
                  value={form.status}
                  onChange={handleChange}
                >
                  <option value="ACTIVE">Active</option>
                  <option value="INACTIVE">Inactive</option>
                </select>
              </div>
            </div>

            <div className="row">
              <div className="col-md-6 mb-3">
                <label className="form-label">Coverage Start Date</label>
                <input
                  type="date"
                  className={`form-control ${formErrors.coverageStartDate ? 'is-invalid' : ''}`}
                  name="coverageStartDate"
                  value={form.coverageStartDate}
                  onChange={handleChange}
                  required
                />
                {formErrors.coverageStartDate && <div className="invalid-feedback">{formErrors.coverageStartDate}</div>}
              </div>

              <div className="col-md-6 mb-3">
                <label className="form-label">Coverage End Date</label>
                <input
                  type="date"
                  className={`form-control ${formErrors.coverageEndDate ? 'is-invalid' : ''}`}
                  name="coverageEndDate"
                  value={form.coverageEndDate}
                  onChange={handleChange}
                  required
                />
                {formErrors.coverageEndDate && <div className="invalid-feedback">{formErrors.coverageEndDate}</div>}
              </div>
            </div>

            <div className="d-flex justify-content-end mt-4">
              <Link to="/policies" className="btn btn-outline-secondary me-2">Cancel</Link>
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={submitting}
              >
                {submitting ? (
                  <><span className="spinner-border spinner-border-sm me-1" /> {isEdit ? "Updating..." : "Creating..."}</>
                ) : (
                  <>{isEdit ? "Update Policy" : "Create Policy"}</>
                )}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PolicyForm;
