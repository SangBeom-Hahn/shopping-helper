import requests
from .search_converter import SearchConverter


BASE_URI = "https://api.bing.microsoft.com/v7.0/images/visualsearch"
SUBSCRIPTION_KEY = ''
HEADERS = {'Ocp-Apim-Subscription-Key': SUBSCRIPTION_KEY}

class ImageSearcher():
    @staticmethod
    def search(image_path):
        search_results = []
        result = {}
        for site in ["farfetch", "amazon", "pinterest", "etsy", "noFilter"]:
            with open(image_path, 'rb') as image_file:
                if(site == "nofilter"):
                    files = {
                        'image': image_file,
                    }
                else:
                    files = {
                        'image': image_file,
                        'knowledgeRequest': (None, f'{{"knowledgeRequest":{{"filters":{{"site":"www.{site}.com"}}}}}}')
                    }

                try:
                    response = requests.post(BASE_URI, headers=HEADERS, files=files)
                    response.raise_for_status()
                    response = response.json()
                    ImageSearcher._convert_response(search_results, result, site, response)

                except Exception as e:
                    print(f"ImageSearcher Error: {e}")
                    
        result["result"] = search_results
        return result

    @staticmethod
    def _convert_response(search_results, result, site, response):
        search_result = SearchConverter.convert_search_result(response, site)
        result[site] = len(search_result)
        search_results.extend(search_result)