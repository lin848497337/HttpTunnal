#!/bin/sh
pid=`ps -ef | grep proxy-server-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`
echo "kill ${pid}"
kill ${pid}
