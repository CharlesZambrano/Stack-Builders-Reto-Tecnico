FROM eclipse-temurin:21-jre-alpine
COPY build/libs/hackernews-V1.jar /app/hackernews-V1.jar
ENTRYPOINT [ "java", "-Dspring.profiles.active=dev", "-jar", "/app/hackernews-V1.jar" ]
EXPOSE 8080
RUN apk --update --no-cache add curl
HEALTHCHECK --interval=1m --timeout=30s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
LABEL version="0.1" \
    description="Hacker News microservice using Postgres including Docker containers and health check test"
