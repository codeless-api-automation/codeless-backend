mvn clean package
docker build -t apisentinel/codeless-backend .
docker push apisentinel/codeless-backend:latest