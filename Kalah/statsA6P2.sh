#!/bin/bash
Player1name="RNG"
Player2name="Algorithm6"
filename=$Player1name$Player2name


while true; do
	./player1.sh | java -cp Kalah.jar:. algorithms.$Player1name &
	PID=$!
	./player2.sh | java -cp Kalah.jar:. algorithms.$Player2name > $filename.o &
	PID2=$!
	sleep 4
	echo $PID
	echo $PID2
	pkill -15 java
	pkill -TERM -P $PID
	pkill -TERM -P $PID2
	win=$(tail -2 $filename.o | head -1)
	stats=$(cat $filename.results)

	player1=$(echo $stats | head -n1 | awk '{print $1;}')
	player2=$(echo $stats | head -n1 | awk '{print $2;}')
	tie=$(echo $stats | head -n1 | awk '{print $3;}')
	echo $player1
	echo $player2
	echo $tie

	if [ ${win:7:1} == "1" ]; then
		player1=$((player1 + 1))
		cat $filename.o >> $filename.losses
	elif [ ${win:7:1} == "2" ]; then
		player2=$((player2 + 1))
	else
		tie=$((tie + 1))
		cat $filename.o >> $filename.ties
	fi
	
	echo $player1 > $filename.results
	echo $player2 >> $filename.results
	echo $tie >> $filename.results
	cat $filename.results
done
