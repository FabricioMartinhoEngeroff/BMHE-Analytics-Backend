# Usa uma imagem do OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR da aplicação para dentro do contêiner
COPY BMEH-Backend/target/BMEHApplication-1.0.0.jar app.jar

# Expõe a porta configurada no application.properties
EXPOSE 8090

# Define o perfil para rodar a aplicação com Docker
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
