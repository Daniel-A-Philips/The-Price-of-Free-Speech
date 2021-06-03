import requests
import os


class getAllTickers():
    def __init__(self):
        print('Downloading')
        path = os.getcwd() + '/Data/'
        url = 'http://ftp.nasdaqtrader.com/dynamic/SymDir/nasdaqlisted.txt'
        r = requests.get(url, allow_redirects=True)
        print(r.headers.get('content-type'))
        open(path + 'tickers.csv', 'wb+').write(r.content)
