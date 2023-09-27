import openai
from flask import Flask, request
from utils.generator_model_utils import get_response_from_model

app = Flask(__name__)

@app.route('/', methods=['POST'])
def generator_model_handler():
    data = request.get_json()
    message = str(data.get('message'))

    if not message:
        return "The message is missing in the body", 400
    
    return get_response_from_model(message)

@app.errorhandler(openai.error.RateLimitError)
def handle_rate_limit_error(e: Exception):
    return "You exceeded your current quota, please check your plan and billing details", 429

@app.errorhandler(openai.error.InvalidRequestError)
def handle_invalid_request_error(e: Exception):
    return "This is not a chat model and thus not supported in the v1/chat/completions endpoint", 429

@app.errorhandler(openai.error.OpenAIError)
def handle_api_error(e: openai.error.OpenAIError):
    return e.message, e.http_status
