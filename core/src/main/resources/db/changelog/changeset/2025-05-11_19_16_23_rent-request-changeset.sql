alter table rent_request add column resolved_at timestamptz;
alter table rent_request add column created_at date not null default now();
ALTER TABLE rent_request
  ADD COLUMN status varchar(32)
    DEFAULT 'PENDING'  NOT NULL;