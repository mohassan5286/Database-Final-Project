import simpy
import random

# Configuration
STATUS = ["available", "occupied"]
UPDATE_INTERVAL = (2, 5)  # Time in seconds for updates
SIMULATION_TIME = 30  # Total simulation time in seconds

# Mock dataset
results = [
    {"lot_name": "A", "spot_id": "A1", "status": "available"},
    {"lot_name": "A", "spot_id": "A2", "status": "occupied"},
    {"lot_name": "B", "spot_id": "B1", "status": "available"},
    {"lot_name": "B", "spot_id": "B2", "status": "occupied"},
]

def fetch_parking_data():
    """
    Fetch parking lot and spot data from the mock dataset.
    Returns a dictionary of parking lots and their spots with status.
    """
    parking_lots = {}
    for row in results:
        lot = row['lot_name']
        spot = {"spot_id": row['spot_id'], "status": row['status']}
        if lot not in parking_lots:
            parking_lots[lot] = []
        parking_lots[lot].append(spot)
    return parking_lots

def update_parking_spot(lot, spot_id, status):
    """
    Update the parking spot status in the mock dataset.
    """
    for row in results:
        if row['lot_name'] == lot and row['spot_id'] == spot_id:
            row['status'] = status
            # print(f"Updated {lot} - {spot_id} to {status} in mock dataset.")
            break

def parking_spot_simulator(env, lot_name, spot):
    """
    Simulates the status of a parking spot in a given lot.
    """
    spot_id = spot['spot_id']
    while True:
        # Update status
        new_status = random.choice(STATUS)
        timestamp = env.now  # Simulation time
        print(f"Time {timestamp}: {lot_name} - {spot_id} is now {new_status}")

        # Update the mock dataset
        update_parking_spot(lot_name, spot_id, new_status)

        # Wait for the next update
        yield env.timeout(random.randint(*UPDATE_INTERVAL))

def run_simulation():
    """
    Runs the simulation environment.
    """
    # Fetch initial parking data
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
