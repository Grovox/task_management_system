FROM gradle:8.11.1-jdk17
COPY --chown=gradle . /app
WORKDIR /app
CMD ["gradle", "--stacktrace", "bootRun"]