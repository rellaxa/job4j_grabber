create table posts(
    id           serial primary key,
    title        varchar(100),
    "link"       text unique,
    description  text,
    created_date timestamp
);