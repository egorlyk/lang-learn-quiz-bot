import openai
from flask import Flask, request
from code.utils.generator_model_utils import *
from code.exceptions.openai_exceptions import InvalidTextOnImageException

app = Flask(__name__)

@app.route('/', methods=['POST'])
def generator_model_handler():
    data = request.get_json()
    message = str(data.get('message'))

    if not message:
        return "The message is missing in the body", 400
    
    topic = get_topic_from_model(message)

    return get_questions_from_model(topic)

@app.errorhandler(openai.error.RateLimitError)
def handle_rate_limit_error(e: Exception):
    return "You exceeded your current quota, please check your plan and billing details", 429

@app.errorhandler(openai.error.InvalidRequestError)
def handle_invalid_request_error(e: Exception):
    return "This is not a chat model and thus not supported in the v1/chat/completions endpoint", 429

@app.errorhandler(openai.error.OpenAIError)
def handle_api_error(e: openai.error.OpenAIError):
    return e, e.http_status

@app.errorhandler(InvalidTextOnImageException)
def handle_api_error(e: InvalidTextOnImageException):
    return e.message, 400
