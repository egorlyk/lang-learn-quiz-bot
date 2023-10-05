import unittest
import json

from code.utils.generator_model_utils import get_response_from_model

from unittest.mock import Mock, patch

class BuiltinTest:
    json: dict

    def __init__(self, json: dict) -> None:
        self.json = json
    
    def to_dict(self):
        return self.json

class TestGetDataFromAPI(unittest.TestCase):
    @patch('openai.ChatCompletion.create')
    def test_proper_text_should_return_json(self, mock_create):
        expected_value = {
            "question": "I am going ___ a walk in the park.",
            "answers": ["on", "for", "to"],
            "correct_answer_index": 2,
            "reason": "We choose 'to' because 'going for a walk' is an idiomatic expression."
        }

        expected_value_json = {
            "choices": [
                {
                "message": {
                    "function_call": {
                            "arguments": json.dumps(expected_value)
                        }
                    }
                }
            ]
        }
        
        mock_create.return_value = BuiltinTest(expected_value_json)

        result = get_response_from_model("message", "system_helper_message", ["some function"])

        self.assertEqual(result, expected_value)

if __name__ == '__main__':
    unittest.main()