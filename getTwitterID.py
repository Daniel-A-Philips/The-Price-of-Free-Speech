import requests
import csv
import json

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'

def auth():
    bearer_token = "AAAAAAAAAAAAAAAAAAAAAP5KNAEAAAAAycIuL%2BYXlU9sdi7Z267bDQp%2FfE0%3DAkIyNchX9UmeGO10oUkryZa75J7FP4o5jEyM3m4uMbvwe69sXw"
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
    csvfile = open(writeFile,'w',newline='')
    writer = csv.writer(csvfile)
    index = 0
    writable = ""
    for row in allData:
        # Skips over the current user if they are in the 'invalid' section
        if row[0] in invalid:
            continue
        # Checks if the headers need to be written
        if index == 0:
            writable = row
        # Formats the line in order to take both the handle and the id
        else:
            writable = [row[0], ID[index - 1]]
        writer.writerow(writable)
        index += 1

def parseData():
    # Checks if there are any invalid handles
    try:
        temp = output['errors']
    except: # Runs if there are no errors
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
    global readFile
    global writeFile
    global output
    global errors
    global invalid
    global ID
    handles = []
    allData = []
    readFile = 'Data//Handles.csv'
    writeFile = 'Data//Handles_ID.csv'
    index = 0
    errors = 0
    invalid = []
    ID = []
    with open(readFile) as csvfile:
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
    output = connect_to_endpoint(url, headers)
    parseData()
    writeData()


if __name__ == "__main__":
    main()