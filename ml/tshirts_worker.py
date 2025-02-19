import json

from typing import Any, Dict, Tuple
from exception import *
from model_launcher import *
from utils import *
from http.server import BaseHTTPRequestHandler, HTTPServer
from logger.logger_builder import LoggerBuilder
from logger.logger_converter import LoggerConverter


setup_logging(LOG_DIR)
log = LoggerBuilder.get_logger(TSHIRTS_WORKER_NAME_KEYWORD)

model = Tshirts_GAN()

class TshirtsWorker(BaseHTTPRequestHandler):
    def do_GET(self) -> None:
        log.info(HEALTH_CHECK_FINISH_MESSAGE)
        self._send_response(Status.OK.value, {STATUS: Status.OK.value})
    
    def do_POST(self) -> None:
        log.info(REQUEST_START_MESSAGE)
        input_data, message_id = self._get_input_data()
        logger_convertor = LoggerConverter()
        
        try:    
            inference_result = self._inference_process(input_data, message_id)
            search_result = ImageSearcher.search(inference_result)
            log.info(INFERENCE_FINISH_MESSAGE)
            log.info(REQUEST_FINISH_MESSAGE)
            
            logger_convertor.saveInferenceLog(message_id)
            self._send_response(Status.OK.value, search_result)
        except Exception as e:
            log.error(f"{REQUEST_FAIL_MESSAGE} : {e}")
            log.error(f"{ERROR_MESSAGE} : {e}")
            
            logger_convertor.saveInferenceLog(message_id)
            self._send_response(Status.BAD_GATEWAY.value, {ERROR: str(e)})
            

    def _inference_process(self, input_data: str, message_id: str) -> str:
        log.info(INFERENCE_START_MESSAGE)
        return model.inference(input_data, message_id, TSHIRTS_SERVER_NAME)

    def _get_input_data(self) -> Tuple[str, str]:
        log.info(REQUEST_BODY_EXTRACT_START_MESSAGE)
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        input_data = json.loads(post_data)
        log.info(REQUEST_BODY_EXTRACT_FINISH_MESSAGE)
        return SearchConverter.convert_upload_file_name(input_data.get(STORE_FILE_PATH, EMPTY)), input_data.get("id", EMPTY)

    def _send_response(self, status_code: int, response_data: Dict[str, Any]) -> None:
        response = json.dumps(response_data, ensure_ascii=False)            
        self.send_response(status_code)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(response.encode(UTF8))

def run(server_class=HTTPServer, handler_class=TshirtsWorker, port: int = 5000):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    log.info(f'{WAITING_MESSAGE} port = {port}')
    httpd.serve_forever()


if __name__ == "__main__":
    run()