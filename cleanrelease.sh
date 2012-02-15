clean() {
  rm acceptance/pom.xml.releaseBackup
  rm core/pom.xml.releaseBackup
  rm pom.xml.releaseBackup
  rm demo/pom.xml.releaseBackup
  rm release.properties
  git checkout acceptance/pom.xml
  git checkout core/pom.xml
  git checkout pom.xml
  git checkout demo/pom.xml 
}

clean
