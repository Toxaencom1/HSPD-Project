--liquibase formatted sql

-- changeset Toxaencom1:1736510294885-12
INSERT INTO public.roles (id, roles)
VALUES (1, 'USER'),
       (2, 'ADMIN');

-- changeset Toxaencom1:1736510294885-13
INSERT INTO public.permission (id, name)
VALUES (1, 'get_user_permission'),
       (2, 'post_user_permission');

-- changeset Toxaencom1:1736510294885-14
INSERT INTO public.permission_roles (permissions_id, roles_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);