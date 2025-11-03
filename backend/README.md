create table if not exists ttwijang.mission_template
(
    uid              varchar(255)  not null
        primary key,
    auth_method_info varchar(255)  null,
    category_uid     varchar(255)  null,
    content          varchar(255)  null,
    create_date      datetime      null,
    title            varchar(255)  null,
    use_status       bit           not null
);


mission
template_uid