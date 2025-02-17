import numpy as np
import os
from datetime import datetime

from exception import *
from keras_preprocessing.image import load_img
from utils import *
from PIL import Image
from logger import *

log = LoggerBuilder.get_logger("Base_GAN")

class Base_GAN():
    
    def __init__(self) -> None:
        self.storage_handler = StorageHandler()
    
    def _load_model(self) -> None:
        raise NotImplementedError(GAN_IMPLEMENTS_EXCEPTION_MESSAGE)

    def inference(self, store_file_path: str, message_id: int, server_name: str) -> str:
        img = self._preprocess(store_file_path, message_id, server_name)
        log.info("model predict 시작")
        color_img = self.model.predict(img)
        log.info("model predict 완료")
        return self._postprocess(color_img, store_file_path, message_id)

    def _preprocess(self, store_file_path: str, message_id: str, server_name: str) -> np.ndarray:
        log.info("preprocess 시작")
        self.storage_handler.changeCommonLogStatus(message_id, queue_out_time = datetime.now(), inference_server_name = server_name)
        self.storage_handler.download_img(store_file_path)
        img = self._load_img(os.path.join(UPLOAD_RELATIVE_PATH, store_file_path))
        log.info("preprocess 완료")
        return self._pre_normalize(img)
    
    def _load_img(self, store_file_path: str) -> Image.Image:
        log.info("스케치 이미지 메모리 로드 시작")
        self._validate_file_exists(store_file_path)
        log.info("스케치 이미지 메모리 로드 완료")
        return load_img(store_file_path, target_size=(RESIZE_VALUE, RESIZE_VALUE))

    def _validate_file_exists(self, store_file_path: str) -> None:
        if(not os.path.isfile(store_file_path)):
            raise FileAbsentException(store_file_path)

    def _pre_normalize(self, img: Image.Image) -> np.ndarray:
        img = np.array(img)
        img = (img - Normalize.SYMMETRIC_VALUE.value) / Normalize.SYMMETRIC_VALUE.value
        return img[np.newaxis, ...]

    def _postprocess(self, color_img: np.ndarray, store_file_path: str, message_id: int) -> str:
        log.info("postprocess 시작")
        color_img = self._normalize(color_img)
        colored_file_path = self._save_process(color_img, store_file_path)
        self.storage_handler.upload_img(colored_file_path, store_file_path)
        self.storage_handler.changeClothesResultStatus(store_file_path, InferenceStatus.FINISH.name, InferenceStatus.FINISH.value, message_id)        
        self.storage_handler.changeCommonLogStatus(message_id, inference_end_time = datetime.now())
        log.info("postprocess 완료")
        return colored_file_path

    def _normalize(self, image: np.ndarray) -> np.ndarray:
        log.info("normalize 시작")
        color_img = (image + 1) / 2.0
        log.info("normalize 완료")
        return (color_img[ZERO_IDX] * Normalize.MIN_MAX_VALUE.value).astype(np.uint8)
    
    def _save_process(self, image: np.ndarray, store_file_path: str) -> str:
        log.info("추론 결과 이미지 로컬에 save processs 시작")
        colored_file_path = os.path.join(RESULT_RELATIVE_PATH, store_file_path)
        self._validate_data_exists(image)
        
        color_img = Image.fromarray(image)
        color_img.save(colored_file_path)
        log.info("추론 결과 이미지 로컬에 save processs 완료")
        return colored_file_path
        
    def _validate_data_exists(self, image: np.ndarray):
        if(image is None):
            raise DataAbsentException()