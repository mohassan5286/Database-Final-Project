import simpy
import random
import requests

# Configuration
PARKING_LOTS = {
    "Lot1": ["Spot1", "Spot2", "Spot3"],
    "Lot2": ["Spot1", "Spot2"],
}
STATUS = ["available", "occupied"]
UPDATE_INTERVAL = (2, 5)  # Time in seconds for updates
SIMULATION_TIME = 30  # Total simulation time in seconds
CENTRAL_SERVER_API = "http://localhost:5000/update_status"  # Replace with actual API endpoint


def parking_spot_simulator(env, lot_name, spot_name):
    """
    Simulates the status of a parking spot in a given lot.
    """
    while True:
        # Update status
        new_status = random.choice(STATUS)
        timestamp = env.now  # Simulation time
        print(f"Time {timestamp}: {lot_name} - {spot_name} is now {new_status}")
        
        # Send update to the server
        send_update_to_server(lot_name, spot_name, new_status, timestamp)
        
        # Wait for the next update
        yield env.timeout(random.randint(*UPDATE_INTERVAL))


def send_update_to_server(lot, spot, status, timestamp):
    """
    Sends a simulated update to the central server.
    """
    data = {
        "parking_lot": lot,
        "spot": spot,
        "status": status,
        "timestamp": timestamp,
    }
    
    try:
        response = requests.post(CENTRAL_SERVER_API, json=data)
        if response.status_code == 200:
            print(f"Server acknowledged update for {lot} - {spot}: {status}")
        else:
            print(f"Failed to update server for {lot} - {spot}. Status code: {response.status_code}")
    except Exception as e:
        print(f"Error sending update to server: {e}")


def run_simulation():
    """
    Runs the simulation environment.
    """
    import simpy
    env = simpy.Environment()
    
    # Create a parking spot process for each spot in each lot
    for lot, spots in PARKING_LOTS.items():
        for spot in spots:
            env.process(parking_spot_simulator(env, lot, spot))
    
    # Run the simulation for the specified time
    env.run(until=SIMULATION_TIME)


if __name__ == "__main__":
    run_simulation()
