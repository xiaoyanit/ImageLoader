clear
echo ===========================================================================
echo RELEASE PROCESS
echo 1 mvn release:prepare
echo 2 mvn release:perform -Plocalrelease
echo
echo REQUIREMENTS
echo localrelease profile
echo
echo want to continue?
read
mvn release:prepare
echo 1/2 Release prepare end!
echo ===========================================================================
echo Should I continue to with relase:perform, have you set the localrelease profile?
read
clear
mvn release:perform -Plocalrelease
echo 2/2 Release performe end!
echo ===========================================================================
echo Build finished