# Timeline-Service

## 1. Concept
Store a message in timeline
1) Input is Json document with Timestamp.
2) Managing timelines with spcific key(Map<Key, Timeline>).
3) Maintain 

## 2. System Architecture
<div>
  <img src="https://user-images.githubusercontent.com/24906833/92566046-9bcb8f00-f2b6-11ea-8b0b-9f03875cebf6.png">
</div>

## 3. System Spec.
> 1) Store a data in memory.
> > timelien is a TreeMap
> 2) Stored data is replicated.
> > replication with event bus (RabbitMQ)
