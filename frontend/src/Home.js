import React, { useState, useEffect } from 'react';
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

  const [parkingLots, setParkingLots] = useState([]);
  
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

    const lot = parkingLots.find(lot => lot.spots.some(spot => spot.spot_id === selectedSpot.spot_id));
    const reservationDetails = {
        Arrived: "No",
        StartTime: startTime,
        EndTime: endTime,
        User_idUser: userId,
        User_Admin_AdminID: lot.admin_id,
        ParkingSpot_SpotID: selectedSpot.spot_id,
        ParkingSpot_ParkingLot_LotID: lot.lot_id,
        ParkingSpot_ParkingLot_Admin_AdminID: lot.admin_id,
        location: lot.location
    };

    axios.post('http://localhost:8081/reserveSpot', reservationDetails)
        .then(response => {
            console.log("Reservation successful:", response.data);
            setMessage("Your reservation is successful and all your debt is paid automatically.");
            setTimeout(() => setMessage(""), 3000);
        })
        .catch(error => {
            console.error("Error making reservation:", error);
        });

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
    .post('http://localhost:8081/arrive', { spotId, userId })
    .then((response) => {
      console.log("Arrived successfully:", response.data);
      setMessage("Thank you! You have successfully paid for your reservation.");
      setTimeout(() => setMessage(""), 3000);
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
  };

  const filteredParkingLots = parkingLots.filter(lot =>
    lot.location.toLowerCase().includes(searchQuery.toLowerCase())
  );

  useEffect(() => {
    fetchAndSetUserInfo(userId, setUsername, setEmail, setPlateNumber);
  }, [userId]);

  useEffect(() => {
    const fetchParkingLots = async () => {
        try {
            const response = await axios.get('http://localhost:8081/parkingLots');
            setParkingLots(response.data);
        } catch (err) {
            console.log(err, 'Error fetching parking lots');
        }
    };

    fetchParkingLots();
}, []);

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
              <Typography variant="body2" color="textSecondary">
                <strong>Directions:</strong> {lot.directions}
              </Typography>
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