SET teamname=RNG
javac -cp Kalah.jar algorithms/%teamname%.java
java -cp Kalah.jar;. algorithms.%teamname%
pause