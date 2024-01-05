from marshmallow import Schema, fields, post_load, validate


class ImageTextValidator(Schema):
    text = fields.Str(required=True, validate=validate.Length(min=1))

    @post_load
    def create_text(self, data, **kwargs):
        return ImageTextModel(**data)


class ImageTextModel:
    text: str

    def __init__(self, text):
        self.text = text

    def __eq__(self, other):
        return self.text == other.text
