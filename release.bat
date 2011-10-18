mvn release:prepare
mvn release:perform -Plocalrelease
mvn android-release:prepare
git add --all
git commit -m "[android-manifest-version-update] version changes before starting a new development cycle"
git push