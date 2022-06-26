make:
	javac -cp /src src/*.java -d bin 
	javadoc src/*.java -d docs
	jar -cvfm TavlooServer.jar manifestServer.mf -C bin/ . src/
	jar -cvfm TavlooClient.jar manifestClient.mf -C bin/ . src/

server: make
	java -jar TavlooServer.jar

client: make
	java -jar TavlooClient.jar

clean:
	rm *.jar
	rm -r bin docs
