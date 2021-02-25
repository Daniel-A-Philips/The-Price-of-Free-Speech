import requests
import csv
import json
import datetime

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'

def auth():
    bearer_token = ""
    return bearer_token


def create_url(id):
    return "https://api.twitter.com/2/users/{}/tweets".format(id)


def get_params():
    # Tweet fields are adjustable.
    # Options include:
    # attachments, author_id, context_annotations,
    # conversation_id, created_at, entities, geo, id,
    # in_reply_to_user_id, lang, non_public_metrics, organic_metrics,
    # possibly_sensitive, promoted_metrics, public_metrics, referenced_tweets,
    # source, text, and withheld
    d = str(datetime.datetime.utcnow().isoformat("T")) # <-- get time in UTC
    end = d[0:d.index(".")] + "Z"
    print(end)
    return {"tweet.fields": "created_at","end_time":end,"start_time":"2021-02-22T23:59:59Z","max_results":"100"}


def create_headers(bearer_token):
    headers = {"Authorization": "Bearer {}".format(bearer_token)}
    return headers


def connect_to_endpoint(url, headers, params):
    response = requests.request("GET", url, headers=headers, params=params)
    print(response.status_code)
    if response.status_code != 200:
        raise Exception(
            "Request returned an error: {} {}".format(
                response.status_code, response.text
            )
        )
    return response.json()

def getID():
    file = open(fileName,"r")
    reader = csv.reader(file)
    index = 0
    for line in reader:
        if index == 0:
            index += 1
            continue
        ID.append(line[1])

def main():
    global fileName
    global ID
    global Data
    fileName = "Data//Handles_ID.csv"
    ID = []
    Data = []
    getID()
    bearer_token = auth()
    for id in ID:
        url = create_url(id)
        headers = create_headers(bearer_token)
        params = get_params()
        json_response = connect_to_endpoint(url, headers, params)
        Data.append(json_response)
        print(json.dumps(json_response, indent=4, sort_keys=True))


if __name__ == "__main__":
    main()