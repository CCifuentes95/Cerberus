docker build -t ccifuentes95/cerberus-api:latest -t ccifuentes95/cerberus-api:$SHA .
docker push ccifuentes95/cerberus-api:latest
docker push ccifuentes95/cerberus-api:$SHA
