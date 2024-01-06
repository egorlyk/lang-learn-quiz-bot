from flask import Flask, request

from code.exceptions.file_exceptions import FileValidationException, RequiredFileNotProvidedException
from code.models.text_model import TextModel
from code.utils.file_util import FileUtil
from code.utils.text_extract_util import extract

app = Flask(__name__)


@app.route('/extract', methods=['POST'])
def extract_text_handler() -> TextModel:
    file = request.files.get("ext-img")
    if file is None:
        raise RequiredFileNotProvidedException("Send images with ext-img key name")

    file_util = FileUtil(file)
    relative_filepath = file_util.save()

    res = extract(relative_filepath)
    file_util.remove_file()

    return res


@app.errorhandler(FileValidationException)
def handle_api_error(e: FileValidationException):
    return e.message, 415


@app.errorhandler(RequiredFileNotProvidedException)
def handle_api_error(e: RequiredFileNotProvidedException):
    return e.message, 400
