# AlertsFilter

This program reads data from kafka topic in Avro format, leaves only the necessary fields and publishes them in another Kafka topic in Avro format.

Structure of data:
```
{
  "last_reported": 1593694561,
  "station_id": "3",
  "num_ebikes_available": 0,
  "num_bikes_available": 21,
  "num_docks_available": 13,
  "station_status": "active",
  "capacity": 35
}
```

Kafka message has KEY (string) equals "station_id" and VALUE which stores all data

## Installation

Clone the repository 
```bash
https://gitlab.com/Yulia_Krawtschenko/bikerenting.git
cd bikerenting/
git checkout develop
cd alerts_filter
```

Run the command in this folder:

```bash
docker build -t alerts_filter .
```
And then:

```bash
docker run --rm --name alerts_filter alerts_filter
```
