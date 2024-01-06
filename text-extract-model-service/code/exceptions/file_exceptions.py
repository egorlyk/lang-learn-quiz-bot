class FileValidationException(Exception):
    def __init__(self, message=None):
        if message is None:
            self.message = "File validation error"
        else:
            self.message = f"File validation error: {message}"
        super().__init__(self.message)


class RequiredFileNotProvidedException(Exception):
    def __init__(self, message=None):
        if message is None:
            self.message = "Required file not provided error"
        else:
            self.message = f"Required file not provided error: {message}"
        super().__init__(self.message)
