FROM python:3.11.5

WORKDIR /app
COPY requirements.txt .

RUN pip install --upgrade pip && \
    pip install -r requirements.txt

ENV PYTHONPATH=$pwd:$PYTHON

CMD ["pytest", "test"]