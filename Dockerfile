# 기본 이미지로 OpenJDK 11 사용
FROM openjdk:11

# JAR 파일 경로를 ARG로 설정
ARG JAR_FILE=target/*.war

# JAR 파일을 app.war로 복사
COPY ${JAR_FILE} /app.war

# 이미지 폴더를 /images로 복사
COPY images /images

# 컨테이너 실행 시 사용할 포트를 노출
EXPOSE 80

# 컨테이너 실행 시 사용할 ENTRYPOINT 설정
ENTRYPOINT ["java", "-Ddefault-file-path=/images", "-jar", "/app.war"]