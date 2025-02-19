from .constants import *
from typing import Any, Dict, List
from logger.logger_builder import LoggerBuilder

log = LoggerBuilder.get_logger(SEARCH_CONVERTER_NAME_KEYWORD)

class SearchConverter():
    @staticmethod
    def convert_search_result(response: Dict[str, Any], site: str) -> List[Dict[str, Any]]:
        log.info(f"{site} {SEARCH_RESULT_EXTRACT_START_MESSAGE}")
        try:
            
            search_result = response[BING_API_TAGS_KEY][0][BING_API_ACTIONS_KEY][2][BING_API_DATA_KEY][BING_API_VALUE_KEY]
            log.info(f"{site} {SEARCH_RESULT_EXTRACT_FINISH_MESSAGE}")
            return [
                {
                    BING_API_THUMBNAIL_KEY : result[BING_API_THUMBNAIL_KEY], 
                    BING_API_WEB_KEY : result[BING_API_WEB_KEY], 
                    BING_API_HOST_KEY : result[BING_API_HOST_KEY],
                    SEARCH_FILE_NAME : result[SEARCH_FILE_NAME],
                    SITE : site
                } for result in search_result
            ]
        except Exception as e: 
            log.error(f"{SEARCH_CONVERTER_ERRER_MESSAGE} : {e}")
    
    @staticmethod
    def convert_upload_file_name(upload_file_path):
        return upload_file_path.split('/')[-1]