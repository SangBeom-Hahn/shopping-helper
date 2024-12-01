insert into message
    (created_date,last_modified_date,object_type,store_file_path,refine)
values (NOW(),NOW(),'T_SHIRTS','path',false);

insert into clothes_model_result
    (created_date,last_modified_date,rating,shared,message_id,status_message,store_file_path,status,result_id)
values (NOW(),NOW(),0,false,1,'HI','C:\Users\hsb99\OneDrive\바탕 화면\X-song\assignmenting\프로젝트\sketch2fashion\result.png'
       ,'FINISH',1);

insert into clothes_upload_file
(created_date,last_modified_date,message_id,upload_file_name,store_file_path,status,status_message,upload_id)
values (NOW(),NOW(),1,'name','C:\Users\hsb99\OneDrive\바탕 화면\X-song\assignmenting\프로젝트\sketch2fashion\Untitled.png'
       ,'SUCCESS','성공',1);