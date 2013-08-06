
mvn gpg:sign-and-deploy-file -DpomFile=target/checkout/core/pom.xml -Dfile=target/checkout/core/target/imageloader-core.jar -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging

mvn gpg:sign-and-deploy-file -DpomFile=target/checkout/core/pom.xml -Dfile=target/checkout/core/target/imageloader-core-sources.jar -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -Dclassifier=sources

mvn gpg:sign-and-deploy-file -DpomFile=target/checkout/core/pom.xml -Dfile=target/checkout/core/target/imageloader-core-javadoc.jar -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -Dclassifier=javadoc
