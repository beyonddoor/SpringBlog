#!/usr/bin/env bash

cp_jar() {
    echo "Copying $1..."
    rsync -av "../$1/build/libs/$1-0.0.1-SNAPSHOT.jar" "$1/$1-0.0.1-SNAPSHOT.jar"
}

run_jar() {
    echo "Starting $1..."
    (cd "$1" && nohup java -jar "$1"-0.0.1-SNAPSHOT.jar > out-"$2".log &)
}

check_service_arg() {
  case $1 in
    "echoserver"|"proxy_ws_netty"|"load_test_netty"|all)
        ;;
    *)
        echo "unknown service $1, only all, echoserver, proxy_ws_netty, load_test_netty are supported"
        exit 1
  esac
}

cd "$(dirname "$0")" || exit 1

case $1 in
    "cp")
        cp_jar echoserver
        cp_jar proxy_ws_netty
        cp_jar load_test_netty
        ;;

    "start")
        check_service_arg "$2"

        if [[ "$2" == "all" ]]; then
            #run_jar echoserver "$3"
            #run_jar proxy_ws_netty "$3"
            #run_jar load_test_netty "$3"
            exit 0
        fi

        run_jar "$2" "$3"
        ;;

    "stop")
        check_service_arg "$2"

        if [[ "$2" == "all" ]]; then
            pgrep -a java | grep -E 'echoserver|proxy_ws_netty|load_test_netty' | awk '{print $1}' | xargs kill
            exit 0
        fi

        pgrep -a java | grep -E "$2" | awk '{print $1}' | xargs kill
        ;;

    "restart")
        bash "$0" stop "$2"
        bash "$0" start "$2" "$3"
        ;;

    *)
        echo "Usage: $0 {cp|start [log-postfix]|restart [log-postfix]}|stop"
        exit 1
esac