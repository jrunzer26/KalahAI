Teamname="RNG"
rm algorithms/$Teamname.class
javac -cp Kalah.jar algorithms/$Teamname.java
if [ $1 == 1 ]; then
	./player1.sh | java -cp Kalah.jar:. algorithms.$Teamname
	echo "$Teamname player 1"
elif [ $1 == 2 ]; then
	./player2.sh | java -cp Kalah.jar:. algorithms.$Teamname
	echo "$Teamname player 2"
else 
	java -cp Kalah.jar:. algorithms.$Teamname	
fi



