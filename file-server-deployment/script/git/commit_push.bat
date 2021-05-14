@echo off
For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
set mydate=%mydate: =0%
For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)
set mytime=%mytime: =0%
SET timestamp=%mydate%%mytime%
SET commit_message="file server publishing %timestamp%"
SET tag_name="file_server_v%timestamp%"
SET tag_version=%tag_name%
SET current_directory=%CD%

@echo on
echo pom publishing
@echo off
cd E:\Repositories\source code\git\org\cyk\pom
git add .
git commit --all -m %commit_message%
git tag -a %tag_name% -m %tag_version%
git push cyk_file_server develop:cyk_pom
git push cyk_file_server %tag_name% develop:cyk_pom

@echo on
echo utility publishing
@echo off
cd E:\Repositories\source\git\org\cyk\utility
git add .
git commit --all -m %commit_message%
git tag -a %tag_name% -m %tag_version%
git push cyk_file_server develop_0_1_0:cyk_utility
git push cyk_file_server %tag_name% develop_0_1_0:cyk_utility

@echo on
echo file server publishing
@echo off
cd E:\Repositories\source\git\org\cyk\system\file\server
git add .
git commit --all -m %commit_message%
git tag -a v%timestamp% -m "version of %timestamp%"
git push origin
git push origin v%timestamp%

cd %current_directory%