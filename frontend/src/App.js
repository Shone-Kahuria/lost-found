import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import axios from 'axios';
import Login from './Login';
import Signup from './Signup';
import './App.css';

function LostFoundApp({ user, onLogout }) {
  const [view, setView] = useState('lost'); // 'lost', 'found', 'report'
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    category: '',
    location: '',
    dateLostFound: '',
    contactInfo: '',
    status: 'lost', // or 'found'
  });

  // Set axios default Authorization header with token
  useEffect(() => {
    if (user && user.token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${user.token}`;
    } else {
      delete axios.defaults.headers.common['Authorization'];
    }
  }, [user]);

  const fetchItems = async (status) => {
    setLoading(true);
    setError('');
    try {
      const response = await axios.get(`http://localhost:8080/api/items/status/${status}`);
      setItems(response.data);
    } catch (err) {
      if (err.response && err.response.data) {
        setError(typeof err.response.data === 'string' ? err.response.data : JSON.stringify(err.response.data));
      } else {
        setError('Failed to fetch items');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (view === 'lost' || view === 'found') {
      fetchItems(view);
    }
  }, [view]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleReportSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await axios.post('http://localhost:8080/api/items', formData);
      alert('Item reported successfully');
      setFormData({
        title: '',
        description: '',
        category: '',
        location: '',
        dateLostFound: '',
        contactInfo: '',
        status: 'lost',
      });
      setView('lost');
      fetchItems('lost');
    } catch (err) {
      if (err.response && err.response.data) {
        setError(typeof err.response.data === 'string' ? err.response.data : JSON.stringify(err.response.data));
      } else {
        setError('Failed to report item');
      }
    }
  };

  const handleClaim = async (id) => {
    setError('');
    try {
      const item = items.find(i => i.id === id);
      if (!item) return;
      const updatedItem = { ...item, status: 'claimed' };
      await axios.put(`http://localhost:8080/api/items/${id}`, updatedItem);
      alert('Item claimed successfully');
      fetchItems(view);
    } catch (err) {
      setError('Failed to claim item');
    }
  };

  return (
    <div className="lostfound-container">
      <h2>Welcome, {user.username}</h2>
      <button className="logout-button" onClick={onLogout}>Logout</button>
      <nav className="nav-buttons">
        <button className="nav-button" onClick={() => setView('lost')}>Lost Items</button>
        <button className="nav-button" onClick={() => setView('found')}>Found Items</button>
        <button className="nav-button" onClick={() => setView('report')}>Report Item</button>
      </nav>
      {error && <p className="error-message">{error}</p>}
      {view === 'lost' || view === 'found' ? (
        loading ? (
          <p>Loading...</p>
        ) : (
          <ul className="item-list">
            {items.length === 0 && <li>No items found.</li>}
            {items.map(item => (
              <li key={item.id} className="item-card">
                <h3>{item.title} ({item.status})</h3>
                <p>{item.description}</p>
                <p><strong>Category:</strong> {item.category}</p>
                <p><strong>Location:</strong> {item.location}</p>
                <p><strong>Date:</strong> {item.dateLostFound}</p>
                <p><strong>Contact:</strong> {item.contactInfo}</p>
                {item.status !== 'claimed' && view === 'found' && (
                  <button className="claim-button" onClick={() => handleClaim(item.id)}>Claim</button>
                )}
              </li>
            ))}
          </ul>
        )
      ) : (
        <form className="report-form" onSubmit={handleReportSubmit}>
          <h3>Report Lost or Found Item</h3>
          <label>
            Title:
            <input className="input-field" name="title" value={formData.title} onChange={handleInputChange} required />
          </label>
          <br />
          <label>
            Description:
            <textarea className="input-field" name="description" value={formData.description} onChange={handleInputChange} required />
          </label>
          <br />
          <label>
            Category:
            <input className="input-field" name="category" value={formData.category} onChange={handleInputChange} required />
          </label>
          <br />
          <label>
            Location:
            <input className="input-field" name="location" value={formData.location} onChange={handleInputChange} required />
          </label>
          <br />
          <label>
            Date Lost/Found:
            <input className="input-field" type="date" name="dateLostFound" value={formData.dateLostFound} onChange={handleInputChange} required />
          </label>
          <br />
          <label>
            Contact Info:
            <input className="input-field" name="contactInfo" value={formData.contactInfo} onChange={handleInputChange} required />
          </label>
          <br />
          <label>
            Status:
            <select className="input-field" name="status" value={formData.status} onChange={handleInputChange}>
              <option value="lost">Lost</option>
              <option value="found">Found</option>
            </select>
          </label>
          <br />
          <button className="submit-button" type="submit">Submit</button>
        </form>
      )}
    </div>
  );
}

function App() {
  const [user, setUser] = useState(null);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
  };

  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/login" element={!user ? <Login onLogin={handleLogin} /> : <Navigate to="/" />} />
          <Route path="/signup" element={!user ? <Signup /> : <Navigate to="/" />} />
          <Route path="/" element={user ? <LostFoundApp user={user} onLogout={handleLogout} /> : <Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
