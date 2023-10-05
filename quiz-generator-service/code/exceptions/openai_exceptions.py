class MessageIsEmptyException(Exception):
    message: str

    def __init__(self, message="Message is empty"):
        self.message = message
        super().__init__(self.message)

    def get_message(self) -> str:
        return self.message
    

class InvalidTextOnImageException(Exception):
    message: str

    def __init__(self, message="Invalid text on image"):
        self.message = message
        super().__init__(self.message)
    
    def get_message(self) -> str:
        return self.message
    
