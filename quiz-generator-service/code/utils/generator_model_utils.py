import os
import sys
import openai
import json

from code.exceptions.openai_exceptions import MessageIsEmptyException, InvalidTextOnImageException

openai.api_key = os.getenv("OPENAI_API_KEY")

def get_questions_from_model(message: str) -> str:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.

    Returns:
        JSON: Question from the model
    Raises:
        MessageIsEmptyException: If the 'message' argument is None.
    """
    
    SYSTEM_HELPER_MESSAGE = """
    You are an English grammar tutor. Given a topic, you will create a grammar quiz with 10 questions and explanations.
    The user input is a topic from a textbook page for studying grammar.
    You have to create 1 grammar question from your knowledge base, that represents this topic.
    Every question has to have 2+ possible answers. You have to specify correct answer index.
    At the end you must specify the short reason (10-15 words), why was that answer chosen gramatically.

    Example of correct question with topic (preposition - in/at/on):

    {
        question: There is Amy ___ the room.
        answers: ['in', 'at', 'on']
        correct_answer_index: 1
        reason: We choose in because Amy is inside of the room (inside of the some place)
    }
    """

    functions = [{
            "name": "get_question",
            "description": "Create 1 grammatical question. \
                            Every question has to have 3 possible answers.\
                            Specify correct answer \
                            And in the end you must specify the short reason (10-15 words), why was that answer chosen grammatically.",
            "parameters": {
                "type": "object",
                "properties": {
                    "question": {
                        "type": "string",
                        "description": "question"
                    },
                    "answers": {
                        "type": "string",
                        "description": "3 possible answers"
                    },
                    "correct_answer_index": {
                        "type": "integer",
                        "description": "correct answer index"
                    },
                    "reason": {
                        "type": "string",
                        "description": "short reason based on grammar (< 10 words)"
                    }
                }
            },
            "required": ["question", "answers", "correct_answer_index", "reason"]
        }
    ]

    response = get_response_from_model(message, SYSTEM_HELPER_MESSAGE, functions)

    return response

def get_topic_from_model(message: str) -> str:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.

    Returns:
        JSON: Defined topic from model
    Raises:
        MessageIsEmptyException: If the 'message' argument is None.
    """


    SYSTEM_HELPER_MESSAGE = """
    You are an English grammar tutor. Given a text, you have to define whas is the topic presented in the text.

    Example of correct answer:

    {topic: preposition}`
    """

    if message is None:
        raise MessageIsEmptyException
    
    functions = [
        {
            "name": "get_topic",
            "description": "get topic defined by the text",
            "parameters": {
                "type": "object",
                "properties": {
                    "topic": {
                        "type": "string",
                        "description": "topic title"
                    }
                }                  
            }
        },
    ]
    
    response =  get_response_from_model(message, SYSTEM_HELPER_MESSAGE, functions, 0.1)

    return response['topic']

    
def get_response_from_model(message, system_helper_message, functions, temperature=0.5) -> dict:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.

    Returns:
        str: The generated response from the model, including grammar questions and explanations.
    Raises:
        MessageIsEmptyException: If the 'message' argument is None.

    Model response form with function use:
        "choices": [
            "message": {
                "role": "assistant",
                "content": null,
                "function_call": {
                    "name": "get_answers",
                    "arguments": some JSON
                }
            },
            "finish_reason": "function_call"
            },
            ...  
        ],
        'usage': <OpenAIObject at 0x7f69be19eb10> JSON: {
            "prompt_tokens": 961,
            "completion_tokens": 773,

            "total_tokens": 1734
        }

    Model response form without function use
        "choices": [
                "message": {
                    "role": "assistant",
                    "content": some plain content,
                },
                ...  
        ],
        'usage': <OpenAIObject at 0x7f69be19eb10> JSON: {
            "prompt_tokens": 961,
            "completion_tokens": 773,
            "total_tokens": 1734
        }
    """

    GPT_MODEL = "gpt-3.5-turbo"
    MAX_TOKENS = 2000

    if message is None:
        raise MessageIsEmptyException

    response = openai.ChatCompletion.create(
        model=GPT_MODEL,
        messages=
        [
                {"role": "assistant", "content": system_helper_message},
                {"role": "user", "content": message }
        ],
        max_tokens=MAX_TOKENS,
        temperature=temperature,
        functions=functions,
        function_call="auto",
    )
    
    response_data = response.to_dict()
    
    # Access specific fields in the response
    function_call = response_data['choices'][0]['message'].get('function_call')
    
    if function_call:
        res = function_call['arguments'].strip()
        return json.loads(res)
    else:
        raise InvalidTextOnImageException 
    