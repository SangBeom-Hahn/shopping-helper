import logging
import os

from utils.constants import *

class LoggerBuilder():
    
    @staticmethod
    def create_log_path(save_dir):
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)
        else:
            file_path = save_dir + LOG_FILENAME
            if os.path.isfile(file_path):
                with open(file_path, WRITE_TYPE, encoding=UTF8) as file:
                    file.truncate(0)
    
    @staticmethod    
    def get_logger(name):
        logger = logging.getLogger(name)
        logger.setLevel(logging.INFO)
        return logger