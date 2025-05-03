alter table offers drop column category;

alter table offers add column category_id int not null references categories(id) default 1;