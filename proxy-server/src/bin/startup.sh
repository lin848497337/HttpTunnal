#!/bin/sh
nohup java -jar -Xms256m -Xmx256m proxy-server-1.0-SNAPSHOT.jar 8000  > server.log  &
