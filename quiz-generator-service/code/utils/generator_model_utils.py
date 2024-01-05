import json
import os
import sys
from typing import List

import openai

from code.exceptions.openai_exceptions import MessageIsEmptyException, InvalidTextOnImageException
from code.models.question import QuestionModel
from code.models.topic import TopicModel

openai.api_key = os.getenv("OPENAI_API_KEY")


def get_questions_from_model(message: str) -> QuestionModel:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.

    Returns:
        JSON: Question from the model
    Raises:
        MessageIsEmptyException: If the 'message' argument is None.
    """

    system_helper_message = """
    You are an English grammar tutor. The user input is a topic from a textbook page for studying grammar.
    You have to create 1 grammar question from your knowledge base, that represents this topic.
    Every question has to have 2+ possible answers. You have to specify correct answer index.
    At the end you must specify the short reason (10-15 words), why was that answer chosen grammatically.

    Example of correct question with topic (preposition - in/at/on):

    {
        question: There is someone ___ the room.
        answers: ['in', 'at', 'on']
        correct_answer_index: 1
        reason: We choose in because someone is inside of the room (inside of the some place)
    }
    """

    functions = [{
            "name": "get_question",
            "description": "Create 1 grammatical question. \
                            Every question has to have 3 possible answers.\
                            Specify correct answer \
                            And in the end you must specify the short reason (10-15 words),\
                            why was that answer chosen grammatically.",
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

    temperature = 0.7

    response = get_response_from_model(message, system_helper_message, functions, temperature)

    return QuestionModel(**response)


def get_topic_from_model(message: str) -> TopicModel:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.

    Returns:
        JSON: Defined topic from model
    Raises:
        MessageIsEmptyException: If the 'message' argument is None.
    """

    system_helper_message = """
    You are an English grammar tutor. Given a text, you have to realize what is the grammar unit(topic)
    presented in the text.
    "PartsofSpeech":["Nouns","Pronouns","Verbs","Adjectives","Adverbs","Conjunctions","Prepositions","Interjections"],
    "SentenceStructure":["SubjectandPredicate","Simple,Compound,andComplexSentences","DependentandIndependentClauses"],
    "Tenses":["Present","Past","Future","PresentContinuous","PastContinuous","FutureContinuous","PresentPerfect","PastPerfect","FuturePerfect"],
    "Articles":["DefiniteArticle(The)","IndefiniteArticle(A/An)"],
    "Pronouns":["PersonalPronouns","PossessivePronouns","ReflexivePronouns"],
    "Nouns":["SingularandPluralNouns","CountableandUncountableNouns"],
    "AdjectivesandAdverbs":["ComparativeandSuperlativeForms","ProperUseofAdjectivesandAdverbs"],
    "Prepositions":["CommonPrepositions"],
    "Conjunctions":["CoordinatingConjunctions","SubordinatingConjunctions"],
    "DirectandIndirectSpeech":["ReportingStatementsandQuestions"],
    "ActiveandPassiveVoice":["FormingPassiveSentences"],
    "ModalVerbs":["Can,could,may,might,shall,should,will,would,must"],
    "RelativeClauses":["DefiningandNon-definingClauses"],
    "Subject-VerbAgreement":["Ensuringtheverbagreeswiththesubjectinnumberandperson"],
    "ConditionalSentences":["Zero,First,Second,ThirdConditional"],
    "GerundsandInfinitives":["Verbsusedasnouns"],
    "WordOrder":["Subject-Verb-Object(SVO)structure"],
    "Negation":["Negativesentencesandquestions"],
    "QuestionFormation":["Yes-NoQuestionsandWh-Questions"],
    "Punctuation":["Properuseofcommas,periods,semicolons,colons,andquotationmarks"]
    """

    if message is None:
        raise MessageIsEmptyException

    functions = [
        {
            "name": "get_topic",
            "description": "get topic",
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

    temperature = 0.2
    response = get_response_from_model(message, system_helper_message, functions, temperature)

    return TopicModel(**response)


def get_response_from_model(message: str, system_helper_message: str,
                            functions: List[dict], temperature: float = 0.5) -> dict:
    """
    Get a response from the GPT-x model based on user input.

    Args:
        message (str): The user's input text, which contains grammar rules and examples.
        system_helper_message (str): Message for system for role understanding
        functions (dict): Functions to OpenAI api for grouped model output
        temperature (int): Temperature for better/lower response quality

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
            "finish_reason": "function_call",
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

    print(f"Temperature: {temperature}", file=sys.stderr)
    gpt_model = "gpt-3.5-turbo"
    max_tokens = 2000

    if message is None:
        raise MessageIsEmptyException

    response = openai.ChatCompletion.create(
        model=gpt_model,
        messages=
        [
                {"role": "assistant", "content": system_helper_message},
                {"role": "user", "content": message}
        ],
        max_tokens=max_tokens,
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
