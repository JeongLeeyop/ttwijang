select (select count(p1.*) from POST_LIKE p1 where post_id = 1) as likeCnt
,(select count(p2.id) > 0 from POST_LIKE p2 where post_id = 1) as likeCk
, p.* from POST  p left outer join POST_LIKE  pl on p.id = pl.post_id where p.id = 1