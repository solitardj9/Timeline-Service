# Timeline-Service

## Store a message in timeline
1) Input is Json document.
2) 

## 1. Concept

## 2. System Architecture
<div>
  <img src="https://user-images.githubusercontent.com/24906833/92565657-f7494d00-f2b5-11ea-8d96-24e1c4cc4738.png">
</div>

## 3. System Spec.
> 1) Store a data in memory.
> > timelien is a TreeMap
> 2) Stored data is replicated.
> > replication with event bus (RabbitMQ)
