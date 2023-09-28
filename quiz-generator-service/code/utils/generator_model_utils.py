import os
import sys
import openai
import json

from exceptions.openai_exceptions import MessageIsEmptyException

openai.api_key = os.getenv("OPENAI_API_KEY")

SYSTEM_HELPER_MESSAGE = """
You are an English grammar tutor. Given a text, you will create a grammar quiz with 10 questions and explanations.
The user input is a text from a textbook page for studying grammar.
It contains grammar rules and examples of their use in sentences.
You have to guess wich grammar topic is presented in the page.
After that you have to create 10 grammar questions from your knowledge base (not necessary from user text), that represents this topic.
Every question has to have 2+ possible answers. You have to specify correct answer index.
At the end you must specify the short reason (10-15 words), why was that answer chosen gramatically.

Example of correct question:

questions: [
    {
    question: There is Amy ___ the room.
    answers: ['in', 'at', 'on']
    correct_answer_index: 1
    reason: We choose in because Amy is inside of the room (inside of the some place)
    },
    ...
]
"""

GPT_MODEL = "gpt-3.5-turbo"
MAX_TOKENS = 2000
TEMPERATURE = 0.5

def get_response_from_model(message: str) -> str:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.

    Returns:
        str: The generated response from the model, including grammar questions and explanations.
    Raises:
        MessageIsEmptyException: If the 'message' argument is None.
    """

    if message is None:
        raise MessageIsEmptyException

    response = openai.ChatCompletion.create(
        model=GPT_MODEL,
        messages=
        [
                {"role": "assistant", "content": SYSTEM_HELPER_MESSAGE},
                {"role": "user", "content": message }
        ],
        max_tokens=MAX_TOKENS,
        temperature=TEMPERATURE,
        functions = 
        [
            {
                "name": "get_answers",
                "description": "Create 10 grammatical questions. \
                                Every question has to have 3 possible answers.\
                                Specify correct answer \
                                And in the end you must specify the short reason (10-15 words), why was that answer chosen grammatically.",
                "parameters": {
                    "type": "object",
                    "properties": {
                        "questions": {
                            "type": "array",
                            "description": "questions list with answers",
                            "items": {
                                "question": {
                                    "type": "string",
                                    "description": "question"
                                }, 
                                "answers": {
                                    "type": "array",
                                    "description": "3 possible answers"
                                },
                                "correct_answer_index": {
                                    "type": "integer",
                                    "description": "correct answer index"
                                },
                                "reason": {
                                    "type": "string",
                                    "description": "short reason (< 10 words)"
                                }
                            }
                        }
                    }                  
                }
            },
        ]
    )
    
    response_data = response.to_dict()

    # Access specific fields in the response
    function_call = response_data['choices'][0]['message'].get('function_call')
    if function_call:
        generated_text = function_call['arguments'].strip()
        res = json.loads(generated_text)['questions']
        print(res, file=sys.stderr)
        
    else:
        res = response_data['choices'][0]['message']['content']
    
    return res