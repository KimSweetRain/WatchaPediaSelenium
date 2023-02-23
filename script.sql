use watcha1234; 

drop table tb_movie;
drop table tb_tv;
drop table tb_book;
drop table tb_comment;
drop table tb_boxoffice;
drop table tb_webtoon;

create table tb_movie (
	mov_idx int auto_increment primary key,
    mov_thumbnail text,
    mov_title varchar(100) not null,
    mov_title_org varchar(100) not null,
    mov_making_date varchar(20) not null,
    mov_country varchar(20) not null,
    mov_genre varchar(20) not null,
    mov_time varchar(20) not null,
    mov_age varchar(10),
    mov_people text,
    mov_summary text,
    mov_gallery text,
    mov_video text,
    mov_watch text,
    mov_back_img text
);

create table tb_tv (
	tv_idx int auto_increment primary key,
    tv_thumbnail text,
    tv_title varchar(100) not null,
    tv_title_org varchar(100) not null,
    tv_making_date varchar(20) not null,
    tv_channel varchar(20) not null,
    tv_genre varchar(20) not null,
    tv_country varchar(20) not null,
    tv_age varchar(10),
    tv_people text,
    tv_summary text,
    tv_watch text,
    tv_gallery text,
    tv_video text,
    tv_back_img text
);

create table tb_book (
	book_idx int auto_increment primary key,
    book_thumbnail text,
    book_title varchar(100) not null,
    book_title_sub varchar(100) not null,
    book_writer varchar(20) not null,
    book_category varchar(20),
    book_at_date varchar(20) not null,
    book_page varchar(10) not null,
    book_age varchar(10),
    book_summary text,
    book_people text,
    book_content_idx text,
    book_pub_summary text,
    book_back_img text,
    book_buy text
);

create table tb_webtoon (
	web_idx int auto_increment primary key,
    web_thumbnail text,
    web_title varchar(100) not null,
    web_title_org varchar(100) not null,
    web_writer varchar(20) not null,
    web_genre varchar(20) not null,
    web_ser_detail varchar(20) not null,
    web_ser_day varchar(10),
    web_ser_period varchar(30) not null,
    web_age varchar(10),
    web_summary text,
    web_people text,
    web_watch text
);

drop table tb_person;
create table tb_person (
	per_idx int auto_increment primary key,
    per_name varchar(20) not null,
    per_photo text,
    per_role varchar(20) not null,
    per_mov text,
    per_book text,
    per_webtoon text,
    per_tv text,
    per_biography text
);
create table tb_boxoffice(
	box_rangking int primary key,
	box_mov_idx int,
	box_mov_title varchar(100) not null,
    box_booking	int not null,
    box_spectators int not null
);

drop table tb_watcha_top10;
create table tb_watcha_top10(
	wat_idx int primary key auto_increment,
	wat_rangking int not null,
	wat_content_type varchar(10) not null,
	wat_content_idx int,
	wat_content_title varchar(100) not null
);
drop table tb_netflix_top10;
create table tb_netflix_top10(
	net_idx int primary key auto_increment,
	net_rangking int not null,
	net_content_type varchar(10) not null,
	net_content_idx int,
	net_content_title varchar(100) not null
);

create table tb_comment (
	comm_idx int auto_increment primary key,
    comm_user_idx int not null,
    comm_name varchar(20) not null,
    comm_text text not null,
    comm_content_type varchar(10) not null,
    comm_content_idx int not null,
    comm_reg_date datetime not null
)