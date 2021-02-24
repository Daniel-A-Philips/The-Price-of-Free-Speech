import requests
import csv
import json

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'

def auth():
    bearer_token = ""
    return bearer_token


def create_url():
    # Specify the usernames that you want to lookup below
    # You can enter up to 100 comma-separated values.
    user_fields = "user.fields=description,created_at"
    # User fields are adjustable, options include:
    # created_at, description, entities, id, location, name,
    # pinned_tweet_id, profile_image_url, protected,
    # public_metrics, url, username, verified, and withheld
    url = "https://api.twitter.com/2/users/by?{}&{}".format(usernames, user_fields)
    return url


def create_headers(bearer_token):
    headers = {"Authorization": "Bearer {}".format(bearer_token)}
    return headers


def connect_to_endpoint(url, headers):
    response = requests.request("GET", url, headers=headers)
    if response.status_code != 200:
        raise Exception(
            "Request returned an error: {} {}".format(
                response.status_code, response.text
            )
        )
    return response.json()

def getData():
    global usernames
    usernames = "usernames="
    for handle in handles:
        usernames = usernames + handle + ","
    usernames= usernames[:-1]

def writeData():
    print(handles)
    csvfile = open(fileName,'w',newline='')
    writer = csv.writer(csvfile)
    index = 0
    writable = ""
    for row in allData:
        if row[0] in invalid:
            continue
        if index == 0:
            writable = row
        else:
            print(index-1)
            writable = [row[0], ID[index - 1]]
        writer.writerow(writable)
        index += 1

def parseData():
    try:
        temp = output['errors']
    except:
        print("All twitter handles are valid")
    else:
        errors = len(output['errors'])
        temp = output['errors']
        print("Warning, "+str(errors)+" of the twitter handles is invalid!")
        for i in temp:
            invalid.append(i['value'])
    try:
        toWorkWith = output['data']
    except:
        print("Warning, there are not usable twitter handles")
    else:
        for f in toWorkWith:
            ID.append(f['id'])

def main():
    global handles
    global allData
    global fileName
    global output
    global errors
    global invalid
    global ID
    handles = []
    allData = []
    fileName = 'Data//Handles.csv'
    index = 0
    errors = 0
    invalid = []
    ID = []
    with open(fileName) as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            allData.append(row)
            if not index == 0:
                handles.append(row[0])
            index += 1
    getData()
    bearer_token = auth()
    url = create_url()
    headers = create_headers(bearer_token)
    json_response = connect_to_endpoint(url, headers)
    output = json_response
    #output = (json.dumps(json_response, indent=4, sort_keys=True))
    parseData()
    writeData()


if __name__ == "__main__":
    main()