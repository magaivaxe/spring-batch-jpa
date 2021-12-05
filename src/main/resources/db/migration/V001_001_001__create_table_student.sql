CREATE TABLE student (
  id UUID NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  birth_date TIMESTAMP NOT NULL,
  PRIMARY KEY  (id)
);