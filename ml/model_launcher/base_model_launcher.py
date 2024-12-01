import numpy as np
import os

from exception import *
from keras_preprocessing.image import load_img
from utils import *
from PIL import Image

class Base_GAN():
    
    def __init__(self) -> None:
        self.model = self._load_model()
    
    def _load_model(self):
        raise NotImplementedError(GAN_IMPLEMENTS_EXCEPTION_MESSAGE)

    def inference(self, store_file_path: str) -> str:
        img = self._preprocess(store_file_path)
        color_img = self.model.predict(img)
        return self._postprocess(color_img)

    def _preprocess(self, store_file_path):
        img = self._load_img(store_file_path)
        return self._pre_normalize(img)
        
    def _load_img(self, store_file_path): # "../Untitled.png"
        self._validate_file_exists(store_file_path)
        
        return load_img(store_file_path, target_size=(RESIZE_VALUE, RESIZE_VALUE))

    def _validate_file_exists(self, store_file_path):
        if(not os.path.isfile(store_file_path)):
            raise FileAbsentException(store_file_path)

    def _pre_normalize(self, img):
        img = np.array(img)
        img = (img - Normalize.SYMMETRIC_VALUE) / Normalize.SYMMETRIC_VALUE
        return img[np.newaxis, ...]

    def _postprocess(self, color_img):
        color_img = self._normalize(color_img)
        return self._save_process(color_img)

    def _normalize(self, image: np.ndarray) -> np.ndarray:
        color_img = (image + 1) / 2.0 # 정규화
        return (color_img[ZERO_IDX] * Normalize.MIN_MAX_VALUE).astype(np.uint8) # 0-255 범위로 변환

    def _save_process(self, image):
        save_path = "./result.png"
        color_img = Image.fromarray(image)
        color_img.save(save_path)
        return save_path