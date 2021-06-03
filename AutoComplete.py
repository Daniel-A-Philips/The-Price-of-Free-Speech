import difflib
import csv
import os

class predictor:

	def getTickers(self):
		with open(os.getcwd() + '/Data/tickers.csv') as file:
			reader = csv.reader(file)
			for line in reader:
				if line[0] != 'Symbol': self.allTickers.append(line[0])

	def getClosest(self,current):
		if current == 'Ticker':
			return ['']
		number_to_show = 15
		return difflib.get_close_matches(current.upper(),self.allTickers,n=number_to_show)

	def __init__(self):
		self.allTickers = []
		self.getTickers()
