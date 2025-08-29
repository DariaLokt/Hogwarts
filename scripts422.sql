CREATE TABLE cars (
license_plate VARCHAR PRIMARY KEY,
make VARCHAR,
model VARCHAR,
price INTEGER
);

CREATE TABLE people (
name VARCHAR PRIMARY KEY,
age INTEGER,
drivers_license BOOLEAN,
car VARCHAR REFERENCES cars(license_plate)
);
