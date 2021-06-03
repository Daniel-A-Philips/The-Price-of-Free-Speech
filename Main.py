from WarningWindow import WarningWindow
from Correlation import Correlation
from Stock import Stock
from Gui import Gui
import PySimpleGUI


class GuiRunner:

    def __init__(self):
        self.WarningWindow = WarningWindow()
        self.main()

    def parseHandles(self, handles):
        handles = handles.replace(" ", "")
        return handles.split(",")

    def getMonths(self, values):
        endMonth = values['EndMonth']
        startMonth = values['StartMonth']
        start = self.gui.Months[0:self.gui.Months.index(endMonth)]
        end = self.gui.Months[self.gui.Months.index(startMonth) + 1:len(self.gui.Months)]
        self.gui.window.FindElement('EndMonth').Update(values=end, value=values['EndMonth'], size=(10, 10))
        self.gui.window.FindElement('StartMonth').Update(values=start, value=values['StartMonth'], size=(10, 10))

    def main(self):
        self.gui = Gui()
        self.Intervals = ['1 Minute', '5 Minutes', '15 Minutes', '30 Minutes', '60 Minutes', '1 Day', '1 Week',
                          '1 Month']
        previous_ticker = ''
        while True:
            event, values = self.gui.data()
            self.getMonths(values)
            if values['Ticker'] != previous_ticker:
                self.gui.updateTickers()
                previous_ticker = values['Ticker']
            elif values['Ticker'] in ['Ticker', '']:
                self.gui.window.FindElement('Ticker').Update(values=self.gui.allTickers, value=values['Ticker'],
                                                             size=(10, 10))
            if event in [PySimpleGUI.WIN_CLOSED, 'Exit']:
                exit()
            elif event == 'Run':
                toEnter = [values['Ticker'],
                           self.Intervals.index(values['Interval']),
                           values['StartMonth'],
                           values['EndMonth'],
                           ]
                self.gui.update('SD', "Calculating...")
                self.gui.update('SMVI', "Calculating...")
                self.gui.update('Terminal', 'Calculating...')
                if values['Handle'] in ['', 'Handle']:
                    PySimpleGUI.popup('Error, please enter a valid Twitter handle')
                    continue
                stock = Stock(toEnter[0], toEnter[1], toEnter[2], toEnter[3], self.gui.Months, False)
                if stock.hasErrors:
                    error_message = 'An error occured'
                    self.gui.update('Terminal', error_message)
                    self.gui.update('SD', error_message)
                    self.gui.update('SMVI', error_message)
                    continue
                else:
                    SDToDisplay = str(stock.SDToDisplay)[0:8]
                    self.gui.update('SD', SDToDisplay)
                    self.gui.update('Terminal', 'Calculating baseline...')
                    DIA = Stock('DIA', toEnter[1], toEnter[2], toEnter[3], self.gui.Months, True)
                    self.gui.update('Terminal', 'Calculating SVMI')
                    cor = Correlation(stock, DIA, self.parseHandles(values['Handle']))
                    self.gui.update('SMVI', str(cor.SMVI * float(SDToDisplay))[0:8])
                    self.gui.update('Terminal', 'Done!')


runner = GuiRunner()
