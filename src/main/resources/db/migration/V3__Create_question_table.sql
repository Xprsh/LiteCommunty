create table question
(
	id int auto_increment,
	title varchar(50),
	detail varchar(1000),
	comment_count int default 0,
	gmt_modified bigint,
	creator int,
	gmt_create bigint,
	view_count int default 0,
	like_count int default 0,
	tags varchar(256),
	constraint QUESTION_PK
		primary key (id)
);

