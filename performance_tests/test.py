from locust import HttpUser, task, between, events
import logging
import requests
import json
from locust.runners import MasterRunner, WorkerRunner

logging.basicConfig()
logging.getLogger().setLevel(logging.INFO)

with open('/mnt/locust/config.json', 'r') as file:
    config = json.load(file)

auth_url = "https://trippidy.eu.auth0.com/oauth/token"
client_id = "mifCdLty8BuVnD5w9qHyE70QP6XO6x01"
client_secret = config['client_secret']

username = "test@example.com"
password = "Diplomka123"

USER_CREDENTIALS = None
    
# Function to check node type
def on_test_start(environment, **kwargs):
    global USER_CREDENTIALS  # Declare USER_CREDENTIALS as global
    
    if isinstance(environment.runner, MasterRunner):
        print("This node is a master.")
    elif isinstance(environment.runner, WorkerRunner):
        print("This node is a worker.")
        print("================================================================")
        if (USER_CREDENTIALS == None):
            USER_CREDENTIALS = requests.post(auth_url, {
            'client_id': client_id,
            'client_secret': client_secret,
            'scope': 'openid',
            'grant_type': 'password',
            'username': username,
            'password': password
        }).json()
        print(USER_CREDENTIALS)
    else:
        print("This is a standalone node.")

# Hooking the function to the test_start event
events.test_start.add_listener(on_test_start)

logging.info(USER_CREDENTIALS)

class Test(HttpUser):
    global USER_CREDENTIALS  # Declare USER_CREDENTIALS as global

    wait_time = between(1, 3)

    bearer = None
    trip = None

    @task
    def task_my_trips_update(self):
        self.client.put("/api/v1/my/item", 
                         headers = {"Authorization": self.bearer},
                         json = self.trip["members"][0]["items"][0]
                         )

    (3)
    @task
    def task_my_trips(self):
        self.client.get("/api/v1/my/trip", headers = {"Authorization": self.bearer})

    @task
    def task_my_trip_by_id(self):
        self.client.get("/api/v1/my/trip/" + self.trip["id"], headers = {"Authorization": self.bearer})

    @task
    def task_my_user_profile(self):
        self.client.get("/api/v1/my/userProfile", headers = {"Authorization": self.bearer})

    def on_start(self):
        
        self.bearer = "Bearer " + USER_CREDENTIALS["id_token"]
        logging.info(self.bearer)

        self.trip = self.client.get("/api/v1/my/trip", headers = {"Authorization": self.bearer}).json()[0]
        logging.info(self.trip)
