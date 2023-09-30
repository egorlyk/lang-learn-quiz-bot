import os
from typing import Tuple

from flask import Flask, request

from utils.text_extract_model import extract
from exceptions.image_exceptions import ImageNotFoundException

app = Flask(__name__)

ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}

app.config.update(
    UPLOAD_FOLDER = 'samples',
)

def allowed_file(filename) -> bool:
    """
    Checks if a given filename has an allowed file extension.

    Args:
        filename (str): The filename to be checked.

    Returns:
        bool: True if the filename has an allowed extension; False otherwise.
    """
    
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route('/extract', methods=['POST'])
def extract_text_handler() -> str:
    img = request.files.get("ext-img")
    if img is None:
        return "Send images with ext-img key name", 400
    
    filename = img.filename
    if not allowed_file(filename):
        return "The file extension isn't correct. Use only .png, .jpg or .jpeg", 415
    
    upload_folder = app.config['UPLOAD_FOLDER']
    if not os.path.exists(upload_folder):
        os.mkdir(upload_folder)

    relative_filepath = os.path.join(upload_folder, filename)
    img.save(relative_filepath)

    res =  extract(relative_filepath)
    os.remove(relative_filepath)
    return res
