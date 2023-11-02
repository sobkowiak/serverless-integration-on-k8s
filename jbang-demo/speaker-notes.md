- install `jbang`
```
curl -Ls https://sh.jbang.dev | bash -s - app setup
```
- install `camel`
```
jbang app install camel@apache/camel
```
- check `camel` version
```
camel -V
jbang run -Dcamel.jbang.version=3.20.1 camel@apache/camel -V
```

- Generate Camel route
```
camel init Routes.java
```
- Run Camel route
```
camel run Routes.java --dev
```

- check Quarkus dependencies
```
camel dependencies --runtime quarkus
```
- check Spring Boot dependencies
```
camel dependencies --runtime spring-boot
```

- export to Quarkus project
```
camel export --runtime=quarkus --gav=com.foo:acme:1.0-SNAPSHOT --directory=test-quarkus
```
- export to Spring Boot project
```
camel export --runtime=spring-boot --gav=com.foo:acme:1.0-SNAPSHOT --directory=test-sb
```
- build Quarkus project
```
cd test-quarkus
mvn quarkus:dev
``` 

- build Spring Boot project
```
cd test-sb
mvn spring-boot:run
``` 




camel init Sample.java

camel dependency list --runtime quarkus
camel dependency list --runtime spring-boot

camel run Sample.java --dev

camel export --runtime=quarkus --gav=org.apache.camel:camel-demo:1.0-SNAPSHOT --directory=test-quarkus

cd test-quarkus

mvn quarkus:dev

cd ..

camel init Routes.groovy
camel init Routes.js
camel init Routes.yaml

camel run Routes.yaml --dev

rm *.js *.groovy

camel init Routes.groovy
camel run Routes.groovy --dev

camel init Routes.js
camel run Routes.js --dev

rm *.js *.groovy

camel dependency list  --runtime quarkus
camel dependency list  --runtime spring-boot

camel export --runtime=quarkus --gav=org.apache.camel:camel-demo:1.0-SNAPSHOT --directory=test-quarkus

mvn quarkus:dev