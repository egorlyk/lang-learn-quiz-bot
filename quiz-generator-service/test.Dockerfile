FROM python:3.11.5

WORKDIR /app
COPY test-requirements.txt .

RUN pip install --upgrade pip && \
    pip install -r test-requirements.txt

ENV PYTHONPATH=$pwd:$PYTHON

CMD ["pytest", "test"]