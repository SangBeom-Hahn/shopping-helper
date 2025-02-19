from logger import *

log = LoggerBuilder.get_logger("InferenceException")

class InferenceException(Exception):
    def __init__(self, message: str = None) -> None:
        self.message = message

    def __str__(self) -> str:
        return f"{self.message}"

class NoSuchFileException(InferenceException):
    def __init__(self, message="존재하지 않거나 손상된 모델 파일입니다.") -> None:
        super().__init__(message)
        log.error("존재하지 않거나 손상된 모델 파일입니다.")

class FileAbsentException(InferenceException):
    def __init__(self, store_file_path: str) -> None:
        super().__init__(f"이미지를 찾을 수 없습니다. 이미지 경로 = {store_file_path}")
        log.error(f"이미지를 찾을 수 없습니다. 이미지 경로 = {store_file_path}")   
        
class DataAbsentException(InferenceException):
    def __init__(self) -> None:
        super().__init__("None 값의 데이터가 입력되었습니다.")
        log.error("None 값의 데이터가 입력되었습니다.")  