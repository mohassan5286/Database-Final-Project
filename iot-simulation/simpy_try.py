import simpy
import random
import mysql.connector
from mysql.connector import Error

# Configuration
DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "yourpassword",
    "database": "parking_system",
}
STATUS = ["available", "occupied"]
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
        cursor.execute("SELECT lot_name, spot_id, status FROM parking_spots")
        results = cursor.fetchall()
        parking_lots = {}
        for row in results:
            lot = row['lot_name']
            spot = {
                'id': row['spot_id'],
                'status': row['status']
            }
            if lot not in parking_lots:
                parking_lots[lot] = []
            parking_lots[lot].append(spot)
        return parking_lots
    except Error as e:
        print(f"Error fetching data from MySQL: {e}")
        return {}
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def update_parking_spot(lot, spot, status):
    """
    Update the parking spot status in the database.
    """
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        cursor = connection.cursor()
        query = "UPDATE parking_spots SET status = %s WHERE lot_name = %s AND spot_name = %s"
        cursor.execute(query, (status, lot, spot))
        connection.commit()
        print(f"Updated {lot} - {spot} to {status} in database.")
    except Error as e:
        print(f"Error updating data in MySQL: {e}")
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def parking_spot_simulator(env, lot_name, spot_name):
    """
    Simulates the status of a parking spot in a given lot.
    """
    while True:
        # Update status
        new_status = random.choice(STATUS)
        timestamp = env.now  # Simulation time
        print(f"Time {timestamp}: {lot_name} - {spot_name} is now {new_status}")
        
        # Update the database
        update_parking_spot(lot_name, spot_name, new_status)
        
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
    for lot, spots in parking_lots.items():
        for spot in spots:
            env.process(parking_spot_simulator(env, lot, spot))
    
    # Run the simulation for the specified time
    env.run(until=SIMULATION_TIME)


if __name__ == "__main__":
    run_simulation()
