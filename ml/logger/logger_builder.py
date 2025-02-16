import logging
import os

class LoggerBuilder():
    
    @staticmethod
    def create_log_path(save_dir):
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)
        else:
            file_path = f"{save_dir}info.log"
            if os.path.isfile(file_path):
                with open(file_path, "w", encoding="utf8") as file:
                    file.truncate(0)
    
    @staticmethod    
    def get_logger(name):
        logger = logging.getLogger(name)
        logger.setLevel(logging.INFO)
        return logger