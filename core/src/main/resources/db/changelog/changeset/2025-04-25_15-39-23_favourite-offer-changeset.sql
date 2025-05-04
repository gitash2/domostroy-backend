create table if not exists favourites(
    user_id bigint not null references users(id),
    offer_id bigint not null references offers(id),
    primary key (user_id, offer_id)
);

