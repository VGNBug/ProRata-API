INSERT INTO prorata_user
    (prorata_user_id, email, password, first_name, last_name, address, postcode)
    VALUES(1, 'bob@test.com', 'password', 'Bob', 'Alison', '1 Test lane', 'TE12ST');
    
INSERT INTO subscription_type
    (subscription_type_id, name, rate)
    VALUES (1, 'standard', 0);
    
INSERT INTO subscription
    (subscription_id, start_date_time, end_date_time, prorata_user_id, subscription_type_id)
    VALUES (1, '2013-11-11 12:10:15', '2014-11-11 12:10:15', 1, 1);

INSERT INTO bank
    (bank_id, name, address, postcode, email)
    VALUES(1, 'Bank', '1b', 'TE11ET', 'b@t.com');

INSERT INTO employer
    (employer_id, name, office_address, office_postcode)
    VALUES(1, 'The Firm', 'Firm House, 11 Firm Street, Firmham, Firmhamshire', 'FI1 2RM');
    
INSERT INTO employment
    (employment_id, employer_id, prorata_user_id, hourly_rate, start_date, hours_per_week, name)
    VALUES(1, 1, 1, 11.50, '2013-11-11', 40, 'Employment with The Firm');

INSERT INTO employment_session
    (employment_session_id, prorata_user_id, start_time, end_time, location_id)
    VALUES(1, 1, '2013-11-11 13:00:00', '2013-11-11 19:00:00', 1);

INSERT INTO location
    (location_id, employment_id, x_coordinate, y_coordinate, radius)
    VALUES(1, 1, 52.628391, 1.294964, 100);

INSERT INTO location
    (location_id, employment_id, x_coordinate, y_coordinate, radius)
    VALUES(2, 1, 51.628391, 1.294964, 100);