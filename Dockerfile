# Usa uma imagem leve com Java 17
FROM eclipse-temurin:17-jdk-jammy

# Cria um diretório para a aplicação
WORKDIR /app

# Copia o .jar gerado pelo Maven para dentro do container
COPY target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando que executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
