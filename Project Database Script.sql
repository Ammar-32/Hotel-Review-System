use sys;
create table hotels(
city varchar(30) NOT NULL,
hotel_name varchar(50) NOT NULL,
value_rating decimal NOT NULL,
cleanliness_rating decimal NOT NULL,
room_rating decimal NOT NULL,
location_rating decimal NOT NULL,
service_rating decimal NOT NULL,
overall_rating decimal NOT NULL
);

LOAD DATA LOCAL INFILE  'd:/hotels.csv'
INTO TABLE hotels
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

