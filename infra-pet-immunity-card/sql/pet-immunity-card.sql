create database infoPet;
show databases;
use infoPet;

CREATE TABLE pet (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name_pet VARCHAR(200) NOT NULL,
  breed VARCHAR(255) NOT NULL,
  birth_date DATE NOT NULL,
  number_chip VARCHAR(100) NOT NULL,
  name_tutor VARCHAR(100) NOT NULL,
  phone_number VARCHAR(13) NOT NULL,
  link_image VARCHAR(350) NOT NULL
);

CREATE TABLE vaccine (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name_vaccine VARCHAR(200) NOT NULL,
  date_application DATE NOT NULL,
  date_reapplication DATE NOT NULL,
  link_vaccine VARCHAR(400) NOT NULL
);

ALTER TABLE pet ADD FOREIGN KEY (person_id) REFERENCES person (person_id);
ALTER TABLE vaccine ADD FOREIGN KEY (pet_id) REFERENCES pet (pet_id);