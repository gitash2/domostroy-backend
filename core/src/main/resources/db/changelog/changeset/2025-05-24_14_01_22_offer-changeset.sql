alter table offers
    add column is_banned boolean not null default false;
alter table offers
    add column ban_reason varchar(128) default null;