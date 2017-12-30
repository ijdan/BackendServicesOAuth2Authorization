DELETE FROM services_credentials;
INSERT INTO services_credentials (id, name, client_id, client_secret, replaced, updated)
VALUES
	('4c9f95dc-415b-477f-bb57-09b2c038e38b', 'service_1', '9c851cdb-6301-4882-8741-04dce31cb117', '5513e387-a79e-402b-977a-97ce824335ac', '0', '2017-12-30'),
	('ea4d00bb-40d1-4416-999a-b42456cd5d02', 'service_2', 'da6ee56d-12e4-4c7d-809c-c7ee2e5dd00d', '3bfa165a-fdb0-44d5-a2d1-233e200c581e', '0', '2017-12-30'),
	('afc40193-bced-477e-9804-e61f86fc6f95', 'service_3', 'b653dd85-5e2d-4872-94df-6954b2ba0747', 'b0646a26-c65d-4de3-b43b-21843bf441af', '0', '2017-12-30'),
	('37232410-1428-4fb2-95a4-934ccc8dc660', 'service_4', 'fd06f086-1145-4c9f-a6aa-012a710cefbf', 'f8ecffd5-5966-4743-b97a-fd7f5ab35d0d', '1', NOW()-4),
	('37232410-1428-4fb2-95a4-934ccc8dc660', 'service_4', 'fd06f086-1145-4c9f-a6aa-012a710cefbf', '4d1463c3-ef70-44e3-9b15-1c97b09129ba', '0', '2017-12-30'),
	('b32c1cf5-db6c-4d06-9919-dd11f72dc846', 'service_5', '4379b2eb-4e8b-4eda-a0b7-1f4da57ca064', '1b16453b-0892-4682-8e26-ba911768d47e', '0', '2017-12-30');

DELETE FROM services_authorization;
INSERT INTO services_authorization (id, client_id, producer_id)
VALUES 
	('4102fe51-0ca5-4375-8f0b-c36d6ac9dc7c', '9c851cdb-6301-4882-8741-04dce31cb117', 'da6ee56d-12e4-4c7d-809c-c7ee2e5dd00d'),
	('78d37282-152a-45e4-b6bf-d89bcc29f084', 'fd06f086-1145-4c9f-a6aa-012a710cefbf', 'da6ee56d-12e4-4c7d-809c-c7ee2e5dd00d'),
	('e843776a-00d5-42f3-a805-07a2f1540a73', 'fd06f086-1145-4c9f-a6aa-012a710cefbf', '4379b2eb-4e8b-4eda-a0b7-1f4da57ca064'),
	('1b02f64b-5d3f-41c4-8589-24c7e8012164', 'fd06f086-1145-4c9f-a6aa-012a710cefbf', 'b653dd85-5e2d-4872-94df-6954b2ba0747');