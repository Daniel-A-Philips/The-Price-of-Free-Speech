from calendar import monthrange
import requests
import datetime
import os


class FinnHub:

    def run(self):
        self.URLConnect()
        self.writeData()

    def __init__(self, start, end, Ticker, Interval, forSMVI):
        self.tempMonth = ['', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September','October', 'November', 'December']
        self.key, self.headers = 'c1vv82l37jkoemkedus0', ['time', 'open', 'high', 'low', 'close', 'volume']
        self.start, self.end, self.start_end = start, end, [start, end]
        self.Ticker, self.Interval, self.forSMVI, self.Format = Ticker.upper(), Interval, forSMVI, 'csv'
        self.Dates, self.rawData, self.allData, self.absPath = self.getDates(),[],[],os.getcwd()
        self.run()

    def toUnix(self, months, years):
        years = [int(i) for i in years]
        months = [self.tempMonth.index(i) for i in months]
        return [int(datetime.datetime(years[0], months[0], 1).timestamp()), int(datetime.datetime(years[1], months[1], self.getDaysInMonth(months[1], years[1])).timestamp())]

    def getDaysInMonth(self, month, year):
        return monthrange(year, month)[1]

    def getDates(self):
        months = []
        years = []
        if self.start == self.end:  # Runs if one month is selected
            year = self.start[len(self.start) - 4:]
            dateHolder = [year]
        else:
            for startend in self.start_end:
                years.append(startend[len(startend) - 4:])
                months.append(startend[:len(startend) - 5:])
        unixed = self.toUnix(months, years)
        return unixed

    def URLConnect(self):
        monthInSeconds = 2629746
        start, end, prevStart, final = self.Dates[0], self.Dates[0] + monthInSeconds, self.Dates[0], self.Dates[1]
        index = 0
        ticker_to_print = self.Ticker
        if self.forSMVI: ticker_to_print = 'Baseline'
        os.system('clear')
        while end < final:
            prevStart = prevStart + monthInSeconds
            end = end + monthInSeconds
            startime = str(prevStart)
            endtime = str(end)
            try:
                url = self.create_url(self.Ticker,self.Interval,startime,endtime,self.Format,self.key)
                r = requests.get(url)
            except:
                print('Unable to connect to URL for',ticker_to_print)
                for i in range(3):
                    try:
                        print('Try',i)
                        url = self.create_url(self.Ticker,self.Interval,startime,endtime,self.Format,self.key)
                        r = requests.get(url)
                        break
                    except:
                        foo = None
                continue

            if index == 0: print('Connected to URL for',ticker_to_print)
            index = index + 1
            data = str(r.content).split("\\n")
            data.pop(0)
            data.remove("'")
            self.rawData.append(data)
        print('Recieved all stock related information for',ticker_to_print)

    def create_url(self,ticker,interval,startime,endtime,format,key):
        return ('https://finnhub.io/api/v1/stock/candle?symbol=%s&resolution=%s&from=%s&to=%s&format=%s&token=%s' % (ticker,interval,startime,endtime,format,key))


    def writeData(self):
        if self.forSMVI:
            path = self.absPath + '/Data/DIA_Data.csv'
        else:
            path = self.absPath + '/Data/Data.csv'
        with open(path, 'w', newline='') as file:
            for data in self.rawData:
                for line in data:
                    self.allData.append(line)
                    file.write(line + '\n')
