# import TwitterAPI
import csv
#
# #https://geduldig.github.io/tutorials/twitter-counter/
#
# API_KEY =
# API_SECRET =
# ACCESS_TOKEN =
# ACCESS_TOKEN_SECRET=
#
# accounts = []
# reader = open("Twits.txt","r")
# for line in reader:
# accounts.append(line)


# api = TwitterAPI(API_KEY, API_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET)

originalData = []
with open('D:\Topics in Computer Science\Final\Data.csv',newline='') as csvfile:
    reader = csv.reader(csvfile,delimiter=",")
    for row in reader:
        originalData.append(row)

newData = originalData
newData[0].append("tweets")

for row in newData:
    print(row)