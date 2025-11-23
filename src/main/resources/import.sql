-- I used this in the test too, of course this would not be here in production, but for this exercise it's ok

-- USERS
INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (1, 'David', 100.0);
INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (2, 'Arnold', 200.0);
INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (3, 'Ilona', 150.0);


-- PAYMENT METHODS
INSERT INTO payment_methods (user_id, name) VALUES (1, 'My bank account');
INSERT INTO payment_methods (user_id, name) VALUES (1, 'My mom account');
INSERT INTO payment_methods (user_id, name) VALUES (2, 'Work account');
INSERT INTO payment_methods (user_id, name) VALUES (3, 'My bank account');
INSERT INTO payment_methods (user_id, name) VALUES (3, 'Secret account');
