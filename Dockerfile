FROM maven:3.6.3-jdk-8

RUN mkdir -p /usr/src/app/
WORKDIR /usr/src/app/

COPY ./ ./

RUN mvn clean package

ENV APPLICATION_ID=AlertsFilter
ENV BROKERS=sandbox-hdp.hortonworks.com:6667
ENV SCHEMA_R_URL=http://sandbox-hdp.hortonworks.com:7788
ENV INPUT_TOPIC=initial_data_avro
ENV OUTPUT_TOPIC=alerts
ENV HDP_IP=10.132.0.23
ENV HDP_DOMAIN_NAME sandbox-hdp.hortonworks.com

CMD echo "$HDP_IP $HDP_DOMAIN_NAME" >> /etc/hosts && java -jar ./target/AlertsFilter-1.0-SNAPSHOT.jar --output_topic $OUTPUT_TOPIC --application_id $APPLICATION_ID --brokers $BROKERS --schema_registry_url $SCHEMA_R_URL --input_topic $INPUT_TOPIC --output_topic $OUTPUT_TOPIC




