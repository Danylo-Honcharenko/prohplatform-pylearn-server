FROM tomcat:latest
ADD target/progplatform.war /usr/local/tomcat/webapps/
RUN cd webapps.dist && cp -R * ../webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]