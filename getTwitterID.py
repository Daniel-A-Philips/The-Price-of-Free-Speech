import requests
import csv



class getTwitterID:

    def auth(self):
        self.bearer_token = "AAAAAAAAAAAAAAAAAAAAAP5KNAEAAAAAycIuL%2BYXlU9sdi7Z267bDQp%2FfE0%3DAkIyNchX9UmeGO10oUkryZa75J7FP4o5jEyM3m4uMbvwe69sXw"
        return self.bearer_token

    def create_url(self):
        user_fields = "user.fields=description,created_at"
        self.url = "https://api.twitter.com/2/users/by?{}&{}".format(self.usernames, user_fields)

    def create_headers(self, bearer_token):
        self.headers = {"Authorization": "Bearer {}".format(bearer_token)}

    def connect_to_endpoint(self, url, headers):
        response = requests.request("GET", url, headers=headers)
        if response.status_code != 200:
            raise Exception(
                "Request returned an error: {} {}".format(
                    response.status_code, response.text
                )
            )
        return response.json()

    def getData(self):
        self.usernames = "usernames="
        for handle in self.handles:
            self.usernames = self.usernames + handle + ","
        self.usernames = self.usernames[:-1]

    def writeData(self):
        csvfile = open(self.writeFile, 'w', newline='')
        writer = csv.writer(csvfile)
        index = 0
        writable = ""
        for row in self.allData:
            # Skips over the current user if they are in the 'invalid' section
            if row[0] in self.invalid:
                continue
            # Checks if the headers need to be written
            if index == 0:
                writable = row
            # Formats the line in order to take both the handle and the id
            else:
                writable = [row[0], self.ID[index - 1]]
            writer.writerow(writable)
            index += 1

    def parseData(self):
        # Checks if there are any invalid handles
        try:
            temp = self.output['errors']
        except:  # Runs if there are no errors
            print("All twitter handles are valid")
        else:
            errors = len(self.output['errors'])
            temp = self.output['errors']
            print("Warning, " + str(errors) + " of the twitter handles is invalid!")
            for i in temp:
                self.invalid.append(i['value'])
        try:
            toWorkWith = self.output['data']
        except:
            print("Warning, there are not usable twitter handles")
        else:
            for f in toWorkWith:
                self.ID.append(f['id'])

    def getOutput(self):
        return self.ID

    def __init__(self, handle):
        self.handles = [handle]
        self.allData, self.invalid, self.ID = ([] for i in range(3))
        self.writeFile = 'Data/Handles_ID.csv'
        self.getData()
        self.auth()
        self.create_url()
        self.create_headers(self.bearer_token)
        self.output = self.connect_to_endpoint(self.url, self.headers)
        self.parseData()
        self.writeData()
