FROM maven:3.6.3-jdk-8

RUN mkdir -p /usr/src/app/
WORKDIR /usr/src/app/

RUN git clone https://github.com/Borman22/alerts.git
WORKDIR /usr/src/app/alerts/
# COPY ./ ./

RUN mvn clean compile assembly:single

ENV APPLICATION_ID=AlertsFilter
ENV BROKERS=broker:9092
ENV SCHEMA_REGISTRY_URL=http://schema-registry:8081
ENV INPUT_TOPIC=initial_data_avro
ENV OUTPUT_TOPIC=alerts
ENV HDP_IP=10.132.0.23
ENV HDP_DOMAIN_NAME sandbox.hortonworks.com

CMD echo "$HDP_IP $HDP_DOMAIN_NAME" >> /etc/hosts && java -jar ./target/AlertsFilter-1.0-SNAPSHOT-jar-with-dependencies.jar --output_topic $OUTPUT_TOPIC --application_id $APPLICATION_ID --brokers $BROKERS --schema_registry_url $SCHEMA_REGISTRY_URL --input_topic $INPUT_TOPIC --output_topic $OUTPUT_TOPIC




