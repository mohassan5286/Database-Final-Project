import React, { useState, useEffect } from 'react';
import { Button, Card, CardContent, Typography, AppBar, Toolbar } from '@mui/material';
import './Admin.css';

const Admin = () => {
  const [users, setUsers] = useState([
    {
      "user_id": 1,
      "name": "John Doe",
      "plate_number": "ABC123",
      "email": "john.doe@example.com",
      "password": "password123"
    },
    {
      "user_id": 2,
      "name": "Jane Smith",
      "plate_number": "XYZ789",
      "email": "jane.smith@example.com",
      "password": "securepass456"
    },
    {
      "user_id": 3,
      "name": "Alice Johnson",
      "plate_number": "LMN456",
      "email": "alice.johnson@example.com",
      "password": "mypassword789"
    },
    {
      "user_id": 4,
      "name": "Bob Brown",
      "plate_number": "QRS321",
      "email": "bob.brown@example.com",
      "password": "pass1234"
    },
    {
      "user_id": 5,
      "name": "Charlie Davis",
      "plate_number": "TUV654",
      "email": "charlie.davis@example.com",
      "password": "charliepass"
    }
  ]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch('http://localhost:8081/getAllUsers');
        const data = await response.json();
        setUsers(data);
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    };
    fetchUsers();
  }, []);

  const handleRemoveUser = async (userId) => {
    setUsers(users.filter(user => user.user_id !== userId));
    try {
      const response = await fetch(`http://localhost:8081/removeUser/${userId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        console.error('Error removing user');
      }
    } catch (error) {
      console.error('Error removing user:', error);
    }
  };

  const handleGenerateReport = async () => {
    try {
      const response = await fetch('http://localhost:8081/generateReport', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({}),
      });

      if (response.ok) {
        console.log('Report generation initiated');
      } else {
        console.error('Error generating report');
      }
    } catch (error) {
      console.error('Error generating report:', error);
    }
  };

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" style={{ flexGrow: 1 }}>
            Admin Panel
          </Typography>
        </Toolbar>
      </AppBar>
      <div className="admin-container">
        <div className="user-list">
          {users.map(user => (
            <Card key={user.user_id} className="user-card">
              <CardContent>
                <Typography variant="h6">{user.name}</Typography>
                <Typography variant="body2">Email: {user.email}</Typography>
                <Typography variant="body2">Plate Number: {user.plate_number}</Typography>
                <Button variant="contained" color="secondary" onClick={() => handleRemoveUser(user.user_id)}>
                  Remove User
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
        <div className="report-section">
          <Button variant="contained" color="primary" onClick={handleGenerateReport}>
            Generate Report
          </Button>
        </div>
      </div>
    </>
  );
};

export default Admin;