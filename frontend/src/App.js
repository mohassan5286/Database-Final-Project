import './App.css';
import Login from './Login';
import Home from './Home';
import Admin from './Admin';
import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation } from 'react-router-dom';

function App({ userId }) {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!userId) {
      navigate('/', { replace: true });
    }
  }, [userId, location, navigate]);

  return (
    <div>
      <Home userId={userId}/>
    </div>
  );
}

function AppWrapper() {
  const [userId, setUserId] = useState(localStorage.getItem("userId") || 1);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login setUserId={setUserId} />} />
        <Route path="/home" element={<App userId={userId} />} />
        <Route path="/admin" element={<Admin />} />
      </Routes>
    </Router>
  );
}

export default AppWrapper;