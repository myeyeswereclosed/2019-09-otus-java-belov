create table if not exists user(
    id serial primary key,
    name varchar not null,
    login varchar not null unique,
    password varchar not null,
    role varchar not null default 'CLIENT'
);