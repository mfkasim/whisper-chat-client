Build:
1. You will need MAVEN to be able to build the project. 

	mvn clean package
	
========================================================================

Run:

1. Run Geth using the following command, allowing for new network creation and JSON-RPC interface.

	./geth --datadir node1 --networkid 98765 console --rpc --shh

2. Run the application using the following 

	java -jar ./EthTestApp-0.0.1-SNAPSHOT.jar
	
	
3. Repeat no. 3 for adding a new chat member

4. Make sure to make the same password and topic in each chat member.


