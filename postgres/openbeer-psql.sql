
CREATE TABLE IF NOT EXISTS breweries (
  id decimal(10,0) NOT NULL,
  name varchar(100),
  address1 varchar(50),
  address2 varchar(50),
  city varchar(50),
  state varchar(50),
  code varchar(50),
  country varchar(50),
  phone varchar(50),
  website varchar(150),
  filepath varchar(150),
  description varchar(8192),
  add_user decimal(10,2),
  last_modified timestamp,
  PRIMARY KEY (id)
);

copy breweries from '/tmp/breweries.csv' with (FORMAT csv, HEADER 1);

CREATE TABLE IF NOT EXISTS beers (
  id decimal(10,0) NOT NULL,
  brewery_id decimal(10,0) NOT NULL REFERENCES breweries(id),
  name varchar(100),
  cat_id decimal(10,0) NOT NULL,
  style_id decimal(10,0) NOT NULL,
  abv decimal(10,2),
  ibu decimal(10,2),
  srm decimal(10,2),
  upc decimal(15,2),
  filepath varchar(150),
  description varchar(8192),
  add_user decimal(10,0),
  last_modified timestamp,
  UNIQUE (id,brewery_id)
);

copy beers from '/tmp/beers.csv' with (FORMAT csv, HEADER 1);

