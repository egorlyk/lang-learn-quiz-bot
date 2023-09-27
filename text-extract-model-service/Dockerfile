FROM python:3.11.5

WORKDIR /app
COPY requirements.txt .
# COPY code/* /code/python/
# COPY samples/* /code/python/samples/

RUN apt update
RUN apt install tesseract-ocr ffmpeg libsm6 libxext6  -y
RUN pip install --upgrade pip && \
    pip install -r /app/requirements.txt

ENV FLASK_APP=/app/code/server.py
ENV FLASK_RUN_HOST=0.0.0.0
EXPOSE 5000

CMD ["flask", "run"]