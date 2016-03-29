#!/bin/bash
Player1name="Algorithm5"
Player2name="RNG"
filename=$Player1name$Player2name
stats=$(cat $filename.results)
echo $stats

player1=$(echo $stats | head -n1 | awk '{print $1;}')
player2=$(echo $stats | head -n1 | awk '{print $2;}')
tie=$(echo $stats | head -n1 | awk '{print $3;}')

while true; do 
	sleep 40
	stats=$(cat $filename.results)
	player1=$(echo $stats | head -n1 | awk '{print $1;}')
	player2=$(echo $stats | head -n1 | awk '{print $2;}')
	tie=$(echo $stats | head -n1 | awk '{print $3;}')
	total=$((player1 + player2 + tie))
	echo "Total: $total"
	p1stat=$(bc -l<<<"$player1/$total*100")
	p2stat=$(bc -l<<<"$player2/$total*100")
	tiestat=$(bc -l<<<"$tie/$total*100")
	echo "Player 1 wins: $player1 Win %: $p1stat"
	echo "Player 2 wins: $player2 Win %: $p2stat"
	echo "Ties: $tie Win %: $tiestat"
done

