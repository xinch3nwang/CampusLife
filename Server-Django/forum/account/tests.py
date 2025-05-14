import requests
import json

# url = 'http://localhost:8000/account/signup/'
# data = {
#     'username': 'testuser1',
#     'password': 'Test@1111'
# }
# response = requests.post(url, data=data)
# print(response.json())

url = 'http://localhost:8000/account/fans/'
params = {"usrid":"913f0208-b489-44e5-a802-04c82648b39d"}
r = requests.get(url, params=params)
print(r.json())