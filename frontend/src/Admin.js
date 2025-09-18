import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Button,
  Card,
  CardContent,
  Typography,
  AppBar,
  Toolbar,
} from "@mui/material";
import "./Admin.css";

const Admin = () => {
  const [users, setUsers] = useState([]);
  const defaultFolderPath = "http://localhost:8080/";
  const defaultFileName = "Lecture 4.1 - Network Layer.pdf";

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch("http://localhost:8081/getAllUsers");
        const data = await response.json();
        setUsers(data);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };
    fetchUsers();
  }, []);

  const handleRemoveUser = async (userId) => {
    setUsers(users.filter((user) => user.user_id !== userId));
    try { 
      const response = await axios.post(
        `http://localhost:8081/removeUser/${userId}`
      );

      if (response.status !== 200) {
        console.error("Error removing user");
      }
    } catch (error) {
      console.error("Error removing user:", error);
    }
  };

  const openFile = (fileName) => {
    const filePath = `${defaultFolderPath}${fileName}`;
    window.open(filePath, "_blank");
  };

  const fetchOccupancyRates = () => {
    axios
      .get("http://localhost:8081/reports/get/occupancyRates")
      .then((response) => {
        const fileName = response.data || defaultFileName;
        openFile(fileName);
      })
      .catch((error) => {
        console.error("Error fetching occupancy rates:", error);
        openFile(defaultFileName);
      });
  };

  const fetchTopUsersReport = () => {
    axios
      .get("http://localhost:8081/reports/get/topUsersReport")
      .then((response) => {
        const fileName = response.data || defaultFileName;
        openFile(fileName);
      })
      .catch((error) => {
        console.error("Error fetching top users report:", error);
        openFile(defaultFileName);
      });
  };

  const fetchTopParkingLotsReport = () => {
    axios
      .get("http://localhost:8081/reports/get/topParkingLotsReport")
      .then((response) => {
        const fileName = response.data || defaultFileName;
        openFile(fileName);
      })
      .catch((error) => {
        console.error("Error fetching top parking lots report:", error);
        openFile(defaultFileName);
      });
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
          {users.map((user) => (
            <Card key={user.user_id} className="user-card">
              <CardContent>
                <Typography variant="h6">{user.name}</Typography>
                <Typography variant="body2">Email: {user.email}</Typography>
                <Typography variant="body2">
                  Plate Number: {user.plate_number}
                </Typography>
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={() => handleRemoveUser(user.user_id)}
                >
                  Remove User
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
        <div className="report-section">
          <Button
            variant="contained"
            color="primary"
            style={{ margin: "20px" }}
            onClick={fetchOccupancyRates}
          >
            Generate Dashboard
          </Button>
          <Button
            variant="contained"
            color="primary"
            style={{ margin: "20px" }}
            onClick={fetchTopUsersReport}
          >
            Generate Top Users
          </Button>
          <Button
            variant="contained"
            color="primary"
            style={{ margin: "20px" }}
            onClick={fetchTopParkingLotsReport}
          >
            Generate Top Lots
          </Button>
        </div>
      </div>
    </>
  );
};

export default Admin;
