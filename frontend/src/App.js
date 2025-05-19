import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState({
    title: '',
    description: '',
    category: '',
    location: '',
    dateLostFound: '',
    contactInfo: '',
    status: 'lost',
  });
  const [searchKeyword, setSearchKeyword] = useState('');

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/items');
      setItems(res.data);
    } catch (error) {
      console.error('Error fetching items:', error);
    }
  };

  const handleInputChange = (e) => {
    setForm({...form, [e.target.name]: e.target.value});
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/items', form);
      setForm({
        title: '',
        description: '',
        category: '',
        location: '',
        dateLostFound: '',
        contactInfo: '',
        status: 'lost',
      });
      fetchItems();
    } catch (error) {
      console.error('Error adding item:', error);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchKeyword) {
      fetchItems();
      return;
    }
    try {
      const res = await axios.get(`http://localhost:8080/api/items/search?keyword=${searchKeyword}`);
      setItems(res.data);
    } catch (error) {
      console.error('Error searching items:', error);
    }
  };

  return (
    <div className="App">
      <h1>Strathmore University Lost and Found</h1>

      <form onSubmit={handleSubmit} className="item-form">
        <h2>Add Lost/Found Item</h2>
        <input type="text" name="title" placeholder="Title" value={form.title} onChange={handleInputChange} required />
        <textarea name="description" placeholder="Description" value={form.description} onChange={handleInputChange} required />
        <input type="text" name="category" placeholder="Category" value={form.category} onChange={handleInputChange} />
        <input type="text" name="location" placeholder="Location" value={form.location} onChange={handleInputChange} />
        <input type="date" name="dateLostFound" placeholder="Date Lost/Found" value={form.dateLostFound} onChange={handleInputChange} />
        <input type="text" name="contactInfo" placeholder="Contact Info" value={form.contactInfo} onChange={handleInputChange} />
        <select name="status" value={form.status} onChange={handleInputChange}>
          <option value="lost">Lost</option>
          <option value="found">Found</option>
        </select>
        <button type="submit">Add Item</button>
      </form>

      <form onSubmit={handleSearch} className="search-form">
        <input type="text" placeholder="Search items..." value={searchKeyword} onChange={(e) => setSearchKeyword(e.target.value)} />
        <button type="submit">Search</button>
        <button type="button" onClick={() => { setSearchKeyword(''); fetchItems(); }}>Clear</button>
      </form>

      <div className="item-list">
        <h2>Lost and Found Items</h2>
        {items.length === 0 ? (
          <p>No items found.</p>
        ) : (
          <ul>
            {items.map(item => (
              <li key={item.id} className={item.status === 'lost' ? 'lost' : 'found'}>
                <h3>{item.title} ({item.status})</h3>
                <p><strong>Description:</strong> {item.description}</p>
                <p><strong>Category:</strong> {item.category}</p>
                <p><strong>Location:</strong> {item.location}</p>
                <p><strong>Date:</strong> {item.dateLostFound}</p>
                <p><strong>Contact:</strong> {item.contactInfo}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default App;
