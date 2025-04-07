create table if not exists roles(
    id int primary key,
    role varchar(64) not null
);

create sequence if not exists roles_seq
    start with 1
    increment by 50
    cache 10;

