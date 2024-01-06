class TextModel:
    text: str

    def __init__(self, text):
        self.text = text

    def __eq__(self, other):
        return self.text == other.text
