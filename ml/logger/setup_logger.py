import logging
import logging.config
import json

from pathlib import Path
from collections import OrderedDict
from utils.constants import *
from .logger_builder import LoggerBuilder

def setup_logging(save_dir, log_config="logger/logger_config.json", default_level=logging.INFO):
    LoggerBuilder.create_log_path(save_dir)
    log_config = Path(log_config)
    
    if(log_config.is_file()):
        fname = Path(log_config)
        with fname.open(READ_TYPE) as handle:
            config = json.load(handle, object_hook = OrderedDict)
            filename_key = FILENAME_KEY
            for _, handler in config[HANDLERS_KEY].items():
                if(filename_key in handler):
                    handler[filename_key] = save_dir  + DIR_DELIMETER + handler[filename_key]
            logging.config.dictConfig(config)
    else:
        print("{}에서 로거 설정 파일을 찾을 수 없습니다.".format(log_config))
        logging.basicConfig(level=default_level)