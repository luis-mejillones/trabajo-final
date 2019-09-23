DROP KEYSPACE IF EXISTS omega;

CREATE KEYSPACE omega
  WITH REPLICATION = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
  };

USE omega;

CREATE TABLE kudos (
    id text PRIMARY KEY,
    topic text,
    date text,
	place text,
    content text,
    target_id int,
    source_id int
);

