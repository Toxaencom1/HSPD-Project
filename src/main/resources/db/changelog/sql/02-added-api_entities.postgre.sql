-- liquibase formatted sql

-- changeset Toxaencom1:1736510294885-15
CREATE TABLE "results"
(
    "id"                     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "date"                   date,
    "open"                   numeric(38, 2),
    "close"                  numeric(38, 2),
    "high"                   numeric(38, 2),
    "low"                    numeric(38, 2),
    "stock_response_data_id" BIGINT,
    CONSTRAINT "results_pkey" PRIMARY KEY ("id")
);

-- changeset Toxaencom1:1736510294885-16
CREATE TABLE "stock_response_data"
(
    "id"     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "ticker" VARCHAR(255),
    CONSTRAINT "stock_response_data_pkey" PRIMARY KEY ("id")
);

-- changeset Toxaencom1:1736510294885-17
CREATE TABLE "users_results"
(
    "user_id"   BIGINT NOT NULL,
    "result_id" BIGINT NOT NULL
);

-- changeset Toxaencom1:1736510294885-18
ALTER TABLE "results"
    ADD CONSTRAINT "fk20ai9ttf7rxpbry5rpyxcqbqq" FOREIGN KEY ("stock_response_data_id") REFERENCES "stock_response_data" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;


-- changeset Toxaencom1:1736510294885-19
ALTER TABLE "users_results"
    ADD CONSTRAINT "fk88lefgdelmxpth6e79mvwuknh" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;


-- changeset Toxaencom1:1736510294885-20
ALTER TABLE "users_results"
    ADD CONSTRAINT "fkfmtu5dr01ademsdvbt1d8kl56" FOREIGN KEY ("result_id") REFERENCES "results" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;
