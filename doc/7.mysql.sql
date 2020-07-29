


-- mysql日志文件饱满解决方法
mysql -u root -p
reset master
-- /usr/local/mysql/my.cnf
#log-bin=mysql-bin
#expire_logs_days=7
service mysql status
service mysql restart
