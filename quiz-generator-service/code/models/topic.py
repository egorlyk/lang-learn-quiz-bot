from marshmallow import Schema, fields, post_load, validate


class TopicValidator(Schema):
    topic = fields.Str(required=True, validate=validate.Length(min=1, max=25))

    @post_load
    def create_topic(self, data, **kwargs):
        return TopicModel(**data)


class TopicModel:
    topic: str

    def __init__(self, topic):
        self.topic = topic

    def __eq__(self, other):
        return self.topic == other.topic
