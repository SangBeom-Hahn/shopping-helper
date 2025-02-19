import os
import mysql

from google.cloud import storage
from google.cloud.exceptions import GoogleCloudError
from google.oauth2 import service_account
from mysql.connector import Error
from typing import Optional
from .constants import *
from logger.logger_builder import LoggerBuilder

log = LoggerBuilder.get_logger("StorageHandler")

KEY_PATH = ""
credentials = service_account.Credentials.from_service_account_file(KEY_PATH)

class StorageHandler():
    def download_img(self, file_name: str) -> None:
        try:
            log.info(DOWNLOAD_START_MESSAGE)
            storage_client = storage.Client(credentials = credentials, project = credentials.project_id)
            bucket = storage_client.bucket(BUCKET_NAME)
            blob = bucket.blob(f"{BUCKET_UPLOAD_RELATIVE_PATH}/{file_name}")
            blob.download_to_filename(os.path.join(UPLOAD_RELATIVE_PATH, file_name))
            log.info(DOWNLOAD_FINISH_MESSAGE)
        except GoogleCloudError as e:
            print(e)
        
    def upload_img(self, src: str, dest: str) -> None:
        try:
            log.info(UPLOAD_START_MESSAGE)
            storage_client = storage.Client(credentials = credentials, project = credentials.project_id)
            bucket = storage_client.bucket(BUCKET_NAME)
            blob = bucket.blob(f'{BUCKET_RESULT_RELATIVE_PATH}/{dest}')
            blob.upload_from_filename(src)
            log.info(UPLOAD_FINISH_MESSAGE)
        except GoogleCloudError as e:
            print(e)
            
    def changeClothesResultStatus(self, status: str, status_message: str, message_id: int, colored_file_path: str = None) -> None:
        connection = None
        try:
            log.info(RESULT_UPDATE_START_MESSAGE)
            connection = mysql.connector.connect(
                host='',
                database=SOURCE_MYSQL_DATABASE,
                user=SOURCE_MYSQL_USERNAME,
                password=''
            )

            if connection.is_connected():
                if(colored_file_path == None):
                    query = f"UPDATE clothes_model_result SET status = '{status}', status_message = '{status_message}' WHERE message_id = {message_id};"
                else:
                    query = f"UPDATE clothes_model_result SET store_file_path = '{BUCKET_RESULT_RELATIVE_PATH}/{colored_file_path}', status = '{status}', status_message = '{status_message}' WHERE message_id = {message_id};"
                cursor = connection.cursor()
                cursor.execute(query)
                connection.commit()
            log.info(RESULT_UPDATE_FINISH_MESSAGE)
        except Exception as e:
            log.error(f"{DB_CONNECT_EXCEPTION_MESSAGE}: {e}")

        finally:
            if connection.is_connected():
                cursor.close()
                connection.close()
    
    def changeCommonLogStatus(self, message_id: int, queue_out_time: str = None, inference_server_name: str = None, inference_end_time: str = None) -> None:
        connection = None
        try:
            log.info(COMMON_UPDATE_START_MESSAGE)
            connection = mysql.connector.connect(
                host='',
                database=SOURCE_MYSQL_DATABASE,
                user=SOURCE_MYSQL_USERNAME,
                password=''
            )

            if connection.is_connected():
                if(inference_end_time == None):
                    query = f"UPDATE common_log SET queue_out_time = '{queue_out_time}', inference_server_name = '{inference_server_name}' WHERE message_id = {message_id};"
                else:
                    query = f"UPDATE common_log SET inference_end_time = '{inference_end_time}' WHERE message_id = {message_id};"
                    
                cursor = connection.cursor()
                cursor.execute(query)
                connection.commit()
            log.info(COMMON_UPDATE_FINISH_MESSAGE)
        except Exception as e:
            log.error(f"{DB_CONNECT_EXCEPTION_MESSAGE}: {e}")

        finally:
            if connection.is_connected():
                cursor.close()
                connection.close()
                
    def changeInferenceLogStatus(self, message_id: int, content: str = None) -> None:
        connection = None
        try:
            log.info(INFERENCE_UPDATE_START_MESSAGE)            
            connection = mysql.connector.connect(
                host='',
                database=SOURCE_MYSQL_DATABASE,
                user=SOURCE_MYSQL_USERNAME,
                password=''
            )

            if connection.is_connected():
                query = "INSERT INTO inference_log (message_id, content) VALUES (%s, %s);"
                cursor = connection.cursor()
                cursor.execute(query, (message_id, content))
                connection.commit()
            log.info(INFERENCE_UPDATE_FINISH_MESSAGE)
        except Exception as e:
            log.error(f"{DB_CONNECT_EXCEPTION_MESSAGE}: {e}")

        finally:
            if connection.is_connected():
                cursor.close()
                connection.close()                
