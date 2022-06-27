FROM openjdk:17
EXPOSE 8080
ADD target/e-commerce.jar e-commerce-docker.jar
ENTRYPOINT ["java","-jar","/e-commerce.jar"]

