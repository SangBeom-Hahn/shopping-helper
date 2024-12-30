from .constants import *
from typing import Any, Dict, List

class SearchConverter():
    @staticmethod
    def convert_search_result(response: Dict[str, Any], site: str) -> List[Dict[str, Any]]:
        try:
            
            search_result = response[BING_API_TAGS_KEY][0][BING_API_ACTIONS_KEY][2][BING_API_DATA_KEY][BING_API_VALUE_KEY]
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
            print(f"SearchConverter Error: {e}")
            print(search_result)
    
    @staticmethod
    def convert_upload_file_name(upload_file_path):
        return upload_file_path.split('/')[-1]