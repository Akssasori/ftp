# Criando ftp com docker

Os comandos abaixo server para **criação de um ftp com docker** para testarmos nosso código

### Windows
~~~windows
docker run -d -v C:/ftp:/home/vsftpd -p 20:20 -p 21:21 -p 47400-47470:47400-47470 -e FTP_USER=teste -e FTP_PASS=teste123 -e PASV_ADDRESS=127.0.0.1 --name ftp --restart=always bogem/ftp
~~~

### Linux
~~~linux
docker run -d -v c\ftp:/home/vsftpd -p 20:20 -p 21:21 -p 47400-47470:47400-47470 -e FTP_USER=teste -e FT
P_PASS=teste123 -e PASV_ADDRESS=127.0.0.1 --name ftp --restart=always bogem/ftp
~~~

