from flask import Flask, request
from marshmallow import ValidationError

from code.models.question import QuestionValidator
from code.models.text import ImageTextModel, ImageTextValidator
from code.models.topic import TopicValidator
from code.utils.generator_model_utils import *

app = Flask(__name__)


@app.route('/topic', methods=['POST'])
def generate_topic_handler() -> TopicModel:
    data: ImageTextModel = ImageTextValidator().load(request.get_json())

    response = get_topic_from_model(data.text)
    print(f"Topic service response = {response}", file=sys.stderr)
    return response


@app.route('/question', methods=['POST'])
def generate_question_handler() -> QuestionModel:
    data: TopicModel = TopicValidator().load(request.get_json())

    response = get_questions_from_model(data.topic)
    print(f"Question service response = {response}", file=sys.stderr)
    return response


@app.errorhandler(openai.error.OpenAIError)
def handle_api_error(e: openai.error.OpenAIError):
    return e, e.http_status


@app.errorhandler(ValidationError)
def handle_api_error(e: ValidationError):
    return e.messages, 400
