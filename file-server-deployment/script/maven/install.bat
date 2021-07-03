cd ../../..
cd file-server-persistence-entities/script/maven
call install.bat

cd ../../..
cd file-server-persistence-api/script/maven
call install.bat

cd ../../..
cd file-server-persistence-impl/script/maven
call install.bat

cd ../../..
cd file-server-business-entities/script/maven
call install.bat

cd ../../..
cd file-server-business-api/script/maven
call install.bat

cd ../../..
cd file-server-business-impl/script/maven
call test.bat
call install.bat

cd ../../..
cd file-server-representation-entities/script/maven
call install.bat

cd ../../..
cd file-server-representation-api/script/maven
call install.bat

cd ../../..
cd file-server-representation-impl/script/maven
call install.bat

cd ../../..
cd file-server-deployment/script/maven