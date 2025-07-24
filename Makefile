make:
	javac -cp src/main/java src/main/java/com/tavloo/*.java -d bin 
	javadoc src/main/java/com/tavloo/*.java -d docs
	jar -cfe TavlooServer.jar com.tavloo.Server -C bin/ .
	jar -cfe TavlooClient.jar com.tavloo.Client -C bin/ .

server: make
	java -jar TavlooServer.jar

client: make
	java -jar TavlooClient.jar

clean:
	rm *.jar
	rm -r bin docs
