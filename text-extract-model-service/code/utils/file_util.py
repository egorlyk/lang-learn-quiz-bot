import os

from werkzeug.datastructures import FileStorage

from code.exceptions.file_exceptions import FileValidationException

UPLOAD_FOLDER = "samples"


class FileUtil:
    file: FileStorage
    relative_filepath: str

    def __init__(self, file):
        self.validate_file(file)

        self.file = file
        if not os.path.exists(UPLOAD_FOLDER):
            os.mkdir(UPLOAD_FOLDER)

        self.relative_filepath = str(os.path.join(UPLOAD_FOLDER, file.filename))

    @staticmethod
    def validate_file(file: FileStorage) -> None:
        """
        Checks if a given file is valid.
        """

        allowed_extensions = {'png', 'jpg', 'jpeg'}

        filename = file.filename
        if not ('.' in filename and filename.rsplit('.', 1)[1].lower() in allowed_extensions):
            raise FileValidationException("File extension isn't allowed. Use png, jpg or jpeg")

    def remove_file(self) -> None:
        os.remove(self.relative_filepath)
        if os.path.exists(UPLOAD_FOLDER) and not len(os.listdir(UPLOAD_FOLDER)):
            os.rmdir(UPLOAD_FOLDER)

    def save(self) -> str:
        self.file.save(self.relative_filepath)
        return self.relative_filepath

    def get_relative_filepath(self) -> str:
        return self.relative_filepath
