class InferenceException(Exception):
    def __init__(self, message=None):
        self.message = message

    def __str__(self):
        return f"{self.message}"

class NoSuchFileException(InferenceException):
    def __init__(self, message="존재하지 않거나 손상된 모델 파일입니다."):
        super().__init__(message)

class FileAbsentException(InferenceException):
    def __init__(self, store_file_path):
        super().__init__(f"이미지를 찾을 수 없습니다.. 이미지 경로 = {store_file_path}")