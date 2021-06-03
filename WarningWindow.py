import PySimpleGUI


class WarningWindow:

    def __init__(self):
        layout = [
            [PySimpleGUI.Text(
                'Warning, this tool should not be used as a sole decider on whether to trade an asset or not. By clicking OK, you agree that any party involved in the making of this program is not liable for any losses or impacts that occur due to the use of the product',
                size=(45, 5))],
            [PySimpleGUI.Checkbox('Do you agree with the statement above?', key='check')],
            [PySimpleGUI.Button('Begin', key='agree')],
            [PySimpleGUI.Button('Quit', key='quit')]
        ]
        PySimpleGUI.theme('Reddit')
        window = PySimpleGUI.Window("The Price of Free Speech Conditions", layout)
        while True:
            event, values = window.read()
            if event in [PySimpleGUI.WIN_CLOSED, 'quit']:
                exit()
            elif event == 'agree' and values['check'] == True:
                window.close()
                break
