create table if not exists user_verification(
    id bigint not null primary key,
    password varchar(69) not null,
    confirmation_code varchar(6) not null,
    expires_at timestamp not null
);