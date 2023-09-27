FROM python:3.11.5

WORKDIR /app
COPY requirements.txt .

RUN pip install --upgrade pip && \
    pip install -r /app/requirements.txt

ENV FLASK_APP=/app/code/server.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=5050
EXPOSE 5050

CMD ["flask", "run"]