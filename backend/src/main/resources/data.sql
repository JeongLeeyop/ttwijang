


insert into board values (1,'영양상담');
insert into board values (2,'건강피드');
insert into board values (3,'식단후기');
insert into board values (4,'공지사항');
insert into board values (5,'서비스문의');

insert into category values (1,'영양정보');
insert into category values (2,'식재료');
insert into category values (3,'레시피');
insert into category values (4,'다이어트');
insert into category values (5,'식단관리');
insert into category values (6,'영양관리');
insert into category values (7,'식습관');

INSERT INTO account (id, activity_level, authority, birth, created_date, dest_date, dest_weight, diet_check, diet_precaution, 
diet_purpose, email, gender, height, name, password, point, status, tel, weight, thumbnail_file_uid) 
VALUES (1, 1, 0, 19941226, now(), 2022-12-31, 75, 0, 0, 0, 'leeyop12@gmail.com', 0, 180, '정이욥', '2c835ba8966d902120fb4504037fad34effa4b9461e988e4c4da073ad50dae82', 0, null, 01044493118, 65, null);
INSERT INTO account (id,activity_level, authority, birth, created_date, dest_date, dest_weight, diet_check, diet_precaution,
diet_purpose, email, gender, height, name, password, point, status, tel, weight, thumbnail_file_uid)
VALUES (2,1, 0, 19941226, now(), 2022-12-31, 75, 0, 0, 0, 'test1', 0, 180, '테스트유저1', '2c835ba8966d902120fb4504037fad34effa4b9461e988e4c4da073ad50dae82', 0, null, 01012341234, 65, null);
INSERT INTO account (id,activity_level, authority, birth, created_date, dest_date, dest_weight, diet_check, diet_precaution, 
diet_purpose, email, gender, height, name, password, point, status, tel, weight, thumbnail_file_uid) 
VALUES (3,1, 0, 19941226, now(), 2022-12-31, 75, 0, 0, 0, 'test2', 0, 180, '테스트유저2', '2c835ba8966d902120fb4504037fad34effa4b9461e988e4c4da073ad50dae82', 0, null, 01012341234, 65, null);
INSERT INTO account (id,activity_level, authority, birth, created_date, dest_date, dest_weight, diet_check, diet_precaution, 
diet_purpose, email, gender, height, name, password, point, status, tel, weight, thumbnail_file_uid) 
VALUES (4,1, 0, 19941226, now(), 2022-12-31, 75, 0, 0, 0, 'test3', 0, 180, '테스트유저3', '2c835ba8966d902120fb4504037fad34effa4b9461e988e4c4da073ad50dae82', 0, null, 01012341234, 65, null);

insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(1,1,1,1,'내용1','0',now(),null,'Y','제목1',null,'Y');
insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(2,1,1,2,'내용2','0',now(),null,'Y','제목2',null,'Y');
insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(3,1,1,3,'내용3','0',now(),null,'Y','제목3',null,'Y');

insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(4,1,2,1,'내용1','0',now(),null,'Y','제목1',null,'Y');
insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(5,1,2,2,'내용2','0',now(),null,'Y','제목2',null,'Y');
insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(6,1,2,3,'내용3','0',now(),null,'Y','제목3',null,'Y');

insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(7,1,3,1,'내용1','0',now(),null,'Y','제목1',null,'Y');
insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(8,1,3,2,'내용2','0',now(),null,'Y','제목2',null,'Y');
insert into post(id,account_id,board_id,category_id,content,count,created_date,score,secret_yn,title,updated_date,use_yn) 
values(9,1,3,3,'내용3','0',now(),null,'Y','제목3',null,'Y');

insert into tag values(1,7,'태그1');
insert into tag values(2,1,'태그2');
insert into tag values(3,2,'태그3');
insert into tag values(4,2,'태그4');
insert into tag values(5,2,'태그5');
insert into tag values(6,1,'태그6');

insert into post_tag values (1,1,1);
insert into post_tag values (2,1,2);
insert into post_tag values (3,1,3);

insert into post_tag values (4,2,3);
insert into post_tag values (5,2,4);
insert into post_tag values (6,2,5);

insert into post_tag values (7,3,4);
insert into post_tag values (8,3,5);
insert into post_tag values (9,3,6);

insert into post_tag values (10,4,1);
insert into post_tag values (11,4,2);
insert into post_tag values (12,4,3);

insert into post_tag values (13,5,4);
insert into post_tag values (14,5,5);
insert into post_tag values (15,5,6);

insert into post_tag values (16,6,2);
insert into post_tag values (17,6,3);
insert into post_tag values (18,6,4);

insert into custom_diet_config(name, type) values('식단형태','샐러드|도시락');
insert into custom_diet_config(name, type) values('식단목적','체중감량|영양밸런스');
insert into custom_diet_config(name, type) values('식단유의사항','없음|비건|알레르기');
insert into custom_diet_config(name, type) values('식사횟수','5|7|10|14');
insert into custom_diet_config(name, type) values('픽업요일','수요일|금요일');
insert into custom_diet_config(name, type) values('희망기간','1주|2주|4주|8주');

INSERT INTO ttwijang.shop (id, address, break_time, description, name, tel, weekday_time, weekend_time, pick_up_time) VALUES (1, '경남 진주시1 ...', '1', '1', '1', '1', '1', '1', '1시|2시|3시');
INSERT INTO ttwijang.shop (id, address, break_time, description, name, tel, weekday_time, weekend_time, pick_up_time) VALUES (2, '경남 진주시2 ...', '2', '2', '2', '2', '2', '2', '1시|3시');
INSERT INTO ttwijang.shop (id, address, break_time, description, name, tel, weekday_time, weekend_time, pick_up_time) VALUES (3, '경남 진주시3 ...', '3', '3', '3', '3', '3', '3', '1시|2시|3시|4시');

INSERT INTO ttwijang.items (id, calorie, description, diet_precaution, diet_purpose, diet_type, material, name, price, status, supplier, weight,shop_id) VALUES (1, '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',1);
INSERT INTO ttwijang.items (id, calorie, description, diet_precaution, diet_purpose, diet_type, material, name, price, status, supplier, weight,shop_id) VALUES (2, '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',2);
INSERT INTO ttwijang.items (id, calorie, description, diet_precaution, diet_purpose, diet_type, material, name, price, status, supplier, weight,shop_id) VALUES (3, '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',3);

INSERT INTO ttwijang.coupon (id, created_date, discount_rate, period, sub_title, title) VALUES (1, '2023-01-06 01:12:55', 10, '2024-01-06 01:13:00', '신규회원 환영 쿠폰1', '쿠폰1');
INSERT INTO ttwijang.coupon (id, created_date, discount_rate, period, sub_title, title) VALUES (2, '2023-01-07 01:12:55', 10, '2024-02-06 01:13:00', '신규회원 환영 쿠폰2', '쿠폰2');
INSERT INTO ttwijang.coupon (id, created_date, discount_rate, period, sub_title, title) VALUES (3, '2023-01-08 01:12:55', 10, '2024-03-06 01:13:00', '신규회원 환영 쿠폰3', '쿠폰3');

INSERT INTO ttwijang.user_coupon (id, account_id, coupon_id, down_yn,use_yn, due_date) VALUES (1, 1, 1, 'Y','N', '2024-01-06 01:22:12');
INSERT INTO ttwijang.user_coupon (id, account_id, coupon_id, down_yn, use_yn, due_date) VALUES (2, 1, 2, 'N', 'N','2024-01-06 01:22:12');
INSERT INTO ttwijang.user_coupon (id, account_id, coupon_id, down_yn, use_yn, due_date) VALUES (3, 1, 3, 'N', 'N','2024-01-06 01:22:12');

INSERT INTO ttwijang.diary (id, account_id, date, fat_rate, muscle_rate, weight) VALUES (1, 1, '2023-01-08 19:46:45', '111', '111', '111');
INSERT INTO ttwijang.diary (id, account_id, date, fat_rate, muscle_rate, weight) VALUES (2, 1, '2023-01-08 19:46:45', '222', '222', '333');
INSERT INTO ttwijang.diary (id, account_id, date, fat_rate, muscle_rate, weight) VALUES (3, 1, '2023-01-08 19:46:45', '333', '333', '333');

INSERT INTO ttwijang.food (id, iron, calcium, carbohydrate, cholesterol, created_date, dietary_fiber, fat, kcal, name, natrium, potassium, protein, ref, s_fatty_acid, sugar, trans_fat, input_type, vitaminc, weight) VALUES (1, '1', '1', '1', '1', '1970-01-15', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO ttwijang.food (id, iron, calcium, carbohydrate, cholesterol, created_date, dietary_fiber, fat, kcal, name, natrium, potassium, protein, ref, s_fatty_acid, sugar, trans_fat, input_type, vitaminc, weight) VALUES (2, '2', '2', '2', '2', '1970-02-25', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2');
INSERT INTO ttwijang.food (id, iron, calcium, carbohydrate, cholesterol, created_date, dietary_fiber, fat, kcal, name, natrium, potassium, protein, ref, s_fatty_acid, sugar, trans_fat, input_type, vitaminc, weight) VALUES (3, '3', '3', '3', '3', '1970-03-25', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3');
