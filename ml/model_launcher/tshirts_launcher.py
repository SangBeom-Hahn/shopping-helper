import os
import sys

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from utils import *
from exception import *
from model_launcher import *
from keras.models import load_model

class Tshirts_GAN(Base_GAN):
    
    def __init__(self) -> None:
        self.model = self._load_model()
    
    def _load_model(self):
        try:
            return load_model(TSHIRTS_MODEL_PATH)
        except Exception as e:
            print(e)
            raise NoSuchFileException()