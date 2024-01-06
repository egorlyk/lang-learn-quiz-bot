import io
import unittest

import pytest
from werkzeug.datastructures import FileStorage

from code.exceptions.file_exceptions import FileValidationException
from code.utils.file_util import FileUtil


class TestFileUtils(unittest.TestCase):
    def test_proper_file_extension_should_pass_allowance(self):
        mock_file = FileStorage(
            stream=io.BytesIO(b'file content'),
            filename="img.jpg",
            content_type="multipart/form-data; boundary=<calculated when request is sent>",
        )

        FileUtil.validate_file(mock_file)

    def test_disallowed_file_extension_should_raise_an_error(self):
        mock_file = FileStorage(
            stream=io.BytesIO(b'file content'),
            filename="img.txt",
            content_type="multipart/form-data; boundary=<calculated when request is sent>",
        )

        with pytest.raises(FileValidationException):
            FileUtil.validate_file(mock_file)


if __name__ == '__main__':
    unittest.main()
