import React, { useState } from 'react';
import axios from 'axios';

function Login({ onLogin }) {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState(''); // for signup
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password,
      });
      const token = response.data;
      onLogin({ token, username });
    } catch (err) {
      setError('Invalid username or password');
    }
  };

  const handleSignupSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      const response = await axios.post('http://localhost:8080/api/auth/register', {
        username,
        email,
        password,
      });
      setSuccess('Registration successful! You can now log in.');
      setIsLogin(true);
      setUsername('');
      setEmail('');
      setPassword('');
    } catch (err) {
      setError(err.response?.data || 'Registration failed');
    }
  };

  return (
    <div className="login-container">
      {isLogin ? (
        <>
          <h2>Login</h2>
          <form onSubmit={handleLoginSubmit} className="login-form">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <button type="submit">Login</button>
            {error && <p className="error">{error}</p>}
          </form>
          <p>
            Don't have an account?{' '}
            <button className="link-button" onClick={() => { setIsLogin(false); setError(''); setSuccess(''); }}>
              Sign Up
            </button>
          </p>
        </>
      ) : (
        <>
          <h2>Sign Up</h2>
          <form onSubmit={handleSignupSubmit} className="login-form">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <button type="submit">Sign Up</button>
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
          </form>
          <p>
            Already have an account?{' '}
            <button className="link-button" onClick={() => { setIsLogin(true); setError(''); setSuccess(''); }}>
              Login
            </button>
          </p>
        </>
      )}
    </div>
  );
}

export default Login;
