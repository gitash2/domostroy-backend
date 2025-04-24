create table if not exists users(
    id bigint primary key,
    email varchar(255) not null unique,
    password varchar(69) not null,
    first_name varchar(64) not null,
    last_name varchar(64),
    phone_number varchar(16),
    role_id int not null references roles(id),
    created_at timestamptz default now()
);

create sequence if not exists users_seq
    start with 1
    increment by 50
    cache 50;

