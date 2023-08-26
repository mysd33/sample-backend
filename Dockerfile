FROM public.ecr.aws/amazoncorretto/amazoncorretto:17
RUN yum install -y shadow-utils
ARG USERNAME=app
ARG GROUPNAME=app
ARG UID=1000
ARG GID=1000
ARG JAR=sample-backend-0.1.0-SNAPSHOT.jar

RUN groupadd -g $GID $GROUPNAME && \
    useradd -m -s /bin/bash -u $UID -g $GID $USERNAME
RUN chown -R $USERNAME:$GROUPNAME /tmp

USER $USERNAME
VOLUME [ "/tmp" ]
WORKDIR /home/$USERNAME/

COPY target/$JAR /home/$USERNAME/
ENTRYPOINT [ "java" ]
CMD [ "-jar", "echo $JAR" ]
EXPOSE 8080