set EMAJ_PATH=C:\Projects\API\Elektron-SDK\Java\Ema
set ETAJ_PATH=C:\Projects\API\Elektron-SDK\Java\Eta
set CLASSPATH=.;%EMAJ_PATH%\Libs\ema.jar;%ETAJ_PATH%\Libs\upa.jar;%ETAJ_PATH%\Libs\upaValueAdd.jar;%EMAJ_PATH%\Libs\apache\org.apache.commons.collections.jar;%EMAJ_PATH%\Libs\apache\commons-configuration-1.10.jar;%EMAJ_PATH%\Libs\apache\commons-lang-2.6.jar;%EMAJ_PATH%\Libs\apache\commons-logging-1.2.jar;%EMAJ_PATH%\Libs\SLF4J\slf4j-1.7.12\slf4j-api-1.7.12.jar;%EMAJ_PATH%\Libs\SLF4J\slf4j-1.7.12\slf4j-jdk14-1.7.12.jar

javac -classpath %CLASSPATH% Consumer.java
