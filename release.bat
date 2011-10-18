mvn release:prepare
mvn release:perform -Plocalrelease
mvn android:release
git add --all
git commit -m "updating android version before starting a new development cycle"
git push