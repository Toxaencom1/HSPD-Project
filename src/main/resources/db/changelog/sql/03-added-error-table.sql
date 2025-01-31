-- liquibase formatted sql

-- changeset Toxaencom1:1736510294885-22
create table errors
(
    id              uuid         not null
        primary key,
    username        varchar(60),
    message         text,
    error_timestamp timestamp(6) not null
);