from TwitterAPI import TwitterAPI, TwitterRequestError, TwitterConnectionError
import csv
#
# #https://geduldig.github.io/tutorials/twitter-counter/
#


def main():
    global api
    global tweets
    global accounts
    global API_KEY
    global API_KEY_SECRET
    global ACCESS_TOKEN
    global ACCESS_TOKEN_SECRET
    global AccountIDs
    API_KEY = "buLT0zag38jzbX4lNECBBkexx"
    API_KEY_SECRET = "bILyEVPlJd1GG8Nl6NRaXJ5dZCR62m1iY5NRMWEEn1UCOf2g57"
    ACCESS_TOKEN = ""
    ACCESS_TOKEN_SECRET= ""
    api = TwitterAPI(API_KEY, API_KEY_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET,api_version=2)


def getTwitterData(ID):
    tweets = []
    AccountIDs = []
    try:
        # Get tweets - default setting
        tweets = api.request(f'users/:{ID}/tweets')
        for t in tweets:
            print(t)

        # Get tweets with customization - (5 tweets only with created_at timestamp)
        print()
        params = {'max_results': 1000, 'tweet.fields': 'created_at'}
        tweets = api.request(f'users/:{ID}/tweets', params)
        for t in tweets:
            print(t)
            AccountIDs.append(t)
    
    except TwitterRequestError as e:
        print('Request error')
        print(e.status_code)
        for msg in iter(e):
            print(msg)

    except TwitterConnectionError as e:
        print('Connection error')
        print(e)

    except Exception as e:
        print('Exception')
        print(e)
    
def getWantedAccounts():
    accounts = []
    reader = open("Twits.txt","r")
    for line in reader:
        accounts.append(line)

def writeData():
    originalData = []
    with open('Data\\Data.csv',newline='') as csvfile:
        reader = csv.reader(csvfile,delimiter=",")
        for row in reader:
            originalData.append(row)

    newData = originalData
    if not newData[0].contains("tweets"):
        newData[0].append("tweets")





if __name__ == "__main__":
    main()