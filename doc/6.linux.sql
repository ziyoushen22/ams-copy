-- 模拟请求
curl -i "http://192.168.0.1:8080/bi_app/test01"     --     -i查看参数

-- 查看当前目录下最深2层目录有G大小的文件结构
du -sh . --max-depth 2|grep G
du -sh *

-- 查看含**字符的文件位置
grep -i ** *.log

-- 将文件压进jar
jar uvf **.jar BOOT-INF/classes/com/howtodoinjava/demo/controller/a.class

-- 新建多级目录
mkdir -r BOOT-INF/classes/com/howtodoinjava/demo/controller

-- 启动jar
nohup java -jar *.jar >> /dev/null 2>&1 &

-- 获取静态文件
wget "http://192.168.0.1:8080/bi_app/login.html"

-- 将本机文件复制到远程目录
scp /home/item.txt root@192.168.0.1:/etc/test
-- 将本机目录复制到远程目录
scp -r /home/test root@192.168.0.1:/etc/test
-- 将远程文件复制到本机目录
scp remote@www.abc.com:/usr/local/sin.sh /home/test
-- 将远程目录复制到本机目录
scp -r remote@www.abc.com:/usr/local /home/test

-- 跳板机操作
ssh root@192.168.0.1
