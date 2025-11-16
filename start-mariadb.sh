podman run -d \
  --name mariadb \
  -e MARIADB_ROOT_PASSWORD=your_root_password \
  -e MARIADB_DATABASE=mariadb \
  -e MARIADB_USER=mariadb \
  -e MARIADB_PASSWORD=your_strong_password \
  -p 3306:3306 \
  docker.io/library/mariadb:latest

