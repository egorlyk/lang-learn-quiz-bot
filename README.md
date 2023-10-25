# Language learn quiz bot

[![Static Badge](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Static Badge](https://img.shields.io/badge/SpringBoot-3-green.svg)](https://docs.spring.io/spring-boot/docs/3.1.5/reference/html/)
[![Static Badge](https://img.shields.io/badge/Python-3.11-yellow.svg)](https://www.python.org/downloads/release/python-3110/)
[![Static Badge](https://img.shields.io/badge/Flask-3-orange.svg)](https://flask.palletsprojects.com/en/3.0.x/)
[![Static badge](https://img.shields.io/badge/Model-gpt--3.5--turbo-076E67)](https://openai.com/blog/gpt-3-5-turbo-fine-tuning-and-api-updates)


## Description
This is a project that helps people learn a language (currently only available in English) by generating quizzes with some possible answers.

Each quiz is based on a specific language topic (e.g. present perfect, passive, -ing/-ed endings, etc.).
To specify a topic, you must enter it after the /topic command ("Choose a topic" button in the main menu)
or analyze the image to get the topic from it (send an uncompressed photo after the /image command ("Load image" in the main menu)).
To get a question, you must send /question command ("Get question" button in main menu).

Available via Telegram Bot https://t.me/lang_learn_quiz_bot

## Demo

### Analyze image topic and get question

![Image_and_question](https://github.com/egorlyk/lang-learn-quiz-bot/assets/46978921/2c9b1edd-c465-4caa-9c0c-71819e80ec79)


### Manually choose topic and get question

![Topic_and_question](https://github.com/egorlyk/lang-learn-quiz-bot/assets/46978921/a8f896b1-74db-497d-8f03-dd692a964682)


### Get question on a previously chosen topic

![Question](https://github.com/egorlyk/lang-learn-quiz-bot/assets/46978921/d6729cea-047d-48a1-bbe9-b0f699c861b7)


## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

`OPENAI_API_KEY` - OpenAI API key https://platform.openai.com/account/api-keys

`TG_BOT_API` - Telegram bot API key (must be taken from Bot father https://t.me/BotFather)

`TG_BOT_USERNAME` - Telegram bot username (must be taken from Bot father https://t.me/BotFather)

## Run Locally

Clone the project

```bash
  git clone https://github.com/egorlyk/lang-learn-quiz-bot.git
```

Go to the project directory

```bash
  cd lang-learn-quiz-bot
```

Run docker compose in debug mode (currently only debug available)

```bash
 docker-compose --profile=debug up
```

## Test

```bash
  cd lang-learn-quiz-bot
  # Run docker compose in test mode
  docker-compose --profile=test up
```
    
## License

MIT License

Copyright (c) 2023

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.