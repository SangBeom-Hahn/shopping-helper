from enum import Enum

class Status(Enum):
    
    OK = 200
    BAD_GATEWAY = 502
    
class Normalize(Enum):
    
    SYMMETRIC_VALUE = 127.5
    MIN_MAX_VALUE = 255
    
class Site(Enum):
    
    FARFETCH = "farfetch"
    AMAZON = "amazon"
    PINTEREST = "pinterest"
    ETSY = "etsy"
    NOFILTER = "noFilter"
    
class InferenceStatus(Enum):
    
    FINISH = "채색이 완료되었습니다. 결과를 확인해주세요."
    ERROR = "에러가 발생했습니다. 원인을 분석 중이니 잠시만 기다려주세요."