#!/usr/bin/env bash

cp_jar() {
    cp ../$1/build/libs/$1-0.0.1-SNAPSHOT.jar $1
}

run_jar() {
    echo "Starting $1..."
    (cd $1 && nohup java -jar $1-0.0.1-SNAPSHOT.jar > out.log &)
}

cd "$(dirname $0)" || exit 1

case $1 in
    "cp")
        cp_jar echoserver
        cp_jar proxy_ws_netty
        cp_jar load_test_netty
        ;;
    "run")
        run_jar echoserver
        run_jar proxy_ws_netty
#        run_jar load_test_netty
        ;;
    *)
        echo "Usage: $0 {cp|run}"
        exit 1
esac