set M2_HOME=%M2_HOME%
set JAVA_HOME=%JAVA_8_HOME%

@echo off
docker-machine rm counter-hub -f
docker-machine create -d virtualbox --virtualbox-memory "8192" --virtualbox-cpu-count "3" counter-hub

docker-machine ip counter-hub > IP
SET /p HUB= < IP
DEL IP

docker-machine ssh counter-hub sudo curl -L "https://github.com/docker/compose/releases/download/1.10.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
docker-machine ssh counter-hub sudo chmod +x /usr/local/bin/docker-compose
docker-machine ssh counter-hub sudo mkdir /mnt/app
docker-machine ssh counter-hub sudo sysctl -w vm.max_map_count=2621440
echo "Mount application point as [app]"
pause
docker-machine ssh counter-hub sudo mount -t vboxsf app /mnt/app

call init.bat

cls
echo *******************************************************
echo http://%HUB%:8080 - Kibana (logging service)
echo http://%HUB%:8100 - Eureka (service discovery)
echo http://%HUB%:8090 - Zipkin (tracing)
echo http://%HUB% - Application
echo *******************************************************
pause
@echo on