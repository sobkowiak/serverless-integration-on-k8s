oc apply -f test/minio.yaml

kamel run API.java --open-api file:openapi.yaml --property file:test/minio.properties
kamel run API.java --open-api file:openapi.yaml --property file:test/minio.properties --dependency mvn:org.apache.camel.quarkus:camel-quarkus-openapi-java
kn service update api --scale-window 20s --scale-target=100

oc get integrations
oc get integration api -o yaml

URL=$(oc get routes.serving.knative.dev api -o jsonpath='{.status.url}')/v1
echo $URL

curl -k $URL/

curl -k -X PUT --header "Content-Type: application/octet-stream" --data-binary "@API.java" $URL/example

curl -k $URL/example

curl -k -X DELETE $URL/example

hey -c 500 -z 10s $URL/