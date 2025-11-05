# http-trigger-cloud-function

# Concepts to rember while building cloud function:

1. VPC Connector - We need to create vpc connector in GCP. For that we need vpc network with dedicated subnet range of CIDR /28 e.g. 10.2.0.0/28
2. In cloud build yaml file, we need to provide below settings:
   `entry-point: entrypoint of application`

        `service-account: Service account to run cloud function`

        `region`

        `no-gen2 (If going to use first generation cloud function)`

        `runtime: java17 or nodejs20 or any other which you are using`

        `max-instances : instances count max`

        `source: source of cloudbuild.yaml`

        `memory: memory to be used`

        `vpc-connector: created vpc connector in GCP`

        `egress-settings: Egress settings`

        `ingress-settings: internal-only`

        `trigger-topic: Use it if using pubsub trigger`

        `trigger-http: Use it if using http trigger`

        `trigger-source: Use it only if using cloud storage or firestore trigger`
         
         `trigger-event: Use it only if using cloud storage or firestore trigger`

3. When using first gen cloud function entrypoint class implements BackgroundFunction<T> e.g. BackgroundFunction<PubsubMessage>, BackgroundFunction<ObjectStorageData> or HttpFunction for http trigger
4. When using second gen cloud function entrypoint class implements CloudEvents generic interface.