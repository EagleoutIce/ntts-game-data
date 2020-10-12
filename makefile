# Bisher noch relativ leer :D

all: target/game-data.jar

target/game-data.jar: pom.xml src/main/java/de/uulm/team020/GameData.java
	mvn clean install -Dmaven.javadoc.skip=true