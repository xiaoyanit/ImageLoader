clear
mvn release:prepare
echo Continue?
read
clear
mvn release:perform -Plocalrelease
echo Continue?
read
clear
mvn android-release:prepare
echo Continue?
read
clear
git add --all
git commit -m "[android-manifest-version-update] version changes before starting a new development cycle"
git push
echo Build success