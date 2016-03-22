#!/bin/bash

stats=$(cat results.txt)

player1=(${stats:0:1})
player2=(${stats:2:1})

while true; do 
	sleep 3
	stats=$(cat results.txt)
	player1=(${stats:0:1})
	player2=(${stats:2:1})
	total=$((player1 + player2))
	echo $total
	p1stat=$(expr "$player1"/"$total"*100)
	p1stat=$(expr "$player1"/"$total"*100)
	echo "Player 1 wins: $player1 Win %: $p1stat"
	echo "Player 2 wins: $player2 Win %: $p2stat"
done

