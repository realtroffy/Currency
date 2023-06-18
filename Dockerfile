FROM maven:3.8.6-jdk-11

COPY pom.xml /pom.xml
COPY src/ /src/

RUN mvn clean package -DskipTests

ENTRYPOINT ["java","-jar","/target/currency.jar"]