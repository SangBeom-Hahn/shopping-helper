drop table if exists search;
drop table if exists clothes_model_result;
drop table if exists clothes_upload_file;
drop table if exists message;

create table clothes_model_result
(
    result_id bigint not null auto_increment,
    rating int,
    shared boolean not null,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    message_id bigint not null unique,
    status_message varchar(50) not null,
    store_file_path varchar(255),
    review varchar(2000),
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
    status enum ('SUCCESS','FAIL') not null,
    status_message varchar(50) not null,
    primary key (upload_id)
);

create table message
(
    message_id bigint not null auto_increment,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    store_file_path varchar(255) not null,
    object_type enum ('T_SHIRTS', 'PANTS', 'SKIRT', 'HAT') not null,
    refine boolean not null,
    primary key (message_id)
);

create table search
(
    search_id bigint not null auto_increment,
    result_id bigint not null,
    thumbnail_url varchar(1000),
    web_search_url varchar(1000),
    host_page_url varchar(1000),
    site varchar(50) not null,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    primary key (search_id)
);

alter table clothes_model_result
    add constraint fk_result_clothes_message
        foreign key (message_id)
            references message (message_id);

alter table clothes_upload_file
    add constraint fk_upload_clothes_message
        foreign key (message_id)
            references message (message_id);

alter table search
    add constraint fk_search_result
        foreign key (result_id)
            references clothes_model_result (result_id);