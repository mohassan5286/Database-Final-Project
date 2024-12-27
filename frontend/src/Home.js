import React, { useState } from 'react';
import axios from 'axios';
import './Home.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Card, CardContent, Typography, Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from '@mui/material';

function fetchAndSetUserInfo(userId, setUsername, setEmail, setPlateNumber) {
  axios
    .get(`http://localhost:8081/getUserInformation/${userId}`)
    .then((response) => {
        const { name, email, plateNumber } = response.data;

        if (name) {
            setUsername(name);
            localStorage.setItem("name", name);
        }

        if (email) {
            setEmail(email);
            localStorage.setItem("email", email);
        }

        if (plateNumber) {
            setPlateNumber(plateNumber);
            localStorage.setItem("plateNumber", plateNumber);
        }
    })
    .catch((error) => {
        console.error("Error fetching user information:", error);
    });
}

function Home({ userId }) {
  const [isCardVisible, setIsCardVisible] = useState(false);
  const [openLotId, setOpenLotId] = useState(null);
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [reservedSpots, setReservedSpots] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [name, setUsername] = useState(localStorage.getItem("name") || "Muhammad");
  const [email, setEmail] = useState(localStorage.getItem("email") || "mohassan5286@gmail.com");
  const [plateNumber, setPlateNumber] = useState(localStorage.getItem("plateNumber") || "ABC123");
  const [message, setMessage] = useState("");

  const [parkingLots, setParkingLots] = useState([
    {
      "lot_id": 1,
      "location": "123 Main St",
      "spots": [
        {
          "spot_id": 101,
          "spot_type": "Regular",
          "status": "Available",
          "user_id": null
        },
        {
          "spot_id": 102,
          "spot_type": "Disabled",
          "status": "Reserved",
          "user_id": 1 
        },
        {
          "spot_id": 103,
          "spot_type": "EV Charging",
          "status": "Occupied",
          "user_id": 2
        }
      ]
    },
    {
      "lot_id": 2,
      "location": "456 Elm St",
      "spots": [
        {
          "spot_id": 201,
          "spot_type": "Regular",
          "status": "Occupied",
          "user_id": 3
        },
        {
          "spot_id": 202,
          "spot_type": "Regular",
          "status": "Available",
          "user_id": null
        },
        {
          "spot_id": 203,
          "spot_type": "EV Charging",
          "status": "Reserved",
          "user_id": 4 
        }
      ]
    },
    {
      "lot_id": 3,
      "location": "789 Oak St",
      "spots": [
        {
          "spot_id": 301,
          "spot_type": "Regular",
          "status": "Available",
          "user_id": null
        },
        {
          "spot_id": 302,
          "spot_type": "Disabled",
          "status": "Occupied",
          "user_id": 5 
        },
        {
          "spot_id": 303,
          "spot_type": "EV Charging",
          "status": "Available",
          "user_id": null
        }
      ]
    },
    {
      "lot_id": 4,
      "location": "101 Pine St",
      "spots": [
        {
          "spot_id": 401,
          "spot_type": "Regular",
          "status": "Reserved",
          "user_id": 6
        },
        {
          "spot_id": 402,
          "spot_type": "Regular",
          "status": "Occupied",
          "user_id": 7
        },
        {
          "spot_id": 403,
          "spot_type": "Disabled",
          "status": "Available",
          "user_id": null
        }
      ]
    },
    {
      "lot_id": 5,
      "location": "202 Maple St",
      "spots": [
        {
          "spot_id": 501,
          "spot_type": "Regular",
          "status": "Available",
          "user_id": null
        },
        {
          "spot_id": 502,
          "spot_type": "EV Charging",
          "status": "Reserved",
          "user_id": 8
        },
        {
          "spot_id": 503,
          "spot_type": "Disabled",
          "status": "Occupied",
          "user_id": 9
        }
      ]
    }
  ]);
  
  const handleClickOpen = (spot) => {
    setSelectedSpot(spot);
    setOpenLotId(true);
  };

  const handleClose = () => {
    setOpenLotId(false);
    setSelectedSpot(null);
  };

  const handleReserve = () => {
    if (!selectedSpot) return;

    setParkingLots((prevLots) =>
      prevLots.map((lot) => ({
        ...lot,
        spots: lot.spots.map((spot) =>
          spot.spot_id === selectedSpot.spot_id
            ? { ...spot, status: "Reserved", user_id: userId }
            : spot
        ),
      }))
    );
    setReservedSpots((prev) => [...prev, selectedSpot.spot_id]);
    handleClose();
  };

  const handleArrive = (spotId) => {
    axios
    .post('http://localhost:8081/arrive', { spotId, message: "yes" })
    .then((response) => {
      console.log("Arrived successfully:", response.data);
    })
    .catch((error) => {
      console.error("Error arriving:", error);
    });

    setParkingLots((prevLots) =>
      prevLots.map((lot) => ({
        ...lot,
        spots: lot.spots.map((spot) =>
          spot.spot_id === spotId ? { ...spot, status: "Occupied" } : spot
        ),
      }))
    );
    setReservedSpots((prev) => prev.filter((id) => id !== spotId));
    setMessage("Thank you! You have successfully paid for your reservation and cleared all outstanding dues.");
    setTimeout(() => setMessage(""), 3000);
  };

  const filteredParkingLots = parkingLots.filter(lot =>
    lot.location.toLowerCase().includes(searchQuery.toLowerCase())
  );

  fetchAndSetUserInfo(userId, setUsername, setEmail, setPlateNumber);

  return (
    <>
      {message && (
        <>
          <div className="blur-overlay"></div>
          <div className="arrival-message">
            {message}
          </div>
        </>
      )}
      <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <div className="container-fluid">
          <span className="website-title">SmartPark</span>
          <div className="d-flex ms-auto me-3">
            <input
              className="form-control me-2 search-field"
              type="search"
              placeholder="Search"
              aria-label="Search"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button className="btn btn-outline-success search-button" type="submit">
              Search
            </button>
          </div>
          <span className="user-link" onClick={() => setIsCardVisible(true)}>
            {name}
          </span>
        </div>
      </nav>

      {isCardVisible && (
        <div className="user-card-overlay">
          <div className="user-card">
            <button className="close-button" onClick={() => setIsCardVisible(false)}>
              &times;
            </button>
            <h3>User Information</h3>
            <p><strong>Name:</strong> {name}</p>
            <p><strong>Email:</strong> {email}</p>
            <p><strong>Plate Number:</strong> {plateNumber}</p>
          </div>
        </div>
      )}

      <div className="parking-lot-cards">
        {filteredParkingLots.map((lot) => (
          <Card key={lot.lot_id}>
            <CardContent>
              <Typography variant="h5">{lot.location}</Typography>
              <Button onClick={() => setOpenLotId(openLotId === lot.lot_id ? null : lot.lot_id)}>
                Show Spots
              </Button>
              {openLotId === lot.lot_id &&
                lot.spots.map((spot) => (
                  <div key={spot.spot_id} className={`spot-card spot-${spot.status.toLowerCase()}`}>
                    <Typography>
                      <strong>Type:</strong> {spot.spot_type}, <strong>Status:</strong> {spot.status}, <strong>Price:</strong> ${spot.price}
                    </Typography>
                    {spot.status === "Available" && (
                      <Button onClick={() => handleClickOpen(spot)}>Reserve</Button>
                    )}
                    {spot.status === "Reserved" && spot.user_id === userId && (
                      <Button onClick={() => handleArrive(spot.spot_id)}>Arrive</Button>
                    )}
                  </div>
                ))}
            </CardContent>
          </Card>
        ))}
      </div>

      <Dialog open={!!selectedSpot} onClose={handleClose}>
        <DialogTitle>Reserve Spot</DialogTitle>
        <DialogContent>
          <TextField
            label="Start Time"
            type="datetime-local"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            fullWidth
            margin="normal"
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            label="End Time"
            type="datetime-local"
            value={endTime}
            onChange={(e) => setEndTime(e.target.value)}
            fullWidth
            margin="normal"
            InputLabelProps={{ shrink: true }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleReserve}>Reserve</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}

export default Home;