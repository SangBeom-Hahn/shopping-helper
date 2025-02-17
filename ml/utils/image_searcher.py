import requests
from .search_converter import SearchConverter
from .enum import Site
from typing import Dict, List, Any, Union
from logger.logger_builder import LoggerBuilder

log = LoggerBuilder.get_logger("ImageSearcher")


BASE_URI = "https://api.bing.microsoft.com/v7.0/images/visualsearch"
SUBSCRIPTION_KEY = ''
HEADERS = {'Ocp-Apim-Subscription-Key': SUBSCRIPTION_KEY}
IMAGE_REQUEST_KEY = 'image'
KNOWLEDGE_REQUEST_KEY = 'knowledgeRequest'

class ImageSearcher():
    @staticmethod
    def search(image_path: str) -> Dict[str, Union[List[Any], Dict[str, int]]]:
        log.info("검색 process 시작")
        search_results = []
        result = {}
        
        for site in Site:
            with open(image_path, 'rb') as image_file:
                if(site == Site.NOFILTER):
                    files = {
                        IMAGE_REQUEST_KEY: image_file,
                    }
                else:
                    files = {
                        IMAGE_REQUEST_KEY: image_file,
                        KNOWLEDGE_REQUEST_KEY: (None, f'{{"knowledgeRequest":{{"filters":{{"site":"www.{site.value}.com"}}}}}}')
                    }

                try:
                    response = requests.post(BASE_URI, headers=HEADERS, files=files)
                    response.raise_for_status()
                    response = response.json()
                    ImageSearcher._convert_response(search_results, result, site.value, response)

                except Exception as e:
                    log.error(f"ImageSearcher 에러: {e}")
                    
        result["result"] = search_results
        log.info("검색 process 완료")
        return result

    @staticmethod
    def _convert_response(search_results: List[Any], result: Dict[str, int], site: str, response: Dict[str, Any]) -> None:
        search_result = SearchConverter.convert_search_result(response, site)
        result[site] = len(search_result)
        search_results.extend(search_result)