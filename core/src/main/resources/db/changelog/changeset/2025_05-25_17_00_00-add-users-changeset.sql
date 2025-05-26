insert into users(id, email, password, first_name, last_name, phone_number, role_id, created_at)
values (nextval('users_seq'),
        'darthveider36@gmail.com',
        '$2a$10$a0p2/l3Xx5Zu91E0DvGgDeHD6CPrj/NxintPQi9dHTMfrnUE54ptO',
        'igor',
        'pustilnik',
        9805553535,
        1,
        now());