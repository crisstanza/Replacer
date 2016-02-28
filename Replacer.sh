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
	echo wip...
}

testIt() {
	sleep 1
	cat out/One.txt
	cat out/Two.txt
	cat out/x/y/z/Car.java
	cat out/x/y/z/Entidade.java
}

if [ -z "$1" ] ; then
	echo
	echo "Error!"
	echo "Usage: Replacer.sh compileIt | clean[Bin|Classes|Out] | runIt | testIt"
	echo
else
	$1
fi
