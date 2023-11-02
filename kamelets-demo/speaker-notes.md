oc apply -f log-sink.kamelet.yaml
oc apply -f timer-source.kamelet.yaml

oc get kamelets

kamel bind timer-source -p source.message='Hello from Kamelets' log-sink -o yaml

oc get integration timer-source-to-log-sink -o yaml

kamel delete timer-source-to-log-sink

oc apply -f timer-to-log-binding.yml 