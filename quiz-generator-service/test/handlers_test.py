import json
import unittest
from unittest.mock import patch

from code.models.question import QuestionModel
from code.models.topic import TopicModel
from code.server import generate_topic_handler, app, generate_question_handler


class ModelResponseMock:
    json: dict

    def __init__(self, json: dict) -> None:
        self.json = json

    def to_dict(self):
        return {
            "choices": [
                {
                    "message": {
                        "function_call": {
                            "arguments": json.dumps(self.json)
                        }
                    }
                }
            ]
        }


class TestTopicHandler(unittest.TestCase):
    @patch('openai.ChatCompletion.create')
    def test_proper_text_should_return_topic(self, mock_create):
        expected_value_json = {"topic": "past particle"}

        expected_value = TopicModel(**expected_value_json)

        with app.test_request_context() as mock_context:
            mock_context.request.get_json = lambda: {"text": "some_text"}

            mock_create.return_value = ModelResponseMock(expected_value_json)

            result = generate_topic_handler()

            self.assertEqual(result, expected_value)


class TestQuestionHandler(unittest.TestCase):
    @patch('openai.ChatCompletion.create')
    def test_proper_topic_should_return_question(self, mock_create):
        expected_value_json = {
            "question": "There is someone ___ the room.",
            "answers": ['in', 'at', 'on'],
            "correct_answer_index": 1,
            "reason": "We choose in because someone is inside of the room (inside of the some place)"
        }

        expected_value = QuestionModel(**expected_value_json)

        with app.test_request_context() as mock_context:
            mock_context.request.get_json = lambda: {"topic": "some_topic"}

            mock_create.return_value = ModelResponseMock(expected_value_json)

            result = generate_question_handler()

            self.assertEqual(result, expected_value)
