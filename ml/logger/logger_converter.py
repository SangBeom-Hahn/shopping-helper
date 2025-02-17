from utils import *
from utils.constants import *

class LoggerConverter():
    def __init__(self) -> None:
        self.storage_handler = StorageHandler()
        
    def saveInferenceLog(self, message_id):

        with open(LOG_FILE_PATH, READ_WRITE_TYPE, encoding=UTF8) as file:
            log_messages = []
            for line in file:
                log_messages.append(line.strip())
            total_log = NEW_LINE.join(log_messages)
                
            self.storage_handler.changeInferenceLogStatus(message_id, total_log)
            file.truncate(ZERO_IDX)