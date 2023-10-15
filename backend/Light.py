import serial

from config import ARDUINO_PORT

class Light:
    def __init__(self):
        self.ser = serial.Serial(ARDUINO_PORT, 9600)

    def light_on(self):
        self.ser.write(b'1')

    def light_off(self):
        self.ser.write(b'2')
