import requests
import csv
import json
import datetime

# To set your enviornment variables in your terminal run the following line:
# export 'BEARER_TOKEN'='<your_bearer_token>'

def auth():
    bearer_token = "AAAAAAAAAAAAAAAAAAAAAP5KNAEAAAAAycIuL%2BYXlU9sdi7Z267bDQp%2FfE0%3DAkIyNchX9UmeGO10oUkryZa75J7FP4o5jEyM3m4uMbvwe69sXw"
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
    return {"tweet.fields": "created_at","end_time":end,"start_time":"2021-02-22T23:59:59Z","max_results":"100"}


def create_headers(bearer_token):
    headers = {"Authorization": "Bearer {}".format(bearer_token)}
    return headers


def connect_to_endpoint(url, headers, params):
    response = requests.request("GET", url, headers=headers, params=params)
    if response.status_code != 200:
        print(response.status_code)
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
        ID.append(line)

def writeData(data):
    outfile = open(writeFileName,'w')
    outfile.write(data)

def writeTotal():
    totalTweets = 0
    read_file = ""
    read_data = []
    for id in ID:
        read_file = "User_Data//"
        read_file += id[0]+".json"
        read_file = open(read_file,'r')
        for line in read_file:
            if "result_count" in line:
                read_data.append(line)
    write_file = "User_Data//Admin.json"
    write_file = open(write_file,'w')
    index = 0
    for line in read_data:
        line = line[line.index(":")+2:len(line)]
        totalTweets += int(line)
        write_file.write(ID[index][0]+":"+ line)
        index += 1
    print(totalTweets,"Tweets")

def main():
    global fileName
    global writeFileName
    global ID
    global Data
    global totalTweets
    fileName = "Data//Handles_ID.csv"
    ID = []
    Data = []
    getID()
    bearer_token = auth()
    for id in ID:
        writeFileName = "User_Data//"
        writeFileName += id[0]+".json"
        url = create_url(id[1])
        headers = create_headers(bearer_token)
        params = get_params()
        json_response = connect_to_endpoint(url, headers, params)
        Data.append(json_response)
        writeData(json.dumps(json_response,indent=4,sort_keys=True))
    writeTotal()


if __name__ == "__main__":
    main()