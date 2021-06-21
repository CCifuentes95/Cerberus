kubectl apply -f k8s
kubectl set image deployments/api-deployment cerberus-api=ccifuentes95/cerberus-api:$SHA
