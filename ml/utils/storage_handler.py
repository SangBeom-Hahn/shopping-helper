import os
import mysql

from google.cloud import storage
from google.oauth2 import service_account
from mysql.connector import Error
from .constants import *

KEY_PATH = ""
credentials = service_account.Credentials.from_service_account_file(KEY_PATH)

class StorageHandler():
    def download_img(self, file_name):
        try:
            storage_client = storage.Client(credentials = credentials, project = credentials.project_id)
            bucket = storage_client.bucket(BUCKET_NAME)
            blob = bucket.blob(f"{BUCKET_UPLOAD_RELATIVE_PATH}/{file_name}")
            blob.download_to_filename(os.path.join(UPLOAD_RELATIVE_PATH, file_name))
        except GoogleCloudError as e:
            print(e)
        
    def upload_img(self, src, dest):
        try:
            storage_client = storage.Client(credentials = credentials, project = credentials.project_id)
            bucket = storage_client.bucket(BUCKET_NAME)
            blob = bucket.blob(f'{BUCKET_RESULT_RELATIVE_PATH}/{dest}')
            blob.upload_from_filename(src)
            return 
        except GoogleCloudError as e:
            print(e)
            
    def changeClothesResultStatus(self, colored_file_path, status, status_message, message_id):
        connection = None
        try:
            connection = mysql.connector.connect(
                host='',
                database=SOURCE_MYSQL_DATABASE,
                user=SOURCE_MYSQL_USERNAME,
                password=''
            )

            if connection.is_connected():
                query = f"UPDATE clothes_model_result SET store_file_path = '{BUCKET_RESULT_RELATIVE_PATH}/{colored_file_path}', status = '{status}', status_message = '{status_message}' WHERE message_id = {message_id};"
                cursor = connection.cursor()
                cursor.execute(query)
                connection.commit()

        except Error as e:
            print(DB_CONNECT_EXCEPTION_MESSAGE, e)

        finally:
            if connection.is_connected():
                cursor.close()
                connection.close()