from AutoComplete import predictor
from calendar import monthrange
from datetime import date
import PySimpleGUI
import csv
import os



class Gui:

    def __init__(self):
        self.Intervals, self.Months, self.window, self.complete, self.allTickers, self.non_parsed_tickers = ([] for i in range(6))
        self.run()

    def format(self, foo):
        toReturn = foo.strftime('%B'), foo.strftime('%Y')
        toReturn = ' '.join(toReturn)
        return toReturn

    def getTickers(self):
        with open(os.getcwd() + '/Data/tickers.csv') as file:
            reader = csv.reader(file)
            for line in reader:
                if line[0] != 'Symbol' or line[0].split('|')[0] != 'Symbol':
                    if '^' in line[0] or '^' in line[0].split('|')[0]:
                        continue
                    if '|' not in line[0]:
                        self.non_parsed_tickers.append(line[0])
                    else:
                        self.non_parsed_tickers.append(line[0].split('|')[0])
        self.allTickers = list(dict.fromkeys(self.non_parsed_tickers))
        self.allTickers.sort()

    def getDates(self):
        months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
                  "November", "December"]
        days = []
        today_date = date.today()
        latestMonth = today_date.strftime('%B')
        latestMonth_Index = months.index(latestMonth) + 1
        tempMonthIndex = latestMonth_Index
        year = today_date.year
        lastYear = []
        for i in range(12):
            if tempMonthIndex + i == 13: break
            lastYear.append(date(year - 1, tempMonthIndex + i, 1))
            days.append(monthrange(year - 1, tempMonthIndex + i)[1])
        tempMonthIndex = latestMonth_Index - 1
        thisYear = []
        for f in range(latestMonth_Index):
            thisYear.append(date(year, tempMonthIndex - f + 1, 1))
            days.append(monthrange(year, tempMonthIndex - f + 1)[1])
        thisYear.reverse()
        return lastYear + thisYear

    def update(self, key, text):
        self.window[key].update(text)

    def updateTickers(self):
        event, values = self.data()
        closest = self.complete.getClosest(values['Ticker'])
        self.window.FindElement('Ticker').Update(values=closest, value=values['Ticker'], size=(10, 10))

    def data(self):
        return self.window.read(timeout=30)

    def run(self):
        self.complete = predictor()
        self.getTickers()
        self.Intervals = ['1 Minute', '5 Minutes', '15 Minutes', '30 Minutes', '60 Minutes', '1 Day', '1 Week',
                          '1 Month']
        for month in self.getDates():
            self.Months.append(self.format(month))
        self.layout = [
            [PySimpleGUI.Text('Stock Ticker', size=(15, 1)),    PySimpleGUI.InputCombo(values=self.allTickers, key='Ticker', default_value='Ticker', size=(10, 1))],
            [PySimpleGUI.Text('Twitter Handles (Separated by a comma)', size=(15, 3)),  PySimpleGUI.Input("Handle", key='Handle', size=(10, 1))],
            [PySimpleGUI.Text('Data Interval', size=(15, 1)),   PySimpleGUI.Combo(self.Intervals, default_value=self.Intervals[3], key='Interval')],
            [PySimpleGUI.Text('Start Month', size=(15, 1)),   PySimpleGUI.Combo(self.Months, default_value=self.Months[0], key='StartMonth')],
            [PySimpleGUI.Text('End Month', size=(15, 1)),   PySimpleGUI.Combo(self.Months, default_value=self.Months[len(self.Months) - 1], key='EndMonth')],
            [PySimpleGUI.Text('SD:', size=(5, 1)), PySimpleGUI.Text(size=(10, 1), key='SD')],
            [PySimpleGUI.Text('SMVI:', size=(5, 1)), PySimpleGUI.Text(size=(10, 1), key='SMVI')],
            [PySimpleGUI.Button('Run'),   PySimpleGUI.Text('Please enter in your information', size=(28, 1), key='Terminal')],
            [PySimpleGUI.Button("Exit")]
        ]
        PySimpleGUI.theme('Reddit')
        self.window = PySimpleGUI.Window("The Price of Free Speech", self.layout)
