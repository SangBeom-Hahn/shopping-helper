drop table if exists clothes_model_result;
drop table if exists clothes_upload_file;
drop table if exists message;

create table clothes_model_result
(
    result_id bigint not null auto_increment,
    rating tinyint not null check (rating between 0 and 4),
    shared boolean not null,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    message_id bigint not null unique,
    status_message varchar(50) not null,
    store_file_path varchar(255) not null,
    status enum ('ERROR','FINISH','WAIT') not null,
    primary key (result_id)
);

create table clothes_upload_file
(
    upload_id bigint not null auto_increment,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    message_id bigint not null unique,
    upload_file_name varchar(50) not null,
    store_file_path varchar(255) not null,
    primary key (upload_id)
);

create table message
(
    message_id bigint not null auto_increment,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    store_file_path varchar(255) not null,
    object_type enum ('CLOTHES'),
    primary key (message_id)
);

alter table clothes_model_result
    add constraint fk_result_clothes_message
        foreign key (message_id)
            references message;

alter table clothes_upload_file
    add constraint fk_upload_clothes_message
        foreign key (message_id)
            references message;