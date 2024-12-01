class SearchConverter():
    @staticmethod
    def convert_search_result(response, site):
        try:
            search_result = response['tags'][0]['actions'][2]['data']['value']
        
            return [
                {
                    "thumbnailUrl" : result['thumbnailUrl'], 
                    "webSearchUrl" : result['webSearchUrl'], 
                    "hostPageUrl" : result['hostPageUrl'],
                    "site" : site
                } for result in search_result
            ]
        
        except Exception as e: 
            print(f"SearchConverter Error: {e}")
            print(search_result)