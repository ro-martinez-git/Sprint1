
-- Inserts de los clientes
INSERT INTO clientes (username,password, firstname,lastname,role)
VALUES ('flazzaroni', '$2a$10$3/5ukbr7EaALD6QVn7h5r..NUIABND14nw6lPOmketmj/d8NMrLFu', 'Franco', 'Lazzaroni', 'EMPLEADO');

-- Inserts de las personas
INSERT INTO people (dni, name, last_name, birth_date, email) VALUES
(12345678, 'Juan', 'Perez', '1982-11-10', 'juanperez@gmail.com'),
(87654321, 'Maria', 'Lopez', '1985-05-01', 'marialopez@gmail.com'),
(11223344, 'Carlos', 'Gomez', '1990-03-15', 'carlosgomez@gmail.com'),
(22334455, 'Ana', 'Martinez', '1992-07-22', 'anamartinez@gmail.com'),
(33445566, 'Luis', 'Garcia', '1988-12-05', 'luisgarcia@gmail.com');



-- Inserts de las formas de pago
INSERT INTO payment_methods (type, number_card, dues )
VALUES ('CREDIT', '1234-1234-1234-1234', 6 ), ('DEBIT', '2234-1234-1234-1234', 1 ), ('CREDIT', '1234-1234-1234-8889', 3 );

-- Inserts de las reservas de hotel Booking
INSERT INTO bookings (destination, date_from, date_to, hotel_code, people_amount, room_type, payment_methods_id, clientes_id, amount, creation_date)
VALUES ('Cataratas Hotel', '2025-02-10', '2025-03-20', 'CH-0002', 2, 'DOBLE', 1, 1, 478800, '2025-03-20' );

-- Inserts de los Hoteles
INSERT INTO hotels (hotel_code, hotel_name, destination, room_type, amount, date_from, date_to, reserved, bookings_id)
VALUES
    ('CH-0002', 'Cataratas Hotel', 'Puerto Iguazú', 'Doble', 6300, '2025-02-10', '2025-03-20', 'SI',1),
    ('CH-0003', 'Cataratas Hotel 2', 'Puerto Iguazú', 'Triple', 8200, '2025-02-10', '2025-03-23', 'NO',null),
    ('HB-0001', 'Hotel Bristol', 'Buenos Aires', 'Single', 5435, '2025-02-10', '2025-03-19', 'NO',null),
    ('BH-0002', 'Hotel Bristol 2', 'Buenos Aires', 'Doble', 7200, '2025-02-12', '2025-04-17', 'NO',null),
    ('SH-0002', 'Sheraton', 'Tucumán', 'Doble', 5790, '2025-04-17', '2025-05-23', 'NO',null),
    ('SH-0001', 'Sheraton 2', 'Tucumán', 'Single', 4150, '2025-01-02', '2025-02-19', 'NO',null),
    ('SE-0001', 'Selina', 'Bogotá', 'Single', 3900, '2025-01-23', '2025-11-23', 'NO',null),
    ('SE-0002', 'Selina 2', 'Bogotá', 'Doble', 5840, '2025-01-23', '2025-10-15', 'NO',null),
    ('EC-0003', 'El Campín', 'Bogotá', 'Triple', 7020, '2025-02-15', '2025-03-27', 'NO',null),
    ('CP-0004', 'Central Plaza', 'Medellín', 'Múltiple', 8600, '2025-03-01', '2025-04-17', 'NO',null),
    ('CP-0002', 'Central Plaza 2', 'Medellín', 'Doble', 6400, '2025-02-10', '2025-03-20', 'NO',null),
    ('BG-0004', 'Bocagrande', 'Cartagena', 'Múltiple', 9370, '2025-04-17', '2025-06-12', 'NO',null);


INSERT INTO booking_people (bookings_id, people_id) VALUES
(1, 1),(1, 2);

INSERT INTO flight_reservations (amount, date_from, date_to, seat_type, clientes_id, payment_method_id, destination, flight_number, origin, creation_date)
VALUES (150000, '2025-02-10', '2025-03-20', 'Economy', 1, 3, 'Puerto Iguazú','BAPI-1235', 'Buenos Aires', '2025-03-20') ;
    -- Inserts de la tabla flights
INSERT INTO flights (flight_number, origin, destination, seat_type, amount, date_from, date_to,reserved, flight_reservations_id)
VALUES
('BAPI-1235', 'Buenos Aires', 'Puerto Iguazú', 'Economy', 6500, '2025-02-10', '2025-02-15','SI', 1),
    ('PIBA-1420', 'Puerto Iguazú', 'Bogotá', 'Business', 43200, '2025-02-10', '2025-02-20', 'NO', NULL),
    ('PIBA-1420', 'Puerto Iguazú', 'Bogotá', 'Economy', 25735, '2025-02-10', '2025-02-21', 'NO', NULL),
    ('BATU-5536', 'Buenos Aires', 'Tucumán', 'Economy', 7320, '2025-02-10', '2025-02-17', 'NO', NULL),
    ('TUPI-3369', 'Tucumán', 'Puerto Iguazú', 'Business', 12530, '2025-02-12', '2025-02-23', 'NO', NULL),
    ('TUPI-3369', 'Tucumán', 'Puerto Iguazú', 'Economy', 5400, '2025-01-02', '2025-01-16', 'NO', NULL),
    ('BOCA-4213', 'Bogotá', 'Cartagena', 'Economy', 8000, '2025-01-23', '2025-02-05', 'NO', NULL),
    ('CAME-0321', 'Cartagena', 'Medellín', 'Economy', 7800, '2025-01-23', '2025-01-31', 'NO', NULL),
    ('BOBA-6567', 'Bogotá', 'Buenos Aires', 'Business', 57000, '2025-02-15', '2025-02-28', 'NO', NULL),
    ('BOBA-6567', 'Bogotá', 'Buenos Aires', 'Economy', 39860, '2025-03-01', '2025-03-14', 'NO', NULL),
    ('BOME-4442', 'Bogotá', 'Medellín', 'Economy', 11000, '2025-02-10', '2025-02-24', 'NO', NULL),
    ('MEPI-9986', 'Medellín', 'Puerto Iguazú', 'Business', 41640, '2025-04-17', '2025-05-02', 'NO', NULL);




    INSERT INTO flight_reservations_people (flight_reservations_id, people_id) VALUES
    (1, 1);















