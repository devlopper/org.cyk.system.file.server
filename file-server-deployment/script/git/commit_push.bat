@echo off
For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
set mydate=%mydate: =0%
For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)
set mytime=%mytime: =0%
SET timestamp=v0.0.0-%mydate%%mytime%

SET current_directory=%CD%

@echo on
echo pom publishing
@echo off
cd E:\Repositories\source code\git\org\cyk\pom
git add .
git commit --all -m "file server publishing %timestamp%"
git push cyk_file_server develop:cyk_pom

@echo on
echo utility publishing
@echo off
cd E:\Repositories\source\git\org\cyk\utility
git add .
git commit --all -m "file server publishing %timestamp%"
git push cyk_file_server develop_0_1_0:cyk_utility

@echo on
echo file server publishing
@echo off
cd E:\Repositories\source\git\org\cyk\system\file\server
git add .
git commit --all -m "%timestamp%"
git push origin

cd %current_directory%