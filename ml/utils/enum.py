from enum import Enum

class Status(Enum):
    
    OK = 200
    BAD = 500
    
class Normalize(Enum):
    
    SYMMETRIC_VALUE = 127.5
    MIN_MAX_VALUE = 255
    
class Site(Enum):
    
    FARFETCH = "farfetch"
    AMAZON = "amazon"
    PINTEREST = "pinterest"
    ETSY = "etsy"
    NOFILTER = "noFilter"