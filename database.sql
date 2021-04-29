create table "user"
(
    id     serial not null
        constraint user_pk
            primary key,
    number text   not null
);

alter table "user"
    owner to evgenysobko;

create table parking
(
    id             serial  not null
        constraint parking_pk
            primary key,
    total_cost     integer,
    user_id        integer not null
        constraint parking_user_id_fk
            references "user",
    arrival_time   integer not null,
    departure_time integer
);

alter table parking
    owner to evgenysobko;

create unique index parking_id_uindex
    on parking (id);