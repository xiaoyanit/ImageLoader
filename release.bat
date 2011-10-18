mvn release:prepare
mvn release:perform -Plocalrelease
mvn android:release
git add --all
git commit -m "[android-manifest-version-update] version changes before starting a new development cycle"
git push