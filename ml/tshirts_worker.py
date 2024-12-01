import json

from typing import Any, Dict
from exception import *
from model_launcher import *
from utils import *
from http.server import BaseHTTPRequestHandler, HTTPServer

model = Tshirts_GAN()

class TshirtsWorker(BaseHTTPRequestHandler):
    def do_GET(self) -> None:
        self._send_response(Status.OK, {STATUS: Status.OK})
    
    def do_POST(self) -> None:
        input_data = self._get_input_data()
        
        try:
            inference_result = self._inference_process(input_data)
            search_result = ImageSearcher.search(inference_result)
            
            self._send_response(Status.OK, search_result)
        except Exception as e:
            self._send_response(Status.BAD, {ERROR: e})

    def _inference_process(self, input_data):
        return model.inference(input_data)

    def _get_input_data(self) -> str:
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        input_data = json.loads(post_data)
        return input_data.get(STORE_FILE_PATH, EMPTY)

    def _send_response(self, status_code: int, response_data: Dict[str, Any]) -> None:
        response = json.dumps(response_data)
        self.send_response(status_code)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(response.encode(UTF8))

def run(server_class=HTTPServer, handler_class=TshirtsWorker, port=5000):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print(f'추론 요청을 대기중입니다. port = {port}...') 
    httpd.serve_forever()


if __name__ == "__main__":
    run()