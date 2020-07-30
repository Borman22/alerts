package poc.kafkastreams.alertsfilter;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;

import java.util.Collections;
import java.util.Properties;


public class Main {

    public static String applicationId = "AlertsFilter";
    public static String brokers = "localhost:9092";
    public static String schemaRegistryUrl = "http://localhost:8081";
    public static String inputTopic = "test_avro";
    public static String outputTopic = "alerts";


    public static void main(String[] args) {

        if(args.length == 1 && args[0].equals("--help"))
            System.out.println(
                            "--application_id [Application name. Uses like ConsumerGroup. Default = \"AlertsFilter\"]\n" +
                            "--brokers [Bootstrap server URL. Default = \"localhost:9092\"]\n" +
                            "--schema_registry_url [Schema registry URL. Default = \"http://localhost:8081\"]\n" +
                            "--input_topic [The topic from which the data is read. Default = \"test_avro\"]\n" +
                            "--output_topic [Topic to which data will be written. Default = \"alerts\"]\n");
        else if(args.length %2 != 0)
            System.out.println("Wrong parameter set. Use the --help key to see all options");
        else if(args.length > 0)
            for(int i = 0; i < args.length; i += 2){
                switch (args[i]){
                    case "--application_id" : applicationId = args[i+1]; break;
                    case "--brokers" : brokers = args[i+1]; break;
                    case "--schema_registry_url" : schemaRegistryUrl = args[i+1]; break;
                    case "--input_topic" : inputTopic = args[i+1]; break;
                    case "--output_topic" :  outputTopic = args[i+1]; break;
                    default:
                        System.out.println("Wrong parameter. Use the --help key to see all options");
                }
        }

        System.out.println("applicationId = " + applicationId);
        System.out.println("brokers = " + brokers);
        System.out.println("schemaRegistryUrl = " + schemaRegistryUrl);
        System.out.println("inputTopic = " + inputTopic);
        System.out.println("outputTopic = " + outputTopic);

        StreamsBuilder builder = new StreamsBuilder();

        Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);

        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        streamsConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);

        final Serde<GenericRecord> valueSerde = new GenericAvroSerde();
        valueSerde.configure(Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl), false);

        builder
                .stream(inputTopic, Consumed.with(Serdes.String(), valueSerde))
                .filter((k, v) -> (int)v.get("num_docks_available") <= 0 || (int)v.get("num_ebikes_available") + (int)v.get("num_bikes_available") >= (int)v.get("capacity"))
                .to(outputTopic);

        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
        streams.start();
    }
}
