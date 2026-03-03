INSERT INTO customers (name, email, created_at) VALUES
                                                    ('Мерген Теміржан', 'mergen.temirzhan@example.com', CURRENT_TIMESTAMP),
                                                    ('Петр Петров', 'petr.petrov@example.com', CURRENT_TIMESTAMP),
                                                    ('Анна Смирнова', 'anna.smirnova@example.com', CURRENT_TIMESTAMP);


INSERT INTO orders (customer_id, amount, status, created_at) VALUES
                                                                 (1, 1500.50, 'NEW', CURRENT_TIMESTAMP),
                                                                 (1, 300.00, 'PAID', CURRENT_TIMESTAMP),
                                                                 (2, 999.99, 'NEW', CURRENT_TIMESTAMP),
                                                                 (3, 5000.00, 'CANCELLED', CURRENT_TIMESTAMP);