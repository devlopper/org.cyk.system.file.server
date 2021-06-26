For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
set mydate=%mydate: =0%
For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)
set mytime=%mytime: =0%
SET timestamp=v0.0.0-%mydate%%mytime%

SET current_directory=%CD%

echo cyk pom publishing
cd E:\Repositories\source code\git\org\cyk\pom
git commit -a -m cyk file server publishing %timestamp%
git push cyk_file_server develop:cyk_pom

echo cyk utility publishing
cd E:\Repositories\source\git\org\cyk\utility
git commit -a -m cyk utility server publishing %timestamp%
git push cyk_file_server develop_0_1_0:cyk_utility

cd %current_directory%