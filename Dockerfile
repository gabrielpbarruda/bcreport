FROM adoptopenjdk:11-jre-openj9

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar

ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar app.jar

EXPOSE 8080