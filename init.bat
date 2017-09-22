call "%M2_HOME%\bin\mvn" clean package -f registry\pom.xml
call "%M2_HOME%\bin\mvn" clean package -f configuration\pom.xml
call "%M2_HOME%\bin\mvn" clean package -f counter-service\pom.xml
call "%M2_HOME%\bin\mvn" clean package -f counter-api\pom.xml
call "%M2_HOME%\bin\mvn" clean package -f gateway\pom.xml

call docker-machine ssh counter-hub docker-compose -f /mnt/app/global.yml build
call docker-machine ssh counter-hub docker-compose -f /mnt/app/global.yml up -d
