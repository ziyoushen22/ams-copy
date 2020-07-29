-- 语法校验
../sbin/nginx -t
-- 重启
../sbin/nginx -s reload             -- stop


location /{
    proxy_pass http://30.79.113.95:8089/bi_app/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header x_real_ipP $remote_addr;
    proxy_set_header remote_addr $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection upgrade;
    proxy_http_version 1.1;
    proxy_connect_timeout 300s;
    proxy_send_timeout 300s;
}





