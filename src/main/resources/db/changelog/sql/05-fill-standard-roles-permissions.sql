-- liquibase formatted sql

-- changeset Toxaencom1:1736510294885-24
INSERT INTO public.roles (roles)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO public.permission (name)
VALUES ('user_permission'),
       ('admin_permission');

INSERT INTO public.permission_roles (permissions_id, roles_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);