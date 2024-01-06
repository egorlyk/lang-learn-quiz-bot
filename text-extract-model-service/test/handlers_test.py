import unittest

import pytest

from code.exceptions.file_exceptions import RequiredFileNotProvidedException
from code.server import extract_text_handler, app


class TestFileUtils(unittest.TestCase):
    def test_missed_file_should_raise_an_exception(self):
        with app.test_request_context():
            with pytest.raises(RequiredFileNotProvidedException):
                extract_text_handler()


if __name__ == '__main__':
    unittest.main()
