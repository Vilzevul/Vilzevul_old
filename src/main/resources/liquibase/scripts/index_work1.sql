--liquibase formatted sql


--changeSet Vilzevul:10
CREATE TABLE notificationtask
(
    id        SERIAL  NOT NULL PRIMARY KEY,
    id_chat   BIGINT ,
    message   TEXT,
    time_send TIMESTAMP
);
