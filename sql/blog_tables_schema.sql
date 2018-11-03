CREATE DATABASE IF NOT EXISTS blog;
USE blog;


CREATE TABLE t_blog
(
    id BIGINT,
    appreciation BIT,
    commentabled BIT,
    content VARCHAR(21845),
    create_time TIMESTAMP,
    first_picture VARCHAR(255),
    flag VARCHAR(255),
    published BIT,
    recommend BIT,
    share_statement BIT,
    title VARCHAR(255),
    update_time TIMESTAMP,
    views INT,
    type_id BIGINT,
    user_id BIGINT,
    description VARCHAR(255)
);

CREATE TABLE hibernate_sequence
(
    next_val BIGINT
);

CREATE TABLE t_blog_tags
(
    blogs_id BIGINT,
    tags_id BIGINT
);

CREATE TABLE t_comment
(
    id BIGINT,
    content VARCHAR(255),
    create_time TIMESTAMP,
    email VARCHAR(255),
    nickname VARCHAR(255),
    blog_id BIGINT,
    parent_comment_id BIGINT,
    avatar VARCHAR(255),
    admin_comment BIT
);

CREATE TABLE t_tag
(
    id BIGINT,
    name VARCHAR(255)
);

CREATE TABLE t_type
(
    id BIGINT,
    name VARCHAR(255)
);

CREATE TABLE t_user
(
    id BIGINT,
    avatar VARCHAR(255),
    create_time TIMESTAMP,
    email VARCHAR(255),
    nickname VARCHAR(255),
    password VARCHAR(255),
    type INT,
    update_time TIMESTAMP,
    username VARCHAR(255)
);