FROM adoptopenjdk/openjdk8-openj9:alpine
RUN mkdir /app
CMD ["java", "-cp", "app/*", "be.sourcedbvba.restbucks.RestbucksApplicationKt"]
COPY build/dependencies/third-party /app
COPY build/dependencies/app/* /app/

