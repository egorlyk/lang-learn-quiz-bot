class MessageIsEmptyException(Exception):
    def __init__(self, message="Message is empty"):
        self.message = message
        super().__init__(self.message)