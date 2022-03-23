-- auto-generated definition
create schema if not exists interface_middleware collate utf8_bin;

create table t_field
(
    id            bigint auto_increment
        primary key,
    name          varchar(255) not null,
    default_value varchar(255) null,
    type          varchar(255) not null,
    remark        varchar(255) null
);

create table t_header
(
    id            bigint auto_increment
        primary key,
    pid           bigint       not null,
    name          varchar(255) not null,
    default_value varchar(255) null,
    remark        varchar(255) null
);

create table t_interface
(
    id             bigint auto_increment
        primary key,
    system_info_id bigint       not null,
    `key`          varchar(255) not null,
    name           varchar(255) not null,
    url            varchar(255) not null,
    description    varchar(255) null,
    method         varchar(255) not null,
    header         varchar(255) not null,
    enable         int          not null
);

create table t_scope
(
    id           bigint auto_increment
        primary key,
    field_id     bigint not null,
    interface_id bigint not null,
    essential    int    not null
);

create table t_system_info
(
    id             bigint auto_increment
        primary key,
    `key`          varchar(255) not null,
    name           varchar(255) not null,
    remark         varchar(255) null,
    app_id         varchar(255) null,
    app_secret_key varchar(255) null
);
