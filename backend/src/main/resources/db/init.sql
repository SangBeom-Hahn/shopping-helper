drop table if exists search;
drop table if exists transaction_log;
drop table if exists common_log;
drop table if exists inference_log;
drop table if exists clothes_model_result;
drop table if exists clothes_upload_file;
drop table if exists message;

create table clothes_model_result
(
    clothes_model_result_id bigint not null auto_increment,
    rating int,
    shared boolean not null,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    message_id bigint not null unique,
    status_message varchar(50) not null,
    store_file_path varchar(255),
    review varchar(2000),
    status enum ('ERROR','FINISH','WAIT') not null,
    primary key (clothes_model_result_id)
);

create table clothes_upload_file
(
    clothes_upload_file_id bigint not null auto_increment,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    message_id bigint not null unique,
    upload_file_name varchar(50) not null,
    store_file_path varchar(255) not null,
    status enum ('SUCCESS','FAIL') not null,
    status_message varchar(50) not null,
    primary key (clothes_upload_file_id)
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
    name varchar(500),
    site varchar(50) not null,
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    primary key (search_id)
);

create table transaction_log
(
    transaction_log_id bigint not null auto_increment,
    message_id bigint,
    request_uri varchar(100),
    site_name varchar(100),
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    primary key (transaction_log_id)
);

create table common_log
(
    common_log_id bigint not null auto_increment,
    message_id bigint not null,
    search_lookup_time varchar(150),
    search_click_count varchar(255),
    search_category_type varchar(50),
    queue_out_time varchar(150),
    inference_server_name varchar(100),
    inference_end_time varchar(150),
    created_date datetime(6) not null,
    last_modified_date datetime(6) not null,
    primary key (common_log_id)
);

create table inference_log
(
    inference_log_id bigint not null auto_increment,
    message_id bigint not null,
    content varchar(5000),
    primary key (inference_log_id)
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
            references clothes_model_result (clothes_model_result_id);