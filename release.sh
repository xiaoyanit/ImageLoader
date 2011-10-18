clear
echo ===========================================================================
echo RELEASE PROCESS
echo 1 mvn release:prepare
echo 2 mvn release:perform -Plocalrelease
echo 3 mvn android-release:prepare
echo 4 git commit of changes to the android manifests
echo
echo REQUIREMENTS
echo localrelease profile
echo
echo want to continue?
read
mvn release:prepare
echo 1/4 Release prepare end!
echo ===========================================================================
echo Should I continue to with relase:perform, have you set the localrelease profile?
read
clear
mvn release:perform -Plocalrelease
echo 2/4 Release performe end!
echo ===========================================================================
echo Should I continue to update all the android manifests with the new versions?
read
clear
mvn android-release:prepare
echo 3/4 Android manifests updates finish!
echo ===========================================================================
echo Should I continue to to push all the changes to git?
read
clear
git add --all
git commit -m "[android-manifest-version-update] version changes before starting a new development cycle"
git push
echo 4/4 Android manifests updates finish!
echo ===========================================================================
echo Build finished