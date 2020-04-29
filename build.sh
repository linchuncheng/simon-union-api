git pull
mvn package
docker build -t union-api .
docker rm -f union-api
docker run -d -p "80:8080" --name=union-api union-api
