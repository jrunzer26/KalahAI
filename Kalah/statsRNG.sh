#!/bin/bash

Teamname="RNG"
while true; do
	./player1.sh | java -cp Kalah.jar:. algorithms.$Teamname &
	PID=$!
	./player2.sh | java -cp Kalah.jar:. algorithms.$Teamname > output.txt &
	PID2=$!
	sleep 3
	echo $PID
	echo $PID2
	pkill -15 java
	pkill -TERM -P $PID
	pkill -TERM -P $PID2
	win=$(tail -2 output.txt | head -1)
	stats=$(cat results.txt)

	player1=(${stats:0:1})
	player2=(${stats:2:1})

	if [ ${win:7:1} == "1" ]; then
		player1=$((player1 + 1))
	elif [ ${win:7:1} == "2" ]; then
		player2=$((player2 + 1))
	fi

	echo $player1 > results.txt
	echo $player2 >> results.txt
	cat results.txt
done
