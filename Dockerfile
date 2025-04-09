FROM ubuntu:latest
LABEL authors="ash"

ENTRYPOINT ["top", "-b"]