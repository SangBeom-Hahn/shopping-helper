insert into message
    (created_date,last_modified_date,object_type,store_file_path,refine)
values (NOW(),NOW(),'T_SHIRTS','path',false);

insert into clothes_model_result
    (created_date,last_modified_date,rating,shared,message_id,status_message,store_file_path,status,clothes_model_result_id, review)
values (NOW(),NOW(),3,true,1,'채색이 완료되었습니다. 결과를 확인해주세요.','ztyle/result/d7494fad-a636-4372-84b2-3f5b055e26b0.png'
       ,'FINISH',1, '다운로드 받은 기본 이미지로 가이드에 맞게 따라하면, 검색 결과가 더 좋아요.');

insert into clothes_upload_file
(created_date,last_modified_date,message_id,upload_file_name,store_file_path,status,status_message,clothes_upload_file_id)
values (NOW(),NOW(),1,'name','ztyle/upload/d7494fad-a636-4372-84b2-3f5b055e26b0.png'
       ,'SUCCESS','업로드가 완료되었습니다. 검색이 완료되면 자동으로 화면이 전환됩니다. 잠시만 기다려주세요.',1);