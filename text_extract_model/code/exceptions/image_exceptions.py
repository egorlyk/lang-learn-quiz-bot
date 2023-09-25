class ImageNotFoundException(Exception):
    def __init__(self, message="No image found for this path"):
        self.message = message
        super().__init__(self.message)
