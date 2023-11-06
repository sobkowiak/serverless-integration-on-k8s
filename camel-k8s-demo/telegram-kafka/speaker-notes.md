oc apply -f infra/kafka.yaml

oc apply -f infra/minio.yaml
oc port-forward `oc get pods -l app=minio -o name` 9000
oc expose deployment minio --name minio-console --port 9001
oc expose svc minio-console

oc create secret generic telegram-config --from-literal=authorization-token=xxxxx

mvn quarkus:dev

URL=$(oc get routes.serving.knative.dev api -o jsonpath='{.status.url}')/v1
echo $URL

curl -k $URL/

oc apply -f camel-k/feedback-channel.yaml
kamel run camel-k/S3ToKnative.java --property file:camel-k/minio.properties
kamel run camel-k/S3ToKnative.groovy --property file:camel-k/minio.properties



kamel run camel-k/KnativeToSheets.groovy --property file:camel-k/google.properties
kn service update knative-to-sheets --scale-window 15s

kamel delete s3to-knative
kamel delete knative-to-sheets




mvn quarkus:add-extension -Dextensions='kubernetes-config'
mvn quarkus:add-extension -Dextensions='kubernetes'
mvn quarkus:add-extension -Dextensions='openshift'
mvn package -DskipTests -Dquarkus.container-image.build=true
mvn package -Dquarkus.kubernetes.deploy=true
# -Dquarkus.kubernetes.deployment-target=knative # kubernetes, openshift, knative, minikube