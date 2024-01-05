from typing import List

from marshmallow import Schema, fields, post_load, validate


class QuestionValidator(Schema):
    question = fields.Str(required=True, validate=validate.Length(min=1))
    answers = fields.List(fields.Str(), validate=validate.Length(min=2))
    correct_answer_index = fields.Integer(strict=True, required=True, validate=validate.Length(min=0))
    reason = fields.Str(required=True, validate=validate.Length(min=1))

    @post_load
    def create_question(self, data):
        return QuestionModel(**data)


class QuestionModel:
    question: str
    answers: List[str]
    correct_answer_index: int
    reason: str

    def __init__(self, question, answers, correct_answer_index, reason):
        self.question = question
        self.answers = answers
        self.correct_answer_index = correct_answer_index
        self.reason = reason

    def __eq__(self, other):
        return self.question == other.question and \
            self.answers == other.answers and \
            self.correct_answer_index == other.correct_answer_index and \
            self.reason == other.reason
