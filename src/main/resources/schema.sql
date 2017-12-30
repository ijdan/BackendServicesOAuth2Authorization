CREATE TABLE IF NOT EXISTS services_credentials
	(id varchar(36), name varchar(120), client_id varchar(36), client_secret varchar(36));

CREATE TABLE IF NOT EXISTS services_authorization
	(id varchar(36), client_id varchar(36), producer_id varchar(36), scope varchar(10));