import simpy
import random
import mysql.connector
from mysql.connector import Error

# Configuration
DB_CONFIG = {
    "host": "localhost",
    "user": "admin",
    "password": "admin",
    "database": "ParkingSystemSchema",
}
STATUS = ["Available", "Occupied"]
UPDATE_INTERVAL = (2, 5)  # Time in seconds for updates
SIMULATION_TIME = 30  # Total simulation time in seconds


def fetch_parking_data():
    """
    Fetch initial parking lot and spot data from the database.
    Returns a dictionary of parking lots and their spots.
    """
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        cursor = connection.cursor(dictionary=True)
        query = """
        SELECT pl.LotID, ps.SpotID, ps.Status
        FROM ParkingLot pl
        JOIN ParkingSpot ps ON pl.LotID = ps.ParkingLot_LotID 
            AND pl.Admin_AdminID = ps.ParkingLot_Admin_AdminID
        """
        cursor.execute(query)
        results = cursor.fetchall()
        
        parking_lots = {}
        for row in results:
            lot_id = row['LotID']
            spot = {
                'id': row['SpotID'],
                'status': row['Status']
            }
            if lot_id not in parking_lots:
                parking_lots[lot_id] = []
            parking_lots[lot_id].append(spot)
        return parking_lots
    except Error as e:
        print(f"Error fetching data from MySQL: {e}")
        return {}
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def update_parking_spot(lot_id, spot_id, status):
    """
    Update the parking spot status in the database.
    """
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        cursor = connection.cursor()
        query = """
        UPDATE ParkingSpot
        SET Status = %s
        WHERE ParkingLot_LotID = %s AND ParkingLot_Admin_AdminID = (SELECT Admin_AdminID FROM ParkingLot WHERE LotID = %s)
        AND SpotID = %s
        """
        cursor.execute(query, (status, lot_id, lot_id, spot_id))
        connection.commit()
        print(f"Updated Lot {lot_id} - Spot {spot_id} to {status} in database.")
    except Error as e:
        print(f"Error updating data in MySQL: {e}")
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def parking_spot_simulator(env, lot_id, spot_id):
    """
    Simulates the status of a parking spot in a given lot.
    """
    while True:
        # Update status
        new_status = random.choice(STATUS)
        timestamp = env.now  # Simulation time
        print(f"Time {timestamp}: Lot {lot_id} - Spot {spot_id} is now {new_status}")
        
        # Update the database
        update_parking_spot(lot_id, spot_id, new_status)
        
        # Wait for the next update
        yield env.timeout(random.randint(*UPDATE_INTERVAL))


def run_simulation():
    """
    Runs the simulation environment.
    """
    # Fetch initial parking data from the database
    parking_lots = fetch_parking_data()
    if not parking_lots:
        print("No parking data available. Exiting simulation.")
        return
    
    env = simpy.Environment()
    
    # Create a parking spot process for each spot in each lot
    for lot_id, spots in parking_lots.items():
        for spot in spots:
            env.process(parking_spot_simulator(env, lot_id, spot['id']))
    
    # Run the simulation for the specified time
    env.run(until=SIMULATION_TIME)


if __name__ == "__main__":
    run_simulation()
