-- liquibase formatted sql

-- changeset Toxaencom1:1736510294885-22
create table errors
(
    id      uuid not null
        primary key,
    message text
);