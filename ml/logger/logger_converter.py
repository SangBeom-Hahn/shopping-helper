from utils import *

class LoggerConverter():
    def __init__(self) -> None:
        self.storage_handler = StorageHandler()
        
    def saveInferenceLog(self, message_id):
        with open("logs/info.log", 'r+', encoding='utf-8') as file:
            log_messages = []
            for line in file:
                log_messages.append(line.strip())
            total_log = "\n".join(log_messages)
                
            self.storage_handler.changeInferenceLogStatus(message_id, total_log)
            file.truncate(0)