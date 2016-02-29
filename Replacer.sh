#/bin/sh

VERSION=1.0.0

MAIN_PACKAGE=replacer.main
MAIN_CLASS=MainReplacer

CP=.
CP=${CP}:classes

clear

clean() {
	cleanBin
	cleanClasses
	cleanOut
}

cleanBin() {
	rm -rf bin/*
}

cleanClasses() {
	rm -rf classes/*
}

cleanOut() {
	rm -rf out/*
}

runIt() {
	java -cp ${CP} ${MAIN_PACKAGE}.${MAIN_CLASS}
}

compileIt() {
	find src | grep .java > files.txt
	javac -cp ${CP} -d classes @files.txt
	
	rm files.txt
}

testIt() {
	cat out/One.txt
	cat out/Two.txt
	cat out/x/y/z/Car.java
	cat out/x/y/z/Pessoa.java
	cat out/car.html
	cat out/pessoa.html
}

AppleShowAllFiles() {
	defaults write com.apple.finder AppleShowAllFiles YES
	sudo killall Finder
}

AppleHideAllFiles() {
	defaults write com.apple.finder AppleShowAllFiles NO
	sudo killall Finder
}

if [ -z "$1" ] ; then
	echo
	echo "Error!"
	echo "Usage: ./Replacer.sh (compileIt | clean[Bin|Classes|Out] | runIt | testIt | Apple(Show|Hide)AllFiles)"
	echo
else
	$1
fi
